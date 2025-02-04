
conflevel = input('confidence level [0; 1] = '); % 1 - alpha
alpha = 1 - conflevel;

% the data
X1 = [22.4 21.7 24.5 23.4 21.6 23.3 22.4 21.6 24.8 20.0];
X2 = [17.7 14.8 19.6 19.6 12.1 14.8 15.4 12.6 14.0 12.2];

% the means
m1 = mean(X1);
m2 = mean(X2);
m = m1 - m2;

% the lengths of the vectors
n1 = length(X1);
n2 = length(X2);

% the variances
v1 = var(X1);
v2 = var(X2);

% a) sigma1 == sigma2 | Student distribution T(n1 + n2 - 2)
sp = sqrt(((n1 - 1) * v1 + (n2 - 1) * v2) / (n1 + n2 - 2));

% get the quantiles using the student distribution
t1 = tinv(1 - alpha / 2, n1 + n2 - 2);
t2 = tinv(alpha / 2, n1 + n2 - 2); 

% get the confidence interval limits
ci1 = m - t1 * sp * sqrt(1 / n1 + 1 / n2);
ci2 = m - t2 * sp * sqrt(1 / n1 + 1 / n2);
fprintf('C.I. for the difference of means = %3.4f (sigma1 == sigma2): (%3.4f, %3.4f)\n', m, ci1, ci2);

% b) sigma1 != sigma2
c = (v1 / n1) / (v1 / n1 + v2 / n2);
n = 1 / (c ^ 2 / (n1 - 1) + (1 - c) ^ 2 / (n2 - 1));

t1 = tinv(1 - alpha / 2, n);
t2 = tinv(alpha / 2, n);

ci1 = m - t1 * sqrt(v1 / n1 + v2 / n2);
ci2 = m - t2 * sqrt(v1 / n1 + v2 / n2);

fprintf('C.I. for the difference of means = %3.4f (sigma1 != sigma2): (%3.4f, %3.4f)\n', m, ci1, ci2);

% c) 
f1 = finv(1 - alpha / 2, n1 - 1, n2 - 1);
f2 = finv(alpha / 2, n1 - 1, n2 - 1);

ci1 = 1 / f1 * v1 / v2;
ci2 = 1 / f2 * v1 / v2;

fprintf('C.I. for the ratio of variances = %3.4f: (%3.4f, %3.4f)\n', v1 / v2, ci1, ci2);
fprintf('C.I. for the ratio of standard deviations = %3.4f: (%3.4f, %3.4f)\n',sqrt(v1) / sqrt(v2), sqrt(ci1), sqrt(ci2));