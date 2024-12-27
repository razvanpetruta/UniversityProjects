%{
f = @(x) 1 ./ (2 + sin(x));
[I, nf] = romberg(f, 0, pi / 2, 10 ^ (-6), 50)
%}

[I, n, c] = gauss_quad(@(x) x + 1 - x, 2, 3)
2 * I