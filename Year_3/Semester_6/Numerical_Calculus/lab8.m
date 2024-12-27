%{
xi = [0, 1, 2];
fi = 1 ./ (1 + xi);

d = divided_diff(xi, fi);

xx = linspace(0, 2);

dfi = -1 ./ (1 + xi) .^ 2;

[zi, d2] = divided_diff2(xi, fi, dfi);

plot(xx, 1 ./ (1 + xx), xx, newton_int(xi, d, xx), xx, newton_int(zi, d2, xx));
%}

%{
xi = time
fi = distance
dfi = speed

t = 10
%}

xi = [0 3 5 8 13];
fi = [0 225 383 623 993];
dfi = [0 77 80 74 72];

[z, d] = divided_diff2(xi, fi, dfi);
newton_int(z, d, 10) 

d1 = divided_diff(xi, dfi);
newton_int(xi, d1, 10) 

d2 = divided_diff(fi, dfi);
newton_int(fi, d2, 728.91) 