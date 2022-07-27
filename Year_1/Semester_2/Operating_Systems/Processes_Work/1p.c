#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv)
{
    int i, n = 3;

    for(i = 0; i < n; i++)
    {
        int f = fork();
        if(f == -1)
        {
            perror("Error on fork");
        }
        else
            if(f == 0)
            {
                printf("Child pid: %d Parent pid: %d\n", getpid(), getppid());
                exit(0);
            }
            else
            {
                printf("Child pid: %d Parent pid: %d\n", f, getpid());
            }
    }

    for(i = 0; i < n; i++)
        wait(0);

    return 0;
}