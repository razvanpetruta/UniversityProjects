%{

TESTS


FOR ONE POPULATION
- for population mean, sigma known => ztest(x, m0, sigma, 'alpha', alpha, 'tail', 'right/left/both')
    [H, P, CI, ZVAL] -> ztest
    ZVAL = the value of the test statistic
    P = the P-value 
    H = the result of the test
    CI = confidence interval

- for population mean, sigma unknown => ttest(x, m0, 'alpha', alpha, 'tail', 'right/left/both')
    [H, P, CI, ZVAL] -> ttest
    ZVAL.fstat = the value of the test statistic
    P = the P-value 
    H = the result of the test
    CI = confidence interval

- for population variance
    [H, P, CI, ZVAL] -> vartest(x, sigma, 'alpha', alpha, 'right/left/both')
    ZVAL.chisqstat = the value of the test statistic
    P = the P-value 
    H = the result of the test
    CI = confidence interval

For left tail test: RR = [-inf, quantile], quantile = tinv(alpha, n-1),
                                           norminv(alpha), chi2inv(alpha, n-1);
For right tail test: RR = [quantile, inf], quantile = tinv(1-alpha, n-1),
                                           norminv(1-alpha), chi2inv(1-alpha, n-1);
For two tail test: RR = [-inf, quantile1] U [quantile2, inf], 
                                     - quantile1 = tinv(alpha/2, n-1),
                                       norminv(alpha/2), chi2inv(alpha/2, n-1);
                                     - quantile2 = tinv(1-alpha/2, n-1),
                                       norminv(1-alpha/2), chi2inv(1-alpha/2, n-1);

H = 1 -> null hypothesis is rejected -> the standard is not met
H = 0 -> null hypothesis is not rejected -> the standard is met


FOR TWO POPULATIONS
- for population means, sigma1, sigma2 known


- for population means, sigma1 = sigma2 unknown
    n = n1 + n2 - 2;
    [H, P, CI, ZVAL] = ttest2(pr, rg, "alpha", alpha, "tail", "right");
    tinv(alpha, n), tinv(1-alpha, n); tinv(1-alpha/2, n), tinv(alpha/2, n)

- for population means, sigma1 != sigma2 unknown
    c = (v1/n1)/(v1/n1 + v2/n2);
    n = 1/(c^2/(n1 - 1) + (1 - c)^2/(n2 - 1));
    [H, P, CI, ZVAL] = ttest2(pr, rg, 'alpha', alpha, 'tail', 'right');
    tinv(alpha, n), tinv(1-alpha, n); tinv(1-alpha/2, n), tinv(alpha/2, n)

- for population variances
    f1 = finv(alpha/2, n1-1, n2-1);
    f2 = finv(1-alpha/2, n1-1, n2-1); %quantiles for the rejection region of the two tailed test
    [H, P, CI, ZVAL] = vartest2(pr, rg, "alpha", alpha);


%}