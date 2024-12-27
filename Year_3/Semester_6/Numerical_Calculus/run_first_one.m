%{
x = [0, 1/6, 1/2];
f = [0, 1/2, 1];

divided_diff(x, f)
%}

%{
x = [-1, 1];
f = [-3, 1];
df = [10, 2];

[z, t] = divided_diff2(x, f, df);
z
t
%}

x1 = [-2, -1, 0, 1, 2, 3, 4];
f1 = [-5, 1, 1, 1, 7, 25, 60];
forward_diff(f1)
backward_diff(f1)