%{
[A, b] = generate_matrices(5);
A
b
%}

%{
n = 500;
[A, b] = generate_matrices(500);
x0 = zeros(size(b));
maxint = 100;
err = 10 ^ (-5);
%[x, int] = jacobi_iter(A, b, x0, maxint, err);
[x, int] = gauss_seidel_iter(A, b, x0, maxint, err);
x
int
%}

A = [10, 7, 8, 7 ; 7, 5, 6, 5; 8, 6, 10, 9 ; 7, 5, 9, 10];
b = [32 ; 23 ; 33 ; 31];

x1 = A \ b

bTilda = [32.1 ; 22.9 ; 33.1 ; 30.9];

x2 = A \ bTilda

norm(x1 - x2) / norm(x1)
norm(b - bTilda) / norm(b)

ATilda = [10 7 8.1 7.2 ; 7.8 5.04 6 5 ; 8 5.98 9.89 9 ; 6.99 4.99 9 9.98];
x3 = ATilda \ b

norm(x1 - x3) / norm(x1) 
norm(A - ATilda) / norm(A)
display("-------")

norm(A) * norm(inv(A))
norm(inv(A)) * norm(A * x1) / norm(x1)




