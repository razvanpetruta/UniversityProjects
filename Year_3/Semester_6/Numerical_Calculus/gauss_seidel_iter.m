function [x, int] = gauss_seidel_iter(A, b, x0, maxint, err)
    D = diag(diag(A));
    L = tril(A, -1);
    U = triu(A, 1);
    M = D + L;
    N = -U;
    T = M \ N;
    c = M \ b;
    normT = norm(T, inf);
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