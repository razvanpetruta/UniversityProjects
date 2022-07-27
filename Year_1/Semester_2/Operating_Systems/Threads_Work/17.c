#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct
{
    int N;
    int* index;
    int* arr;
    pthread_mutex_t* mutex;
    pthread_cond_t* cond;
} data;

void* f1(void* a)
{
    data dt = *(data*) a;
    int i;
    pthread_mutex_lock(dt.mutex);

    while(*(dt.index) % 2 == 1)
        pthread_cond_wait(dt.cond, dt.mutex);

    while(*(dt.index) < dt.N)
    {
        int nr = random() % 51 * 2;
        dt.arr[*(dt.index)] = nr;
        *(dt.index) += 1;
        printf("T1: ");
        for(i = 0; i < *(dt.index); i++)
        {
            printf("%d ", dt.arr[i]);
        }
        printf("\n");
        pthread_cond_signal(dt.cond);

        while(*(dt.index) % 2 == 1 && *(dt.index) < dt.N)
            pthread_cond_wait(dt.cond, dt.mutex);
    }

    pthread_cond_signal(dt.cond);
    pthread_mutex_unlock(dt.mutex);

    return NULL;
}

void* f2(void* a)
{
    data dt = *(data*) a;
    int i;
    pthread_mutex_lock(dt.mutex);

    while(*(dt.index) % 2 == 0)
        pthread_cond_wait(dt.cond, dt.mutex);

    while(*(dt.index) < dt.N)
    {
        int nr = random() % 50 * 2 + 1;
        dt.arr[*(dt.index)] = nr;
        *(dt.index) += 1;
        printf("T2: ");
        for(i = 0; i < *(dt.index); i++)
        {
            printf("%d ", dt.arr[i]);
        }
        printf("\n");
        pthread_cond_signal(dt.cond);

        while(*(dt.index) % 2 == 0 && *(dt.index) < dt.N)
            pthread_cond_wait(dt.cond, dt.mutex);
    }

    pthread_cond_signal(dt.cond);
    pthread_mutex_unlock(dt.mutex);

    return NULL;
}

int main(int argc, char** argv)
{
    if(argc != 2)
    {
        printf("Please provide exactly one argument\n");
        exit(1);
    }

    int N = atoi(argv[1]);
    int* arr = malloc(sizeof(int) * N);
    int index = 0;
    data args[2];
    pthread_t threads[2];
    pthread_mutex_t mutex;
    pthread_cond_t cond;

    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&cond, NULL);

    args[0].N = N;
    args[1].N = N;
    args[0].index = &index;
    args[1].index = &index;
    args[0].mutex = &mutex;
    args[1].mutex = &mutex;
    args[0].cond = &cond;
    args[1].cond = &cond;
    args[0].arr = arr;
    args[1].arr = arr;
    pthread_create(&threads[0], NULL, f1, (void*) &args[0]);
    pthread_create(&threads[1], NULL, f2, (void*) &args[1]);

    pthread_join(threads[0], NULL);
    pthread_join(threads[1], NULL);

    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&cond);

    free(arr);

    return 0;
}