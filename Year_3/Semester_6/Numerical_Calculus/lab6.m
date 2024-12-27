%{
xi = [1 2 3];
fi = [1 4 9];
x = [4 5];
f = lagrange_int(xi, fi, x);
f
%}

%{
xi = linspace(-2, 4, 10);
fi = (xi + 1) ./ (3 * xi .^ 2 + 2 * xi + 1);

x = linspace(-2, 4, 500);
f = (x + 1) ./ (3 * x .^ 2 + 2 * x + 1);

L9f = lagrange_int(xi, fi, x);

plot(x, f, "b", x, L9f, "r");
legend("Original function", "Lagrange Interpolant");

plot(x, abs(f - L9f), "r");
max(abs(f - L9f))


x = 1 / 2;
f = (x + 1) ./ (3 * x .^ 2 + 2 * x + 1);

L9f = lagrange_int(xi, fi, x);

max(abs(f - L9f))
%}

%{
xi = [1980 1990 2000 2010 2020];
fi = [4451 5287 6090 6970 7821];
x = [2005 2015 2024];
f = lagrange_bary(xi, fi, x);
f
%}

xi = [100 121 144];
fi = [10 11 12];
x = [118];
f = lagrange_bary(xi, fi, x);
f

