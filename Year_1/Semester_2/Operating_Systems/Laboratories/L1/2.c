#include <stdio.h>

int main(int argc, char *argv[])
{
    printf("Hello World!\n");

    if(argc == 1)
    {
        printf("Not enough number of arguments\n");
        return 1;
    }

    printf("There are %d number of arguments\n", argc);

    int i;
    for(i = 1; i < argc; i++)
    {
        printf("%s\n", argv[i]);
    }

    return 0;
}