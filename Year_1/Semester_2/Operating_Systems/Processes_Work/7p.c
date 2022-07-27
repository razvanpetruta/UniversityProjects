#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv)
{
    int a2b[2], b2a[2], n;

    pipe(a2b); pipe(b2a);

    if(fork() == 0) // A
    {
        close(a2b[0]); close(b2a[1]);

        srandom(getpid());

        n = random() % 10 + 1;
        printf("A: %d\n", n);
        write(a2b[1], &n, sizeof(int));

        while(1)
        {
            if(read(b2a[0], &n, sizeof(int)) <= 0)
                break;
            if(n == 10)
                break;
            n = random() % 10 + 1;
            printf("A: %d\n", n);
            write(a2b[1], &n, sizeof(int));
        }

        close(a2b[1]); close(b2a[0]);
        exit(0);
    }

    if(fork() == 0) // B
    {
        close(a2b[1]); close(b2a[0]);

        srandom(getpid());

        while(1)
        {
            if(read(a2b[0], &n, sizeof(int)) <= 0)
                break;
            if(n == 10)
                break;
            n = random() % 10 + 1;
            printf("B: %d\n", n);
            write(b2a[1], &n, sizeof(int));
        }

        close(a2b[0]); close(b2a[1]);
        exit(0);
    }

    close(a2b[0]); close(a2b[1]);
    close(b2a[0]); close(b2a[1]);

    wait(0); wait(0);

    return 0;
}