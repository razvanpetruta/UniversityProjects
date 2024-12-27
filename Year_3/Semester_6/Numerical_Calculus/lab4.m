U1 = [2,4,2 ; 0,-1,1 ; 2,1,-1];
b1 = [8 ; 0 ; -1];

backward_substitution(U1, b1);

L2 = [1,0,0 ; 1/2,1,0 ; 1/2,1,1];
b2 = [8 ; 4 ; 3];

forward_substitution(L2, b2);

A3 = [2,1,-1,-2 ; 4,4,1,3 ; -6,-1,10,10 ; -2,1,8,4];
b3 = [2 ; 4 ; -5 ; 1];
[L3, U3, P3] = lu(A3);
y3 = forward_substitution(L3, P3 * b3);
x3 = backward_substitution(U3, y3)

x4 = gauss_elimination(A3, b3)