#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    FILE *in;
    int *a;

    a = (int*)malloc(2 * sizeof(int));

    in = fopen(argv[1], "r");

    // read the size of the matrix
    fscanf(in, "%d", &a[0]);
    fscanf(in, "%d", &a[1]);

    printf("lines: %d, columns: %d\n", a[0], a[1]);

    int **mat;
    mat = (int**) malloc(a[0] * sizeof(int*));
    int i, j;
    for(i = 0; i < a[0]; i++)
    {
        mat[i] = (int*) malloc(a[1] * sizeof(int));
    }

    for(i = 0; i < a[0]; i++)
    {
        for(j = 0; j < a[1]; j++)
        {
            fscanf(in, "%d", &mat[i][j]);
        }
    }

    for(i = 0; i < a[0]; i++)
    {
        for(j = 0; j < a[1]; j++)
        {
            printf("%d ", mat[i][j]);
        }
        printf("\n");
    }

    for(i = 0; i < a[0]; i++)
    {
        free(mat[i]);
    }
    free(mat);

    free(a);
    fclose(in);

    return 0;
}