%{
Nickel powders are used in coatings used to shield electronic equipment. A random sample
is selected, and the sizes of nickel particles in each coating are recorded (they are assumed
to be approximately normally distributed):
3.26, 1.89, 2.42, 2.03, 3.07, 2.95, 1.39, 3.06, 2.46, 3.35, 1.56, 1.79, 1.76, 3.82, 2.42, 2.96
a. At the 5% significance level, on average, do these nickel particles seem to be smaller
than 3?
b. Find a 99% confidence interval for the standard deviation of the size of nickel particles.
%}

x = [3.26 1.89 2.42 2.03 3.07 2.95 1.39 3.06 2.46 3.35 1.56 1.79 1.76 3.82 2.42 2.96];
n = length(x);
alpha = input("significance level: ");

% a)
fprintf("we have a left tailed test, sigma unknown\n");
fprintf("null hypothesis: mu = 3\n");
fprintf("alternative hypothesis: mu < 3");

m0 = 3;
[H, P, CI, ZVAL] = ttest(x, m0, 'alpha', alpha, 'tail', 'left');
RR = [-inf, tinv(alpha, n-1)];
fprintf('\nThe confidence interval for m0 = %4.4f is (%4.4f,%4.4f)\n', m0, CI);
fprintf('the rejection region is (%4.4f,%4.4f)\n', RR);
fprintf('the value of the test statistic t is %4.4f\n', ZVAL.tstat);
fprintf('the P-value of the test is %4.4f\n', P);
if H == 1
    fprintf('The null hypothesis is rejected.\n') 
    fprintf('The data suggests that the average is smaller than 3.\n')
    % the number of particles seem to be smaller
else
    fprintf('The null hypothesis is not rejected.\n')
    fprintf('The data suggests that the average is not smaller than 3.\n')
end

% b)
confidence = input("confidence interval: ");
alpha = 1 - confidence; % significance level
    
ss = std(x); % standard deviation

q1 = chi2inv(alpha / 2, n - 1);
q2 = chi2inv(1 - alpha / 2, n - 1);

ci1 = (n - 1) * ss / q2;
ci2 = (n - 1) * ss / q1;

fprintf("the confidence interval for standard deviation: %4.4f is: (%4.4f, %4.4f)\n", ss, sqrt(ci1), sqrt(ci2));
