/*
    Read a string of signed numbers in base 10 from keyboard. Determine the minimum value of the string and write it in the file min.txt (it will be created) in 16 base.
*/

#include <stdio.h>

int my_min(int, int);

int main()
{
    int n, v[100], i, minim;
    FILE * fp;
    
    fp = fopen("min.txt", "w");
    
    // read the number of elements
    printf("n = ");
    scanf("%d", &n);
    
    // read the elements
    for(i = 0; i < n; i++)
    {
        printf("v[%d] = ", i);
        scanf("%d", &v[i]);
    }
        
    minim = v[0];
    for(i = 1; i < n; i++)
        minim = my_min(minim, v[i]);
    
    fprintf(fp, "%x", minim);
    fclose(fp);
    
    return 0;
}