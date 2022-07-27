#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

void func(int n)
{
    if(n > 0)
    {
        int f = fork();
        if(f == 0)
        {
            printf("PID=%d - PPID=%d\n", getpid(), getppid());
            func(n - 1);
        }
        wait(0);
    }
    exit(0);
}

int main(int argc, char** argv)
{
    func(3);

    return 0;
}