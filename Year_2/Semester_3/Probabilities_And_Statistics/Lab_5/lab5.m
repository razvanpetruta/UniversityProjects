clear all;

% The confidence level (1 - alpha) is the probability that a confidence interval will 
% contain the true population parameter. The significance level (alpha) is
% the probability of making a type I error in statistical testing.

% SIGMA KNOWN
% the data
X = [7 7 4 5 9 9 4 12 8 1 8 7 3 13 2 1 17 7 12 5 6 2 1 13 14 10 2 4 9 11 3 5 12 6 10 7];
n = length(X);
sigma = 5; % given in the problem statement
conf_level = input("confidence level: ");
alpha = 1 - conf_level;
xbar = mean(X);

% find the quantiles | N(0, 1) distribution
q1 = norminv(alpha / 2, 0, 1);
q2 = norminv(1 - alpha / 2, 0, 1);

% determine confidence interval limits
ci1 = xbar - sigma / sqrt(n) * q2;
ci2 = xbar - sigma / sqrt(n) * q1;

fprintf('confidence interval for the population mean = %3.4f, sigma known is: (%3.4f, %3.4f)\n', xbar, ci1, ci2);


% SIGMA UNKNOWN
s = std(X); % standard deviation

% find the quantiles using student distribution T(n - 1)
q3 = tinv(alpha / 2, n - 1);
q4 = tinv(1-alpha / 2, n - 1);

% determine the confidence interval limits
ci3 = xbar - s / sqrt(n) * q2;
ci4 = xbar - s / sqrt(n) * q1;

fprintf('Confidence interval for the population mean = %3.4f, case sigma unknown is: (%3.4f, %3.4f)\n', xbar, ci3, ci4);


% FOR VARIANCE AND STANDARD DEVIATION
ss = var(X); % get the variance

% get the quantiles
q5 = chi2inv(alpha / 2, n - 1);
q6 = chi2inv(1 - alpha / 2, n - 1);
 
% get the confidence interval limits
ci5 = (n - 1) * ss / q6;
ci6 = (n - 1) * ss / q5;
fprintf('Confidence interval for the population variance = %3.4f, sigma square: (%3.4f, %3.4f)\n',ss, ci5, ci6);
fprintf('Confidence interval for the population standard deviation = %3.4f, sigma square: (%3.4f, %3.4f)\n',sqrt(ss), sqrt(ci5), sqrt(ci6));