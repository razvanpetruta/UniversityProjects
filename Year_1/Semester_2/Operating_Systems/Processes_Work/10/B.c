#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

int main(int argc, char** argv)
{
    int a2b, b2a, n;

    a2b = open("a2b", O_RDONLY);
    b2a = open("b2a", O_WRONLY);

    while(1)
    {
        if(read(a2b, &n, sizeof(int)) <= 0)
            break;
        if(n < 5)
            break;
        n /= 2;
        printf("B: %d\n", n);
        write(b2a, &n, sizeof(int));
    }

    close(a2b);
    close(b2a);

    return 0;
}