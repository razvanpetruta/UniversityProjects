#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
    int* letters;
    int* digits;
    int* specials;
    pthread_mutex_t* mutexes;
    char* str;
} data;

void* f(void* a)
{
    data dt = *(data*) a;
    int l = 0, d = 0, s = 0;
    int i, len = strlen(dt.str);

    for(i = 0; i < len; i++)
    {
        if((dt.str[i] >= 'a' && dt.str[i] <= 'z') || (dt.str[i] >= 'A' && dt.str[i] <= 'Z'))
            l++;
        else if(dt.str[i] >= '0' && dt.str[i] <= '9')
            d++;
        else
            s++;
    }

    if(l > 0)
    {
        pthread_mutex_lock(&dt.mutexes[0]);
        *(dt.letters) += l;
        pthread_mutex_unlock(&dt.mutexes[0]);
    }

    if(d > 0)
    {
        pthread_mutex_lock(&dt.mutexes[1]);
        *(dt.digits) += d;
        pthread_mutex_unlock(&dt.mutexes[1]);
    }

    if(s > 0)
    {
        pthread_mutex_lock(&dt.mutexes[2]);
        *(dt.specials) += s;
        pthread_mutex_unlock(&dt.mutexes[2]);
    }

    return NULL;
}

int main(int argc, char** argv)
{
    if(argc < 2)
    {
        printf("Please provide at least one argument\n");
        exit(1);
    }

    int* letters = malloc(sizeof(int));
    int* digits = malloc(sizeof(int));
    int* specials = malloc(sizeof(int));
    pthread_t* threads = malloc(sizeof(pthread_t) * (argc - 1));
    pthread_mutex_t* mutexes = malloc(sizeof(pthread_mutex_t) * 3);
    data* args = malloc(sizeof(data) * (argc - 1));

    int i;
    for(i = 0; i < 3; i++)
    {
        pthread_mutex_init(&mutexes[i], NULL);
    }

    *letters = 0;
    *digits = 0;
    *specials = 0;
    for(i = 0; i < argc - 1; i++)
    {
        args[i].letters = letters;
        args[i].digits = digits;
        args[i].specials = specials;
        args[i].mutexes = mutexes;
        args[i].str = argv[i + 1];
        pthread_create(&threads[i], NULL, f, (void*) &args[i]);
    }

    for(i = 0; i < argc - 1; i++)
    {
        pthread_join(threads[i], NULL);
    }

    printf("Letters %d\nDigits %d\nSpecials %d\n", *letters, *digits, *specials);

    for(i = 0; i < 3; i++)
    {
        pthread_mutex_destroy(&mutexes[i]);
    }

    free(args);
    free(threads);
    free(mutexes);
    free(letters);
    free(digits);
    free(specials);

    return 0;
}