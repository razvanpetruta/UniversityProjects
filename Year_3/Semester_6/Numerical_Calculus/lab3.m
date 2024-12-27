%{
x = [0, 1, 2];
f = 1 ./ (1 + x);
df = -1 ./ (1 + x) .^ 2;

divided_diff(x, f);
[z, t] = divided_diff2(x, f, df);
z;
t;

x_ = linspace(1, 2, 11);
f_ = 1 ./ (1 + x_);
df_ = -1 ./ (1 + x_) .^ 2;
divided_diff(x_, f_)
[z_, t_] = divided_diff2(x_, f_, df_);
z_
t_
%}

x1 = [-2, -1, 0, 1, 2, 3, 4];
f1 = [-5, 1, 1, 1, 7, 25, 60];
divided_diff(x1, f1)