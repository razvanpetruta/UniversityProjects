#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

pthread_rwlock_t rw;

void* write1(void* arg)
{
    char* str;
    FILE* f;

    pthread_rwlock_wrlock(&rw);
    printf("File locked, please enter the message\n");
    str = malloc(sizeof(char) * 20);
    scanf("%s", str);
    f = fopen("file.txt", "w");
    fprintf(f, "%s", str);
    fclose(f);
    free(str);
    pthread_rwlock_unlock(&rw);
    printf("Unlocked the file, you can go on\n");

    return NULL;
}

void* write2(void* arg)
{
    char* str;
    FILE* f;

    sleep(3);
    pthread_rwlock_wrlock(&rw);
    printf("File locked, please enter the message\n");
    str = malloc(sizeof(char) * 20);
    scanf("%s", str);
    f = fopen("file.txt", "w");
    fprintf(f, "%s", str);
    fclose(f);
    free(str);
    pthread_rwlock_unlock(&rw);
    printf("File unlocked, you can go on\n");

    return NULL;
}

void* read1(void* arg)
{
    char* str;
    FILE* f;

    sleep(4);
    pthread_rwlock_rdlock(&rw);
    printf("Reading from the file\n");
    str = malloc(sizeof(char) * 10);
    f = fopen("file.txt", "r");
    fscanf(f, "%s", str);
    printf("The message is: %s\n", str);
    fclose(f);
    free(str);
    pthread_rwlock_unlock(&rw);

    return NULL;
}

void* read2(void* arg)
{
    char* str;
    FILE* f;

    sleep(4);
    pthread_rwlock_rdlock(&rw);
    printf("Reading from the file\n");
    str = malloc(20);
    f = fopen("file.txt", "r");
    fscanf(f, "%s", str);
    printf("The message is %s", str);
    fclose(f);
    free(str);
    pthread_rwlock_unlock(&rw);

    return NULL;
}

int main(int argc, char** argv)
{
    pthread_t t1, t2, t3, t4;

    pthread_rwlock_init(&rw, NULL);

    pthread_create(&t1, NULL, write1, NULL);
    pthread_create(&t2, NULL, write2, NULL);
    pthread_create(&t3, NULL, read1, NULL);
    pthread_create(&t4, NULL, read2, NULL);

    printf("Threads were created\n");

    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    pthread_join(t3, NULL);
    pthread_join(t4, NULL);

    pthread_rwlock_destroy(&rw);

    return 0;
}