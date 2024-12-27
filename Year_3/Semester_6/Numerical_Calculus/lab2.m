syms x;
%f = exp(x);
%T1 = taylor(f, x, 0, 'Order', 2);
%T2 = taylor(f, x, 0, 'Order', 3);
%T3 = taylor(f, x, 0, 'Order', 4);
%T4 = taylor(f, x, 0, 'Order', 5);

%clf;
%ezplot(f, [-3, 3]);
%hold on;
%ezplot(T1, [-3, 3]);
%ezplot(T2, [-3, 3]);
%ezplot(T3, [-3, 3]);
%ezplot(T4, [-3, 3]);

%vpa(exp(1), 7)

%subs(T1, x, 1)
%vpa(subs(T4, x, 1), 7)

%for k = 6:1:10
%    T = taylor(f, x, 0, 'Order', k);
%    k
%    vpa(subs(T, x, 1), 7)
%end

%{
f = sin(x);
ts3 = taylor(f, x, 0, 'Order', 4);
ts5 = taylor(f, x, 0, 'Order', 6);
%}

%{
clf;
ezplot(f, [-pi, pi])
hold on;
ezplot(ts3, [-pi, pi])
ezplot(ts5, [-pi, pi])
%}

%{
sin(pi/5)
vpa(sin(pi/5), 6)


for k = 2:1:10
    t = taylor(f, x, 0, 'Order', k);
    k
    vpa(subs(t, x, pi/5), 6)
end
%}

%{
sin(10*pi/3)
vpa(sin(10*pi/3), 6)

sin(-pi/3)
vpa(sin(-pi/3), 6)

for k = 1:1:10
    t = taylor(f, x, 0, 'Order', k);
    k
    vpa(subs(t, x, -pi/3), 6)
end
%}

%{
f = log(1 + x);
ts2 = taylor(f, x, 0, 'Order', 3);
ts5 = taylor(f, x, 0, 'Order', 6);
%}

%{
clf;
ezplot(f, [-0.9, 1])
hold on;
ezplot(ts2, [-0.9, 1])
ezplot(ts5, [-0.9, 1])
%}

%{
vpa(log(2), 6)

for k = 1:1:10
    t = taylor(f, x, 0, 'Order', k);
    k
    vpa(subs(t, x, 2), 6)
end
%}


vpa(log(2), 6)

for k = 1:1:15
    t1 = taylor(log(1 + x), x, 0, 'Order', k);
    t2 = taylor(log(1 - x), x, 0, 'Order', k);
    t3 = t1 - t2;
    k
    vpa(subs(t3, x, 1/3), 6)
end







