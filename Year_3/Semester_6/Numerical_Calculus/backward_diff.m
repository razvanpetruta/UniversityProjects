function t = backward_diff(f)
    n = length(f);
    t = zeros(n);
    t(:, 1) = f';
    for k = 2:n
        for i = n:-1:k
            t(i, k) = t(i, k-1) - t(i-1, k-1);
        end
    end
end
