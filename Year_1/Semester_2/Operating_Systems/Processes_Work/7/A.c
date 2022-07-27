#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char** argv)
{
    int a2b, b2a, n;

    mkfifo("a2b", 0600);
    mkfifo("b2a", 0600);

    a2b = open("a2b", O_WRONLY);
    b2a = open("b2a", O_RDONLY);

    srandom(getpid());

    n = random() % 10 + 1;
    printf("A: %d\n", n);
    write(a2b, &n, sizeof(int));
    while(1)
    {
        if(read(b2a, &n, sizeof(int)) <= 0)
            break;
        if(n == 10)
            break;
        n = random() % 10 + 1;
        printf("A: %d\n", n);
        write(a2b, &n, sizeof(int));
    }

    close(a2b);
    close(b2a);

    unlink("a2b");
    unlink("b2a");

    return 0;
}