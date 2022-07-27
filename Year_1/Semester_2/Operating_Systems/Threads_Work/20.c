#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

typedef struct
{
    int id;
    int M;
    pthread_mutex_t* mutexes;
    pthread_barrier_t* barrier;
} data;

void* f(void* a)
{
    data dt = *(data*) a;
    int i;
    printf("Thread %d is waiting...\n", dt.id);
    pthread_barrier_wait(dt.barrier);
    for(i = 0; i < dt.M; i++)
    {
        pthread_mutex_lock(&dt.mutexes[i]);
        printf("Thread %d has entered the checkpoint %d\n", dt.id, i);
        int n = (random() % 101 + 100) * 1000;
        usleep(n);
        pthread_mutex_unlock(&dt.mutexes[i]);
    }
    printf("Thread %d has finished\n", dt.id);

    return NULL;
}

int main(int argc, char** argv)
{
    if(argc != 3)
    {
        printf("Please provide exactly 2 arguments\n");
        exit(1);
    }

    int i;
    int N = atoi(argv[1]);
    int M = atoi(argv[2]);
    pthread_t* threads = malloc(sizeof(pthread_t) * N);
    pthread_mutex_t* mutexes = malloc(sizeof(pthread_mutex_t) * M);
    data* args = malloc(sizeof(data) * N);
    pthread_barrier_t barrier;

    pthread_barrier_init(&barrier, NULL, N);

    for(i = 0; i < M; i++)
    {
        pthread_mutex_init(&mutexes[i], NULL);
    }

    for(i = 0; i < N; i++)
    {
        args[i].id = i;
        args[i].M = M;
        args[i].mutexes = mutexes;
        args[i].barrier = &barrier;
        pthread_create(&threads[i], NULL, f, (void*) &args[i]);
    }

    for(i = 0; i < N; i++)
    {
        pthread_join(threads[i], NULL);
    }

    for(i = 0; i < M; i++)
    {
        pthread_mutex_destroy(&mutexes[i]);
    }

    pthread_barrier_destroy(&barrier);
    free(threads);
    free(mutexes);
    free(args);

    return 0;
}