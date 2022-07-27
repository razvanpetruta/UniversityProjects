#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>

typedef struct
{
    int id;
    int M;
    pthread_mutex_t* mutexes;
    pthread_barrier_t* barrier;
} data;

void* f(void* a)
{
    data d = *(data*) a;
    int i;
    printf("Thread %d is waiting...\n", d.id);
    pthread_barrier_wait(d.barrier);

    for(i = 0; i < d.M; i++)
    {
        pthread_mutex_lock(&d.mutexes[i]);
        printf("Thread %d has entered checkpoint %d\n", d.id, i);
        int n = (random() % 101 + 100) * 1000;
        usleep(n);
        pthread_mutex_unlock(&d.mutexes[i]);
    }

    printf("Thread %d has finished\n", d.id);

    return NULL;
}

void joinThreads(pthread_t* T, int count)
{
    int i;
    for(i = 0; i < count; i++)
        pthread_join(T[i], NULL);
}

void destroyMutexes(pthread_mutex_t* mutexes, int count)
{
    int i;
    for(i = 0; i < count; i++)
        pthread_mutex_destroy(&mutexes[i]);
}

void cleanup(pthread_t* T, data* args, pthread_mutex_t* mutexes)
{
    free(T);
    free(args);
    free(mutexes);
}

int main(int argc, char** argv)
{
    if(argc != 3)
    {
        printf("Please input 2 arguments\n");
        exit(1);
    }

    int N = atoi(argv[1]);
    int M = atoi(argv[2]);
    pthread_t* T = (pthread_t*) malloc(sizeof(pthread_t) * N);
    data* args = (data*) malloc(sizeof(data) * N);
    pthread_mutex_t* mutexes = (pthread_mutex_t*) malloc(sizeof(pthread_mutex_t) * M);
    pthread_barrier_t barrier;

    pthread_barrier_init(&barrier, NULL, N);

    int i;
    for(i = 0; i < N; i++)
    {
        args[i].id = i;
        args[i].M = M;
        args[i].mutexes = mutexes;
        args[i].barrier = &barrier;
        pthread_create(&T[i], NULL, f, (void*) &args[i]);
    }

    joinThreads(T, N);
    pthread_barrier_destroy(&barrier);
    destroyMutexes(mutexes, M);
    cleanup(T, args, mutexes);

    return 0;
}