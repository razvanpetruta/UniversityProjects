#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>

typedef struct
{
    int id;
    char* str;
} data;

void* f(void* a)
{
    data dt = *(data*) a;
    int i, len = strlen(dt.str);

    for(i = 0; i < len; i++)
    {
        if(dt.str[i] >= 'a' && dt.str[i] <= 'z')
            dt.str[i] -= 'a' - 'A';
    }

    printf("Thread %d finished, string: %s\n", dt.id, dt.str);

    return NULL;
}

int main(int argc, char** argv)
{
    if(argc < 2)
    {
        printf("Please input at least one argument\n");
        exit(1);
    }

    pthread_t* threads = malloc(sizeof(pthread_t) * (argc - 1));
    data* args = malloc(sizeof(data) * (argc - 1));
    int i;

    for(i = 0; i < argc - 1; i++)
    {
        args[i].id = i;
        args[i].str = argv[i + 1];
        pthread_create(&threads[i], NULL, f, (void*) &args[i]);
    }

    for(i = 0; i < argc - 1; i++)
    {
        pthread_join(threads[i], NULL);
    }

    free(threads);
    free(args);

    return 0;
}