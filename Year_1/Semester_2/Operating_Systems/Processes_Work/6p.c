#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <time.h>

int main(int argc, char** argv)
{
    if(argc != 2)
    {
        printf("Please provide exactly one argument\n");
        exit(1);
    }

    int p2c[2], c2p[2], n, x, i;

    pipe(p2c); pipe(c2p);

    if(fork() == 0) // C
    {
        close(p2c[1]); close(c2p[0]);

        if(read(p2c[0], &n, sizeof(int)) <= 0)
        {
            perror("Error at reading n from parent\n");
            close(p2c[0]); close(c2p[1]);
            exit(1);
        }

        int s = 0;
        for(i = 0; i < n; i++)
        {
            if(read(p2c[0], &x, sizeof(int)) <= 0)
            {
                perror("Error at reading random number from parent\n");
                close(p2c[0]); close(c2p[1]);
                exit(1);
            }
            s += x;
        }

        float avg = s * 1.0 / n;

        if(write(c2p[1], &avg, sizeof(float)) <= 0)
        {
            perror("Error at writing the result to the parent\n");
            close(p2c[0]); close(c2p[1]);
            exit(1);
        }

        close(p2c[0]); close(c2p[1]);
        exit(0);
    }

    // P
    close(p2c[0]); close(c2p[1]);

    n = atoi(argv[1]);
    srandom(time(0));

    if(write(p2c[1], &n, sizeof(int)) <= 0)
    {
        perror("Error at writing n to the child\n");
        close(p2c[1]); close(c2p[0]);
        wait(0);
        exit(1);
    }

    for(i = 0; i < n; i++)
    {
        x = random() % 100;
        if(write(p2c[1], &x, sizeof(int)) <= 0)
        {
            perror("Error at writing the random number to the child\n");
            close(p2c[1]); close(c2p[0]);
            wait(0);
            exit(1);
        }
        printf("Parent generated: %d\n", x);
    }

    float rez;
    if(read(c2p[0], &rez, sizeof(float)) <= 0)
    {
        perror("Error at reading the final result\n");
        close(p2c[1]); close(c2p[0]);
        wait(0);
        exit(1);
    }

    printf("Average is: %f\n", rez);
    close(p2c[1]); close(c2p[0]);

    wait(0);

    return 0;
}