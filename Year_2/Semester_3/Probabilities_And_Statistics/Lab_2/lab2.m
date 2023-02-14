%k = 0:0.01:3;
%f = binopdf(k, 3, 1 / 2);
% plot(k, f);

%x = 0:0.01:3;
%plot(x, sin(x));
%binocdf(2, 3, 1 / 2);

%x = 0:0.01:10;
%plot(x, sin(x), x, cos(x));
%or
%hold on;
%plot(x, sin(x));
%plot(x, cos(x));
%hold off;

%a and b
x = 0:0.01:3;
k = 0:3;
pdf = binopdf(k, 3, 1 / 2);
cdf = binocdf(x, 3, 1 / 2);
hold on;
plot(k, pdf);
plot(x, cdf);
legend("pdf", "cdf");
hold off;

%c
fprintf("P(X = 0) is %f\n", binopdf(0, 3, 1 / 2));
fprintf("P(X != 1) is %f\n", binopdf(0, 3, 1 / 2) + binopdf(2, 3, 1 / 2) + binopdf(3, 3, 1 / 2));

%d
fprintf("P(X <= 2) is %f\n", binocdf(2, 3, 1 / 2));
fprintf("P(X < 2) is %f\n", binocdf(1, 3, 1 / 2));

%e
fprintf("P(X >= 1) is %f\n", 1 - binocdf(0, 3, 1 / 2));
fprintf("P(X > 1) is %f\n", 1 - binocdf(1, 3, 1 / 2));

%f
p1 = randi([0, 1]);
p2 = randi([0, 1]);
p3 = randi([0, 1]);
p = (p1 + p2 + p3) / 3;
fprintf("The probability of getting 3 heads is: %f\n", p);