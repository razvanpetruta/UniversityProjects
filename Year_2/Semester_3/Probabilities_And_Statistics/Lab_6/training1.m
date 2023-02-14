alpha = input("significance level: ");

% the data
x1 = [4.6 0.7 4.2 1.9 4.8 6.1 4.7 5.5 5.4];
x2 = [2.5 1.3 2.0 1.8 2.7 3.2 3.0 3.5 3.4];

% the lengths
n1 = length(x1);
n2 = length(x2);

% the variances
v1 = var(x1);
v2 = var(x2);

%{
    a) null hyphothesis: v1 = v2
       alternative hyphothesis: v1 != v2

    b) null hyphothesis: mu1 = mu2
       alternative hyphothesis: mu1 > mu2
%}
% quantiles for the rejection region of the two tailed test
f1 = finv(alpha / 2, n1 - 1, n2 - 1);
f2 = finv(1 - alpha / 2, n1 - 1, n2 - 1); 

[H, P, CI, ZVAL] = vartest2(x1, x2, "alpha", alpha);

fprintf('The rejection region is (%6.4f, %6.4f) U (%6.4f, %6.4f)\n', -inf, f1, f2, inf);
fprintf('The value of the test statistic is %6.4f\n', ZVAL.fstat);
fprintf('The P-value for the variances test is %6.4f\n', P);
if H==0
    fprintf('The null hypothesis is not rejected.\n');
    fprintf('The variances seem to be equal.\n');
    
    % sigma1 = sigma2, because the variances are equal
    n = n1 + n2 - 2;
    t2 = tinv(1 - alpha, n); % quantile for right-tailed test (for rejection region)
    [H2, P2, CI2, ZVAL2] = ttest2(x1, x2, "alpha", alpha, "tail", "right");
    if H2 == 1
        fprintf('\tThe null hypothesis is rejected.\n');
        fprintf('\tSteel pipes lose more heat than glass pipes\n');
    else
        fprintf('\tThe null hypothesis is not rejected.\n');
        fprintf('\tSteel pipes DO NOT lose more heat than glass pipes\n');
    end
    fprintf('\tthe rejection region is (%6.4f,%6.4f)\n', t2, inf);
    fprintf('\tthe value of the test statistic is %6.4f\n', ZVAL2.tstat);
    fprintf('\tthe P-value of the test for diff. of means is %e\n', P2);
else
    fprintf('The null hypothesis is rejected.\n');
    fprintf('The variances seem to be different.\n');

    c = (v1/n1)/(v1/n1 + v2/n2);
    n = c^2/(n1 - 1) + (1 - c)^2/(n2 - 1);
    n = 1/n;
    t2 = tinv(1 - alpha, n); % quantile for right-tailed test (for rejection region)
    [H2, P2, CI2, ZVAL2] = ttest2(x1, x2, 'alpha', alpha, 'tail', 'right');
    if H2 == 1
        fprintf('\tThe null hypothesis is rejected.\n');
        fprintf('\tSteel pipes lose more heat than glass pipes\n');
    else
        fprintf('\tThe null hypothesis is not rejected.\n');
        fprintf('\tSteel pipes DO NOT lose more heat than glass pipes\n');
    end    
    fprintf('\tThe rejection region is (%6.4f,%6.4f)\n', t2, inf);
    fprintf('\tthe value of the test statistic is %6.4f\n', ZVAL2.tstat);
    fprintf('\tthe P-value of the test for diff. of means is %6.4f\n', P2);
end

