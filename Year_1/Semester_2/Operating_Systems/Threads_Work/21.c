#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include <stdlib.h>

typedef struct
{
    int id;
    int N;
    pthread_barrier_t* barrier;
    sem_t* sems;
} data;

void* f(void* a)
{
    data dt = *(data*) a;
    printf("Thread %d is waiting...\n", dt.id);
    pthread_barrier_wait(dt.barrier);
    int i;
    for(i = 0; i < dt.N; i++)
    {
        sem_wait(&dt.sems[i]);
        printf("Thread %d has entered the checkpoint %d\n", dt.id, i);
        usleep(100000);
        sem_post(&dt.sems[i]);
    }
    printf("Thread %d has finished\n", dt.id);

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
    int M = 1;
    int i, step = 2;
    pthread_barrier_t barrier;

    for(i = 0; i < N; i++)
    {
        M *= 2;
    }

    sem_t* sems = malloc(sizeof(sem_t) * N);
    for(i = 0; i < N; i++)
    {
        sem_init(&sems[i], 0, M / step);
        step *= 2;
    }
    pthread_barrier_init(&barrier, NULL, M);

    pthread_t* threads = malloc(sizeof(pthread_t) * M);
    data* args = malloc(sizeof(data) * M);
    for(i = 0; i < M; i++)
    {
        args[i].id = i;
        args[i].N = N;
        args[i].barrier = &barrier;
        args[i].sems = sems;
        pthread_create(&threads[i], NULL, f, (void*) &args[i]);
    }

    for(i = 0; i < M; i++)
    {
        pthread_join(threads[i], NULL);
    }

    pthread_barrier_destroy(&barrier);

    for(i = 0; i < N; i++)
    {
        sem_destroy(&sems[i]);
    }

    free(sems);
    free(threads);
    free(args);

    return 0;
}