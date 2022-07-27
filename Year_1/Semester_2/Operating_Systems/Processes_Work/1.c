// Se se implementeze doua procese A si B. Procesul A citeste de la tastatura numere < 1000,
//  pana la intalnirea valorii 0 si i le transmite procesului B. B verifica care dintre 
// acele numere este scris cu o singura cifra (ex: 7, 77, 777), calculeaza suma acelor 
// numere si o transmite procesului A. A afiseaza suma calculata de B. Codul C trebuie 
// sa compileze fara erori sau warning-uri si sa fie indentat.

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <sys/types.h>

int hasSameDigits(int x)
{
    int last = x % 10;
    x /= 10;

    while(x)
    {
        if(x % 10 != last)
            return 0;
        x /= 10;
    }

    return 1;
}

int main(int argc, char** argv)
{
    int a2b[2], b2a[2], x, s = 0;

    pipe(a2b); pipe(b2a);

    if(fork() == 0) // B
    {
        close(a2b[1]); close(b2a[0]);

        while(1)
        {
            if(read(a2b[0], &x, sizeof(int)) <= 0)
                break;
            if(hasSameDigits(x))
                s += x;
        }
        write(b2a[1], &s, sizeof(int));
        close(a2b[0]); close(b2a[1]);
        exit(0);
    }

    // A
    close(a2b[0]); close(b2a[1]);

    printf("x = ");
    scanf("%d", &x);

    while(x)
    {
        write(a2b[1], &x, sizeof(int));
        printf("x = ");
        scanf("%d", &x);
    }
    close(a2b[1]);

    read(b2a[0], &s, sizeof(int));
    close(b2a[0]);
    wait(0);

    printf("sum = %d\n", s);

    return 0;
}