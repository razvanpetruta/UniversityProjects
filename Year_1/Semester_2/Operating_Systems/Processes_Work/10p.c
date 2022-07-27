#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv)
{
    int p2c[2], c2p[2], n;

    pipe(p2c); pipe(c2p);

    if(fork() == 0) // CHILD
    {
        close(c2p[0]); close(p2c[1]);

        while(1)
        {
            if(read(p2c[0], &n, sizeof(int)) <= 0)
                break;
            if(n < 5)
                break;
            n /= 2;
            printf("B: %d\n", n);
            write(c2p[1], &n, sizeof(int));
        }

        close(c2p[1]); close(p2c[0]);
        exit(0);
    }

    // PARENT
    close(c2p[1]); close(p2c[0]);

    srandom(getpid());
    n = random() % 150 + 51;
    if(n % 2 == 1)
        n++;
    printf("A: %d\n", n);
    write(p2c[1], &n, sizeof(int));

    while(1)
    {
        if(read(c2p[0], &n, sizeof(int)) <= 0)
            break;
        if(n < 5)
            break;
        if(n % 2 == 1)
            n++;
        printf("A: %d\n", n);
        write(p2c[1], &n, sizeof(int));
    }

    close(p2c[1]); close(c2p[0]);

    wait(0);

    return 0;
}