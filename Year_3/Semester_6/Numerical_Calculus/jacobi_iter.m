function [x, int] = jacobi_iter(A, b, x0, maxint, err)
    M = diag(diag(A));
    N = M - A;
    T = inv(M) * N;
    normT = norm(T, inf);
    c = inv(M) * b;
    x = x0;
    xPrev = x0;
    for k = 1 : 1 : maxint
        x = T * x + c;
        if norm(x - xPrev, inf) < err * (1 - normT) / normT
            int = k;
            return 
        end
        xPrev = x;
    end
end