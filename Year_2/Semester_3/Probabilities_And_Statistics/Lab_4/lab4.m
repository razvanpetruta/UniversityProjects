% binornd(10, 0.5)
% unifrnd(1, 10, 3, 4)

clear all;

p = input("probability of success: ");
N = input("number of simulations: ");

% U = rand(1, N);

for i=1:N
   U = rand;
   X(i) = (U < p);
end

U_X = unique(X);
n_X = hist(X, length(U_X));
relative_freq = n_X / N;

X
n_X
[U_X;relative_freq]