clear all;

p = input("probability of success: ");
N = input("number of simulations: ");
n = input("number of trials: ");

for i = 1:N
    % the ith simulation
    X(i) = 0;
    for j = 1:n
        % the ith trial
        U = rand;
        X(i) = X(i) + (U < p);
    end
end

U_X = unique(X);
n_X = hist(X, length(U_X));
relative_freq = n_X / N;

X
n_X
[U_X;relative_freq]

plot(U_X, relative_freq, 'x', 0:n, binopdf(0:n, n, p), 'o');