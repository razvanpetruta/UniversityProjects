#include "common.h"

int main(int argc, char** argv)
{
    int a2b, b2a, i, size = 0;
    char* res;

    a2b = open("a2b", O_WRONLY);
    b2a = open("b2a", O_RDONLY);

    for(i = 1; i < argc; i++)
    {
        size += strlen(argv[i]) + 1;
    }
    size++;

    res = (char*) malloc(size * sizeof(char));
    memset(res, 0, size * sizeof(char));

    for(i = 1; i < argc; i++)
    {
        writeToFD(a2b, argv[i]);
        int len;
        read(b2a, &len, sizeof(int));
        char* buf = (char*) malloc((len + 1) * sizeof(char));
        readFromFD(b2a, len, buf);
        buf[len] = 0;
        strcat(res, buf);
        strcat(res, " ");
        free(buf);
    }

    // send a stop
    // int stop = -1;
    // write(a2b, &stop, sizeof(int));

    res[size - 1] = 0;
    printf("%s\n", res);

    close(a2b);
    close(b2a);

    return 0;
}