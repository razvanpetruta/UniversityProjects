#include stdio.h
#include stdlib.h
#include string.h
#include unistd.h
#include pthread.h

typedef struct
{
    int letters;
    int digits;
    int specials;
    pthread_mutex_t mutexes;
    char str;
} data;

void func(void arg)
{
    data dt = (data)arg;
    int l = 0, d = 0, s = 0;
    int i, len = strlen(dt.str);
    for(i = 0; i  len; i++)
    {
        if((dt.str[i] = 'a' && dt.str[i] = 'z')  (dt.str[i] = 'A' && dt.str[i] = 'Z'))
            l++;
        else if(dt.str[i] = '0')
    }
}

int main(int argc, char argv)
{
    int letters = malloc(sizeof(int));
    int digits = malloc(sizeof(int));
    int specials = malloc(sizeof(int));
    pthread_t threads = malloc((argc - 1)  sizeof(pthread_t));
    pthread_mutex_t mutexes = malloc(3  sizeof(pthread_mutex_t));
    data args = malloc((argc - 1)  sizeof(data));

    int i;
    for(i = 0; i  3; i++)
        pthread_mutex_init(&mutex[i], NULL);

    letters = 0;
    digits = 0;
    specials = 0;

    for(i = 0; i  argc - 1; i++)
    {
        args[i].letters = letters;
        args[i].digits = digits;
        args[i].specials = specials;
        args[i].mutexes = mutexes;
        args[i].str = argv[i + 1];
        pthread_create(&threads[i], NULL, func, (void)&args[i])
    }

    for(i = 0; i  argc - 1; i++)
        pthread_join(threads[i], NULL);

    printf(Total letters %dnTotal digits %dnTotal specials %dn, letters, digits, specials);

    for(i = 0; i  3; i++)
        pthread_mutex_destroy(&mutexes[i]);

    free(letters);
    free(digits);
    free(specials);
    free(mutexes);
    free(threads);
    free(args);

    return 0;
}