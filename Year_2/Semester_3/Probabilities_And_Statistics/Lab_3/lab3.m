option = input("normal / Student / Fischer\n", "s");

switch option
    case "normal"
        mu = input("mu: ");
        sigma = input("sigma: ");
        
        a1 = normcdf(0, mu, sigma);
        a2 = 1 - a1;
        fprintf("P(X <= 0): %f\nP(X >= 0): %f\n", a1, a2);

        b1 = normcdf(1, mu, sigma) - normcdf(-1, mu, sigma);
        b2 = 1 - b1;
        fprintf("P(-1 <= X <= 1): %f\nP(X <= -1 or X >= 1): %f\n", b1, b2);

        alpha = input("alpha: ");

        while alpha < 0 || alpha > 1
            alpha = input("alpha: ");
        end

        c = norminv(alpha, mu, sigma);
        fprintf("%f\n", c);

        beta = input("beta: ");

        while beta < 0 || alpha > 1
            beta = input("beta: ");
        end

        d = norminv(1 - beta, mu, sigma);
        fprintf("%f\n", d);
    case "Student"
        x = 0;
        n = input("n: ");
        
        a1 = tcdf(0, n);
        a2 = 1 - a1;
        fprintf("P(X <= 0): %f\nP(X >= 0): %f\n", a1, a2);

        b1 = tcdf(1, n) - tcdf(-1, n);
        b2 = 1 - b1;
        fprintf("P(-1 <= X <= 1): %f\nP(X <= -1 or X >= 1): %f\n", b1, b2);

        alpha = input("alpha: ");
        c = tinv(alpha, n);
        fprintf("%f\n", c);

        beta = input("beta: ");
        d = tinv(1 - beta, n);
        fprintf("%f\n", d);
    case "Fischer"
        m = input("m: ");
        n = input("n: ");
        
        a1 = fcdf(0, m, n);
        a2 = 1 - a1;
        fprintf("P(X <= 0): %f\nP(X >= 0): %f\n", a1, a2);

        b1 = normcdf(1, m, n) - normcdf(-1, m, n);
        b2 = 1 - b1;
        fprintf("P(-1 <= X <= 1): %f\nP(X <= -1 or X >= 1): %f\n", b1, b2);

        alpha = input("alpha: ");
        c = finv(alpha, m, n);
        fprintf("%f\n", c);

        beta = input("beta: ");
        d = finv(1 - beta, m, n);
        fprintf("%f\n", d);
    otherwise
        fprintf("Wrong option\n");
end

