#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int hasSameDigits(int x)
{
    int last = x % 10;
    x /= 10;

    while(x)
    {
        if(x % 10 != last)
            return 0;
        x /= 10;
    }

    return 1;
}

int main(int argc, char** argv)
{
    int a2b, b2a, s = 0, x;

    a2b = open("a2b", O_RDONLY);
    b2a = open("b2a", O_WRONLY);

    while(1)
    {
        if(read(a2b, &x, sizeof(int)) <= 0)
            break;
        if(hasSameDigits(x) == 1)
            s += x;
    }
    write(b2a, &s, sizeof(int));
    close(a2b);
    close(b2a);

    return 0;
}