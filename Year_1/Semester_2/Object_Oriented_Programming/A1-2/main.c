/* 9.
 *  a. Decompose a given natural number in its prime factors.
 *  b. Given a vector of numbers, find the longest contiguous subsequence such that any
 *     consecutive elements contain the same digits.
 */


#include <stdio.h>
#include <stdbool.h>


///////////////////////////// STRUCTURES
typedef struct
{
    int arr[100];
    int size;
} Vector;


///////////////////////////// UI
/*
 Function: printMenu()
 Print the available options on the screen.
 */
void printMenu()
{
    printf("\n1. Read a vector of numbers\n");
    printf("2. Decompose in prime factors a number\n");
    printf("3. Get the corresponding subsequence\n");
    printf("4. Exit\n");
    printf("5. Additional (first 8 numbers...)\n");
}


/*
 Function: readCommand()
 Read the command number.
 return: an integer
 */
int readCommand()
{
    int n;
    printf("command = ");
    scanf("%d", &n);
    return n;
}


/*
 Function: readInt()
 Read an integer.
 return: an integer
 */
int readInt()
{
    int n;
    printf("n = ");
    scanf("%d", &n);
    return n;
}


/*
 Function: printInfoVector(Vector v, int left, int right)
 Print the information about the vector (the no. of elements and the elements from a given area).
 v: Vector
 left: the left index
 right: the right index
 */
void printInfoVector(Vector v, int left, int right)
{
    int i;
    printf("size: %d\n", right - left);
    printf("elements: ");
    for(i = left; i < right; i++)
    {
        printf("%d ", v.arr[i]);
    }
    printf("\n");
}


/*
 Function: readVector()
 Read a Vector.
 return: a Vector data type
 */
Vector readVector()
{
    Vector v;
    int i;
    printf("size of the vector = ");
    scanf("%d", &v.size);
    for(i = 0; i < v.size; i++)
    {
        printf("v(%d) = ", i);
        scanf("%d", &v.arr[i]);
    }
    printInfoVector(v, 0, v.size);
    return v;
}


//////////////////////////// FUNCTIONALITIES
/*
 Function: decomposeInPrimeFactors(int n)
    n: natural number, preferable greater than 1
    Prints on the screen the prime factors and their powers resulted from the decomposition in prime factors.
 */
void decomposeInPrimeFactors(int n)
{
    // special case
    if(n <= 1)
    {
        printf("%d cannot be decomposed in prime factors", n);
        return;
    }

    int d = 2, p;
    while(n > 1)
    {
        p = 0; // current power of the current prime factor
        while(n % d == 0)
        {
            p++;
            n /= d;
        }
        if(p)
            printf("%d^%d ", d, p);
        d++;
        if(d * d > n) // in case n is prime
            d = n;
    }
}


/*
 Function: containTheSameDigits(int a, int b)
 a: integer
 b: integer
 Check if 2 numbers contain the same digits.
 return: true or false
 */
bool containTheSameDigits(int a, int b)
{
    int va[10] = {0}, vb[10] = {0}, i;

    // fill the appearance array of digits for a
    if(a == 0)
        va[0] = 1;
    while(a)
    {
        va[a % 10] = 1;
        a /= 10;
    }

    // fill the appearance array of digits for b
    if(b == 0)
        vb[0] = 1;
    while(b)
    {
        vb[b % 10] = 1;
        b /= 10;
    }

    // compare the 2 arrays to be identical, which means that the 2 numbers contain the same digits
    for(i = 0; i < 10; i++)
    {
        if(va[i] != vb[i])
            return false;
    }

    return true;
}


/*
 Function: determineSubsequence(Vector v, int* left, int* right)
 v: Vector we check
 left: the left index of the final solution
 right: the right index of the final solution
 */
void determineSubsequence(Vector v, int* left, int* right)
{
    int length = 1, maxLength = 1, i = 1, startIndex = 0;
    *left = 0, *right = 0;

    while(i < v.size)
    {
        if(containTheSameDigits(v.arr[i - 1], v.arr[i]))
            length++;
        else
        {
            length = 1;
            startIndex = i;
        }
        if(length > maxLength)
        {
            maxLength = length;
            *left = startIndex;
            *right = startIndex + length - 1;
        }
        i++;
    }
    if(length > maxLength)
    {
        maxLength = length;
        *left = startIndex;
        *right = startIndex + length - 1;
    }
}


/*
 * Compute the greatest common divisor of two numbers.
 * a, b - 2 integers
 * return: the integer representing the greatest common divisor
 */
int gcd(int a, int b)
{
    int r;
    while(b)
    {
        r = a % b;
        a = b;
        b = r;
    }
    return a;
}


/*
 * Check if a number is prime
 * n - integer number
 * return: true or false
 */
bool isPrime(int n)
{
    if(n < 2)
        return false;
    if(n % 2 == 0 && n > 2)
        return false;
    int i;
    for(i = 3; i * i <= n; i++)
        if(n % i == 0)
            return false;
    return true;
}


/*
 * 10. Determine the first 8 natural numbers (x1, x2, …, x8) greater than 2 with the following property:
 * all the natural numbers smaller than xi and that are relatively prime with xi (except for the number
 * 1) are prime, i =1,2, …, n.
 */
void solveAdditional()
{
    int i, n = 3, cnt = 0;
    bool flag;
    while(cnt < 8)
    {
        flag = true;
        for(i = 2; i < n; i++)
            if(gcd(i, n) == 1 && !isPrime(i))
            {
                flag = false;
            }
        if(flag)
        {
            printf("%d ", n);
            cnt++;
        }
        n++;
    }
    printf("\n");
}


/*
 Function: run()
 */
void run()
{
    int command, n, leftIndex, rightIndex;
    Vector v;
    while(true)
    {
        printMenu();
        command = readCommand();
        switch(command)
        {
            case 1:
                v = readVector();
                break;
            case 2:
                n = readInt();
                decomposeInPrimeFactors(n);
                break;
            case 3:
                determineSubsequence(v, &leftIndex, &rightIndex);
                printInfoVector(v, leftIndex, rightIndex + 1);
                break;
            case 4:
                return;
            case 5:
                solveAdditional();
                break;
            default:
                printf("not a valid command\n");
        }
    }
}


int main()
{
    run();

    return 0;
}