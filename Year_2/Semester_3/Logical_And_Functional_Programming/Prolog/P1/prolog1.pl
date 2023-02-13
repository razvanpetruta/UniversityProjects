% P1 - 2

% Remove all occurrences of a certain atom from a list
% deleteAll(el, l1, l2, l3, ..., ln) {
%     empty list, if n = 0
%     {l1} U deleteAll(el, l2, l3, l4, ..., ln), if l1 != el
%     deleteAll(el, l2, l3, l4, ..., ln) otherwise
% }
deleteAll(_, [], []) :- !.
deleteAll(E, [H | T], R) :-
    E =:= H,
    deleteAll(E, T, R),
    !.
deleteAll(E, [H | T], R) :-
    E =\= H,
    deleteAll(E, T, RN),
    R = [H | RN],
    !.



% Define a predicate to produce a list of pairs from an initial list of atoms.
% (frequency of the elements)

% Count the occurences of an element in a list
% count(el, l1, l2, l3, ..., ln) {
%     0, if n == 0
%     1 + count(el, l2, l3, l4, ..., ln), if l1 == el
%     count(el, l2, l3, l4, ..., ln) otherwise
% }
count(_, [], 0) :- !.
count(E, [H | T], R) :-
    H =:= E,
    count(E, T, RN),
    R is RN + 1,
    !.
count(E, [H | T], R) :-
    H =\= E,
    count(E, T, R),
    !.

% Build the frequency for every element
% Use a set to check the elements that were already counted
% frequency(l1, l2, ..., ln, s1, s2, ..., sm) {
%     empty set, if n == 0
%     {[l1, count(el, l2, l3, l4, ..., ln) + 1]} U frequency(l2, ..., ln, el, s1, s2, ..., sm), if l1 not in s1, s2, ..., sm
%     frequency(l2, ..., ln, el, s1, s2, ..., sm), otherwise
% }
frequency([], _, []) :- !.
frequency([H | T], Set, R) :-
    count(H, Set, SetOc),
    SetOc =:= 0,
    SetN = [H | Set],
    count(H, T, Oc),
    Ocp is Oc + 1,
    frequency(T, SetN, RN),
    R = [[H, Ocp] | RN],
    !.
frequency([H | T], Set, R) :-
    count(H, Set, SetOc),
    SetOc =\= 0,
    frequency(T, Set, R),
    !.

% Solve the frequency problem
solve(L, R) :-
    frequency(L, [], R).