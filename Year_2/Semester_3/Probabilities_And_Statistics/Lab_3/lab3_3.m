n = input("n: ");
while n < 30
    n = input("n: ");
end

p = input("p: ");
while p > 0.05
    p = input("p: ");
end

plot(0:n, binopdf(0:n, n, p), 0:n, poisspdf(0:n, n * p));