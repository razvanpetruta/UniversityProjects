﻿function [I, nf] = romberg(f, a, b, err, nmax)
    R = zeros(nmax);
    nf = 2;
    h = b - a;
    R(1, 1) = h / 2 * sum(f([a, b]));
    for k = 2 : 1 : nmax
        x = a + ((1 : 2 ^ (k - 2)) - 0.5) * h;
        R(k, 1) = 0.5 * (R(k - 1, 1) + h * sum(f(x)));
        nf = nf + length(x);
        for j = 2 : k
            R(k, j) = (4 ^ (j - 1) * R(k, j -1) - R(k - 1, j -1)) / (4 ^ (j - 1) - 1);
        end
        if abs(R(k, k) - R(k - 1, k - 1)) < err
            I = R(k, k);
            R(1 : k, 1 : k)
            return;
        end
        h = h / 2;
    end
    error('too difficult');
end