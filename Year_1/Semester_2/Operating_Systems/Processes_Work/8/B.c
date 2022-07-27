#include "common.h"

int main(int argc, char** argv)
{
    int a2b, b2a;

    a2b = open("a2b", O_RDONLY);
    b2a = open("b2a", O_WRONLY);

    while(1)
    {
        int len;
        if(read(a2b, &len, sizeof(int)) <= 0)
            break;
        char* buf = (char*) malloc((len + 1) * sizeof(char));
        readFromFD(a2b, len * sizeof(char), buf);
        buf[len] = 0;
        int i;
        for(i = 0; i < strlen(buf); i++)
        {
            if(buf[i] >= 'a' && buf[i] <= 'z')
                buf[i] -= 'a' - 'A';
        }
        writeToFD(b2a, buf);
        free(buf);
    }

    close(a2b);
    close(b2a);

    return 0;
}