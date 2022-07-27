#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char** argv)
{
    int a2b, b2a, x;

    a2b = open("a2b", O_WRONLY);
    b2a = open("b2a", O_RDONLY);

    printf("x = ");
    scanf("%d", &x);
    while(x)
    {
        write(a2b, &x, sizeof(int));
        printf("x = ");
        scanf("%d", &x);
    }
    close(a2b);
    int s;
    read(b2a, &s, sizeof(int));
    close(b2a);

    printf("sum = %d\n", s);

    return 0;
}