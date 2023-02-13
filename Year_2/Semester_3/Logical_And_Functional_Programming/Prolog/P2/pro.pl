% P2 - 7

% a)
% Get the maximum element of a list
% getMaxAux(l1, l2, ..., ln, Acc) {
%     Acc, if n == 0
%     getMaxAux(l2, l3, ..., ln, l1), if l1 > Acc
%     getMaxAux(l2, l3, ..., ln, Acc), otherwise
% }
% getMaxAux(L: list, Acc: int, Max: int)
% getMaxAux(i, i, o)
getMaxAux([], Acc, Acc) :- !.
getMaxAux([H | L], Acc, Max) :-
    H > Acc,
    !,
    AccNew is H,
    getMaxAux(L, AccNew, Max).
getMaxAux([H | L], Acc, Max) :-
    H =< Acc,
    getMaxAux(L, Acc, Max).


% getMax(L: list, Max: int)
% getMax(i, o)
getMax(L, Max) :- getMaxAux(L, -999999, Max).


% Get a list of positions of the maximum element
% maxPosAux(l1, l2, l3, ..., ln, max, currentPos) {
%     empty list, if n == 0
%     {currentPos} U maxPosAux(l2, l3, ..., ln, max, currentPos + 1), if l1 == max
%     maxPosAux(l2, l3, ..., ln, max, currentPos + 1), otherwise
% }
% getPosAux(L: list, R: list, Max: int, P: int)
% getPosAux(i, o, i, i)
maxPosAux([], [], _, _) :- !.
maxPosAux([H | T], R, Max, P) :-
    H =:= Max,
    !,
    P1 is P + 1,
    maxPosAux(T, RN, Max, P1),
    R = [P | RN].
maxPosAux([H | T], R, Max, P) :-
    H =\= Max,
    !,
    P1 is P + 1,
    maxPosAux(T, R, Max, P1).


% Solves a)
% maxPos(L: list, R: list)
% maxPos(i, o)
maxPos(L, R) :-
    getMax(L, Max),
    maxPosAux(L, R, Max, 1).



% b)
% replaceWithMaxPos(l1, l2, l3, ..., ln) {
%     empty list, if n == 0
%     {l1} U replaceWithMaxPos(l1, l2, l3, ..., ln), if type(l1) == number
%     maxPos(l1) U replaceWithMaxPos(l1, l2, l3, ..., ln), if type(l1) == list
% }
% replaceWithMaxPos(L: list, R: list)
% replaceWithMaxPos(i, o)
replaceWithMaxPos([], []) :- !.
replaceWithMaxPos([H | T], [H | R]) :-
    number(H),
    !,
    replaceWithMaxPos(T, R).
replaceWithMaxPos([H | T], R) :-
    is_list(H),
    !,
    maxPos(H, New),
    replaceWithMaxPos(T, RN),
    R = [New | RN].
