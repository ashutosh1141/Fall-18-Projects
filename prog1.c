#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define MAX 1000
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
void main()
{ 
    printf("Single threaded program starts\n\n");
    int i,j,k,sum=0;
    time_t t;
    /* Intializes random number generator */
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
    printf("Product of the matrices(c):\n\n");
    for (i = 0; i <MAX; i++) {
        for (j = 0; j < MAX; j++) {
            for (k = 0; k < MAX; k++) {
                sum = sum + a[i][k]*b[k][j];
            }
            c[i][j] = sum;
            printf("%d\t",sum);
            delay(10);
            sum = 0;
        }
        printf("\n");
    }printf("\n");
    printf("Single threaded program ends\n");
}
