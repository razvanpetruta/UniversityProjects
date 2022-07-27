#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t m;
pthread_cond_t c;

void* f(void* a)
{
    int n = *(int*) a;

    pthread_mutex_lock(&m);
    if(n == 1)
    {
        printf("Waiting for t2\n");
        pthread_cond_wait(&c, &m);
        printf("Done waiting for t2\n");
    }
    else
    {
        printf("Signal t1\n");
        pthread_cond_signal(&c);
    }

    pthread_mutex_unlock(&m);

    return NULL;
}

int main(int argc, char** argv)
{
    pthread_t t1, t2;

    pthread_mutex_init(&m, NULL);
    pthread_cond_init(&c, NULL);

    int* id = malloc(sizeof(int));
    *id = 1;

    pthread_create(&t1, NULL, f, (void*) id);
    sleep(1);
    *id = 2;
    pthread_create(&t2, NULL, f, (void*) id);

    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    pthread_mutex_destroy(&m);
    pthread_cond_destroy(&c);
    free(id);

    return 0;
}