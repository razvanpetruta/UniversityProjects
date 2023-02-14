% Exercise 2
% Define the variable and the functions
x = 0:0.1:3;
f1 = x .^ 5 / 10;
f2 = x .* sin(x);
f3 = cos(x);

% Plot all 3 functions in different pictures, but in the same window
subplot(3, 1, 1);
plot(x, f1, "green--");
subplot(3, 1, 2);
plot(x, f2, "blue-o");
subplot(3, 1, 3);
plot(x, f3, "red*");