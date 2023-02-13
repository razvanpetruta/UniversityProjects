; 1
; a) write a function to return the n-th element of a list, or NIL if such an element does not exist

;; nElem(l1, l2, l3, ..., ln, k) = {
;;     nil, if n = 0 and k > 0
;;     l1, if k = 1
;;     nElem(l2, l3, l4, ..., ln, k - 1), otherwise
;; }

(defun nElem (l k)
    (cond
        ((and (null l) (> k 0)) nil)
        ((= k 1) (car l))
        (t (nElem (cdr l) (- k 1)))
    )
)

; (nElem '(1 2 3 4 5 6) 3)



; b) write a function to check whether an atom E is a member of a list which is not necessarily linear.

;; isMember(l1, l2, l3, ..., ln, e) = {
;;     false, if n = 0
;;     true, if l1 is an atom and l1 = E
;;     isMember(l1) or isMember(l2, l3, l4, ..., ln), if l1 is a list
;;     isMember(l2, l3, l4, ..., ln, e), otherwise
;; }

(defun isMember (l e)
    (cond 
        ((null l) nil)
        ((and (atom (car l)) (equal (car l) e)) t)
        ((listp (car l)) (or (isMember (car l) e) (isMember (cdr l) e)))
        (t (isMember (cdr l) e))
    )
)

; (isMember '(1 (3 (1 2)) 3) 2)


; c) Write a function to determine the list of all sublists of a given list, on any level.
;    A sublist is either the list itself, or any element that is a list, at any level. Example:
;    (1 2 (3 (4 5) (6 7)) 8 (9 10)) => 5 sublists :
;    ( (1 2 (3 (4 5) (6 7)) 8 (9 10)) (3 (4 5) (6 7)) (4 5) (6 7) (9 10) )

(defun allSublists (l) 
    (cond
        ((atom l) nil)
        (t (apply 'append (list l) (mapcar 'allSublists l)))
    )
)

; (allSublists '(1 2 (3 (4 5) (6 7)) 8 (9 10)))



; d) write a function to transform a linear list into a set.

;; listToSet(l1, l2, l3, ..., ln, acc) = {
;;     acc, if n = 0
;;     listToSet(l2, l3, l4, ..., ln, acc U {l1}), if l1 not in acc
;;     listToSet(l2, l3, l4, ..., ln, acc), otherwise
;; }

(defun listToSet (l acc)
    (cond
        ((null l) acc)
        ((not (isMember acc (car l))) (listToSet (cdr l) (append acc (list (car l)))))
        (t (listToSet (cdr l) acc))
    )
)

; (listToSet '(1 1 2 1 3 3 4 6 7 7) ())