#include <iostream>
#include <fstream>
#include <vector>
#include <unordered_map>
#include <string>
#include <thread>
#include <mutex>
#include <random>
#include <chrono>

using namespace std;

typedef struct
{
    int serialNumber;
    int amount;
    int sourceId;
    int destinationId;
} Operation;

typedef struct
{
    int accountId;
    int initialBalance;
    int currentBalance;
    mutex *mtx;
    vector<Operation> log;
} Account;

const string ACCOUNTS_FILE_PATH = "accounts.txt";
const int NUMBER_OF_THREADS = 4;
const int NUMBER_OF_OPERATIONS_PER_THREAD = 5;

int currentSerialNumber = 0;
unordered_map<int, Account> accounts;
vector<int> accountIds;

mutex serialNumberMutex;
mutex writeOperationDetailsMutex;

void readAccountsFromFile(string filePath)
{
    ifstream inputFile(filePath);

    if (!inputFile.is_open())
    {
        cerr << "Error: Unable to open the file " << filePath << endl;
        return;
    }

    while (inputFile)
    {
        int accountId, balance;
        inputFile >> accountId >> balance;

        if (!inputFile.fail())
        {
            Account account;
            account.accountId = accountId;
            account.initialBalance = balance;
            account.currentBalance = balance;
            account.mtx = new mutex;
            accounts[accountId] = account;

            accountIds.push_back(accountId);
        }
    }

    inputFile.close();
}

int generateRandomNumberInRange(int min, int max)
{
    random_device rd;
    mt19937 mt(rd());
    uniform_int_distribution<int> dist(min, max);
    return dist(mt);
}

int getRandomAccount()
{
    return accountIds[generateRandomNumberInRange(0, accounts.size() - 1)];
}

int getRandomAmount()
{
    return generateRandomNumberInRange(10, 30);
}

int getRandomDelay()
{
    return generateRandomNumberInRange(200, 500);
}

void performConsistencyCheck()
{
    for (auto &entry : accounts)
    {
        entry.second.mtx->lock();

        Account &account = entry.second;
        int calculatedBalance = account.initialBalance;

        for (const auto &operation : account.log)
        {
            if (operation.sourceId == account.accountId)
            {
                calculatedBalance -= operation.amount;
            }
            else if (operation.destinationId == account.accountId)
            {
                calculatedBalance += operation.amount;
            }
        }

        if (calculatedBalance != account.currentBalance)
        {
            writeOperationDetailsMutex.lock();
            cerr << "Inconsistent currentBalance for Account ID: " << account.accountId << endl;
            cerr << "Actual Balance: " << account.currentBalance << ", Calculated Balance: " << calculatedBalance << endl;
            writeOperationDetailsMutex.unlock();
        }
        else
        {
            writeOperationDetailsMutex.lock();
            cout << "Consistency check passed for Account " << entry.first << endl;
            writeOperationDetailsMutex.unlock();
        }

        entry.second.mtx->unlock();
    }
}

void runRandomOperation()
{
    Operation operation;

    // random values
    int sourceId = getRandomAccount();
    int destinationId = getRandomAccount();
    while (sourceId == destinationId)
    {
        destinationId = getRandomAccount();
    }
    int amount = getRandomAmount();

    // initialise operation
    operation.sourceId = sourceId;
    operation.destinationId = destinationId;
    operation.amount = amount;

    // increment serial number of operations
    serialNumberMutex.lock();
    currentSerialNumber++;
    operation.serialNumber = currentSerialNumber;
    serialNumberMutex.unlock();

    // update account 1 balance
    accounts[operation.sourceId].mtx->lock();
    if (accounts[operation.sourceId].currentBalance - operation.amount < 0)
    {
        writeOperationDetailsMutex.lock();
        cerr << "Insufficient balance: " << accounts[operation.sourceId].currentBalance << " for account " << operation.sourceId << endl;
        cerr << "Cannot transfer: " << operation.amount << " to account " << operation.destinationId << endl;
        writeOperationDetailsMutex.unlock();
        accounts[operation.sourceId].mtx->unlock();
        return;
    }
    accounts[operation.sourceId].currentBalance -= operation.amount;
    accounts[operation.sourceId].log.push_back(operation);
    accounts[operation.sourceId].mtx->unlock();

    // update account 2 currentBalance
    accounts[operation.destinationId].mtx->lock();
    accounts[operation.destinationId].currentBalance += operation.amount;
    accounts[operation.destinationId].log.push_back(operation);
    accounts[operation.destinationId].mtx->unlock();

    // write the operation
    writeOperationDetailsMutex.lock();
    cout << "  Operation Serial: " << operation.serialNumber
         << ", Amount: " << operation.amount
         << ", Source: " << operation.sourceId
         << ", Destination: " << operation.destinationId << endl;
    writeOperationDetailsMutex.unlock();
}

void threadHandler()
{
    for (int i = 0; i < NUMBER_OF_OPERATIONS_PER_THREAD; i++)
    {
        runRandomOperation();
        this_thread::sleep_for(chrono::milliseconds(getRandomDelay()));

        serialNumberMutex.lock();
        if (currentSerialNumber % (NUMBER_OF_OPERATIONS_PER_THREAD + 1) == 0)
        {
            serialNumberMutex.unlock();
            performConsistencyCheck();
        }
        else
        {
            serialNumberMutex.unlock();
        }
    }
}

int main()
{
    readAccountsFromFile(ACCOUNTS_FILE_PATH);

    vector<thread> threads;
    for (int i = 0; i < NUMBER_OF_THREADS; i++)
    {
        threads.push_back(thread(threadHandler));
    }

    for (auto &t : threads)
    {
        t.join();
    }

    performConsistencyCheck();

    for (auto &entry : accounts)
    {
        cout << "Account ID: " << entry.first << ", Balance: " << entry.second.currentBalance << endl;
        for (const auto &operation : entry.second.log)
        {
            cout << "  Operation Serial: " << operation.serialNumber
                 << ", Amount: " << operation.amount
                 << ", Source: " << operation.sourceId
                 << ", Destination: " << operation.destinationId << endl;
        }
        delete entry.second.mtx;
    }

    return 0;
}