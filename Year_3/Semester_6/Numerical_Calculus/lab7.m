%{
xi = [0, 1 / 3, 1 / 2, 1];
fi = cos(pi * xi);
xx = linspace(0, 1, 100);

d = divided_diff(xi, fi);

plot(xx, cos(pi * xx), xx, newton_int(xi, d, xx));
%}


cos(pi / 5)

xi = [0, 1 / 3, 1 / 2, 1];
fi = cos(pi * xi);
xx = 1 / 5;

d = divided_diff(xi, fi);

newton_int(xi, d, xx)



xi1 = 1000 : 10 : 1050;
fi1 = [3.0000000, 3.0043214, 3.0086002, 3.0128372, 3.0170333, 3.0211893];
d1 = divided_diff(xi1, fi1);

xx1 = 1001 : 1 : 1009;

log10(xx1)
newton_int(xi1, d1, xx1)

xi2 = linspace(-4, 4, 9);
fi2 = 2 .^ xi2;

n = length(xi2);
P = zeros(n);

x = 0.5;
P(:, 1) = fi2';

for i = 1 : n
    for j = 1 : i - 1
        P(i, j + 1) = [(x - xi2(j)) * P(i, j) - (x - xi2(i)) * P(j, j)] / (xi2(i) - xi2(j));
    end
end

P