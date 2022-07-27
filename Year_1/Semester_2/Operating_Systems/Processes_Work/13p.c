#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>

int main(int arg, char** argv)
{
    int a2b[2], b2c[2], c2a[2];

    pipe(a2b); pipe(b2c); pipe(c2a);

    if(fork() == 0) // B
    {
        close(a2b[1]); close(b2c[0]); close(c2a[0]); close(c2a[1]);

        int i, x, n;
        srandom(getpid());

        read(a2b[0], &n, sizeof(int));
        write(b2c[1], &n, sizeof(int));
        for(i = 0; i < n; i++)
        {
            if(read(a2b[0], &x, sizeof(int)) <= 0)
                break;
            int r = random() % 4 + 2;
            x += r;
            printf("B: received %d, wrote %d\n", x - r, x);
            write(b2c[1], &x, sizeof(int));
        }
        close(a2b[0]); close(b2c[1]);
        exit(0);
    }

    if(fork() == 0) // C
    {
        close(a2b[0]); close(a2b[1]); close(b2c[1]); close(c2a[0]);

        int s = 0, x, n, i;
        read(b2c[0], &n, sizeof(int));
        for(i = 0; i < n; i++)
        {
            if(read(b2c[0], &x, sizeof(int)) <= 0)
                break;
            printf("C: received %d, new sum: %d\n", x, s + x);
            s += x;
        }

        write(c2a[1], &s, sizeof(int));
        close(b2c[0]); close(c2a[1]);
        exit(0);
    }

    // A
    close(a2b[0]); close(b2c[0]); close(b2c[1]); close(c2a[1]);

    int n, i;
    printf("n = ");
    scanf("%d", &n);
    int* arr = (int*) malloc(n * sizeof(int));
    // read the elements
    for(i = 0; i < n; i++)
    {
        printf("arr[%d] = ", i);
        scanf("%d", &arr[i]);
    }
    write(a2b[1], &n, sizeof(int));

    // write the elements
    for(i = 0; i < n; i++)
    {
        printf("A: writes %d\n", arr[i]);
        write(a2b[1], &arr[i], sizeof(int));
    }

    free(arr);

    int res;
    read(c2a[0], &res, sizeof(int));
    printf("A: the sum is: %d\n", res);

    close(a2b[1]); close(c2a[0]);

    wait(0); wait(0);

    return 0;
}