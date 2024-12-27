%{
f = @(x) 1./ x;
comp_rectangle(f, 1, 2, 18)
log(2)
comp_trapezoid(f, 1, 2, 16)
simpson(f, 1, 2, 10)
%}


f = @(x) sqrt(1 + (pi * cos(pi * x)) .^ 2);
adquad(f, 0, 1, 10 ^ (-6), @comp_rectangle, 4)
integral(f, 0, 1)
