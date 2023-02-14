clear all;

p = input("probability of succes: ");
N = input("number of simulations: ");
n = input("number of successes: ");

for i = 1:N
    X(i) = 0;
    cnt = 0;
    while cnt < n
        U = rand;
        if U < p
            X(i) = X(i) + 1;
        else
            cnt = cnt + 1;
        end
    end
end

U_X = unique(X);
n_X = hist(X, length(U_X));
relative_freq = n_X / N;

[U_X;relative_freq]

plot(U_X, relative_freq, 'x', 0:20, nbinpdf(0:20, n, p), 'o');
