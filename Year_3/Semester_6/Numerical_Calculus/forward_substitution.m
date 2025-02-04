﻿function x = forward_substitution(L, b)
    x = zeros(size(b));
    n = length(b);
    for k = 1 : 1 : n
        x(k) = (b(k) - L(k, 1 : k - 1) * x(1 : k - 1)) / L(k, k);
    end
end