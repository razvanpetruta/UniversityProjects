% 4. The list a1...an is given. Write a predicate to determine all sublists strictly ascending of this list a.


% subset(l1, l2, l3, ..., ln) = {
%     [], if n = 0
%     subset(l2, l3, l4, ..., ln)
%     {l1} U subset(l2, l3, l4, ..., ln)
% }
% subset(L: list, S: list)
% (i, o)
subset([], []) :- !.
subset([H | T], [H | S]) :-
    subset(T, S).
subset([_ | T], S) :-
    subset(T, S).


% isSorted(l1, l2, l3, ..., ln) = {
%     true, if n = 0 or n = 1
%     isSorted(l2, l3, l4, ..., ln), if l1 <= l2
%     false, otherwise
% }
% isSorted(L: list)
% (i)
isSorted([]) :- !.
isSorted([_]) :- !.
isSorted([H1, H2 | T]) :-
    H1 < H2,
    isSorted([H2 | T]).


process([], []) :- !.
process(L, S) :-
    subset(L, S),
    isSorted(S).


solve(L, R) :-
    findall(X, process(L, X), R).