#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <condition_variable>
#include <chrono>
#include <random>
#include <queue>

using namespace std;

const int VECTOR_SIZE = 5;
vector<int> v1 = {1, 2, 3, 4, 5};
vector<int> v2 = {5, 4, 3, 2, 1};
queue<int> partialProducts;

bool productReady = false;
bool producerDone = false;
condition_variable condVar;
mutex mtx;

int getRandomSleepDuration(int min, int max) {
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dist(min, max);
    return dist(gen);
}

void producer()
{
    for (int i = 0; i < VECTOR_SIZE; i++)
    {
        {
            unique_lock<mutex> lock(mtx);
            int result = v1[i] * v2[i];
            partialProducts.push(result);
            productReady = true;
            if (i == VECTOR_SIZE - 1)
                producerDone = true;
            cout << "Producer produced " << result << endl;
        }
        condVar.notify_one();
        int sleepDuration = getRandomSleepDuration(500, 600);
        this_thread::sleep_for(chrono::milliseconds(sleepDuration));
    }
}

void consumer()
{
    int scalarProduct = 0;
    while (!producerDone || !partialProducts.empty())
    {
        int sleepDuration = getRandomSleepDuration(400, 700);
        this_thread::sleep_for(chrono::milliseconds(sleepDuration));
        unique_lock<mutex> lock(mtx);
        condVar.wait(lock, [] { return productReady || producerDone; });
        scalarProduct += partialProducts.front();
        productReady = false;
        cout << "Consumer consumed " << partialProducts.front() << endl;
        partialProducts.pop();
    }
    cout << "Scalar product: " << scalarProduct << endl;
}

int main(int argc, char *argv[])
{
    thread producerThread(producer);
    thread consumerThread(consumer);

    producerThread.join();
    consumerThread.join();

    return 0;
}