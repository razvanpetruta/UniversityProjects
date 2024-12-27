function x = gauss_elimination(A, b)
    x = zeros(size(b));
    [r, n] = size(A);
    A = [A, b];
    for j = 1 : 1 : n - 1
        [u, p] = max(abs(A(j : n, j)));
        p = p + j - 1;
        A([j, p], :) = A([p, j], :);
        for i = j + 1 : r
            m = A(i, j) / A(j, j);
            A(i, :) = A(i, :) - m * A(j, :);
        end 
    end
    x = backward_substitution(A(:, 1 : n), A(:, n + 1)); 
end