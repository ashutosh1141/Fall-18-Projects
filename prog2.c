#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#define MAX_THREAD 25
#define MAX 1000

int step_i = 0;
int a[MAX][MAX],b[MAX][MAX],c[MAX][MAX];
void delay(int number_of_micro_seconds)
{
    // Converting time into milli_seconds
    int milli_seconds = 0.001 * number_of_micro_seconds;

    // Stroing start time
    clock_t start_time = clock();

    // looping till required time is not acheived
    while (clock() < start_time + milli_seconds)
        ;
}
void* multi(void* arg)
{   int i,j,k;
    int core = step_i++;

    // Each thread computes 1/25th of matrix multiplication
    for (i = core * MAX / MAX_THREAD; i < (core + 1) * MAX / MAX_THREAD; i++)
        for (j = 0; j < MAX; j++)
            for (k = 0; k < MAX; k++)
                c[i][j] += a[i][k] * b[k][j];
    delay(10);
}
void main()
{   printf("Multithreaded program starts\n\n");
    int i,j,k;
    time_t t;
    pthread_t threads[MAX_THREAD];
    printf("Generating matrix a...\n\n");
    for(i = 0; i <MAX; i++) {
        for(j = 0; j < MAX; j++) {
            a[i][j]=(rand()%2000)+1;
        }
    }
    printf("Generating matrix b...\n\n");
    for(i = 0; i <MAX; i++) {
        for(j = 0; j <MAX; j++) {
            b[i][j]=(rand()%2000)+1;
        }
    }
    for (i = 0; i < MAX_THREAD; i++) {
        int* p;
        pthread_create(&threads[i], NULL, multi, (void*)(p));
    }
    // joining and waiting for all threads to complete
    for (i = 0; i < MAX_THREAD; i++)
        pthread_join(threads[i], NULL);
    printf("Product of the matrices:\n\n");
    for (i = 0; i <MAX; i++) {
        for (j = 0; j < MAX; j++) {
            printf("%d\t",c[i][j]);
        }
        printf("\n");
    }printf("\n");
    printf("Multithreaded program ends\n");
}