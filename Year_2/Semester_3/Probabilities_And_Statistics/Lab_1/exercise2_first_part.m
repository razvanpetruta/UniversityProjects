% Exercise 2
% Define the variable and the functions
x = 0:0.1:3; % start:step:end
f1 = x .^ 5 / 10; % because it is a vector, and for every element we need
                  % to apply the power
f2 = x .* sin(x); % the same, we need the multiplication of elements
f3 = cos(x);

% Plot all 3 functions in the same axes
plot(x, f1, "green--", x, f2, "blue-o", x, f3, "red*"),
title("Plotting different functions"),
legend("x^5 / 10","x * sin(x)", "cos(x)");