#include stdio.h
#include stdlib.h
#include string.h
#include pthread.h

typedef struct
{
    int id;
    char str[100];
} argument;

void upcase(void arg)
{
    int i;
    argument a = ((argument)arg);
    for(i = 0; i  strlen(a.str); i++)
        if(a.str[i] = 'a' && a.str[i] = 'z')
            a.str[i] -= 'a' - 'A';
    printf(Thread %d finished %sn, a.id, a.str);
    return NULL;
}

int main(int argc, char argv)
{
    int i;
    pthread_t threads = malloc((argc - 1)  sizeof(pthread_t));
    argument args = malloc((argc - 1)  sizeof(argument));

    for(i = 1; i  argc; i++)
    {
        args[i - 1].id = i - 1;
        strcpy(args[i - 1].str, argv[i]);
        pthread_create(&threads[i - 1], NULL, upcase, (void)&args[i - 1]);
    }

    for(i = 1; i  argc; i++)
    {
        pthread_join(threads[i - 1], NULL);
    }

    free(threads);
    free(args);
    return 0;
}