p = input("probability: ");

while p < 0.05 || p > 0.95
    p = input("probability: ");
end

for n = 0:2:100
    mu = n * p;
    sigma = sqrt(mu * (1 - p));
    plot(0:n, binopdf(0:n, n, p), 0:n, normpdf(0:n, mu, sigma));
    pause(0.5);
end