% Exercise 1
A = [1 0 -2;2 1 3;0 1 0];
B = [2 1 1;1 0 -1;1 1 0];
C = A - B;
D = A * B;
E = A .* B;
disp("C = ");
disp(C);
disp("D = ");
disp(D);
disp("E = ");
disp(E);

% Exercise 2
% Define the variable and the functions
x = 0:0.1:3;
f1 = x .^ 5 / 10;
f2 = x .* sin(x);
f3 = cos(x);

% Plot all 3 functions in the same axes
plot(x, f1, "green--", x, f2, "blue-o", x, f3, "red*"),
title("Plotting different functions"),
legend("x^5 / 10","x * sin(x)", "cos(x)");

% Plot all 3 functions in different pictures, but in the same window
figure
subplot(3, 1, 1);
plot(x, f1, "green--");
subplot(3, 1, 2);
plot(x, f2, "blue-o");
subplot(3, 1, 3);
plot(x, f3, "red*");
