; 1. Return the path from the root to a certain given node X

;; travLeft(l1, l2, l3, ..., ln, v, m) = { 
;;     nil, if n = 0
;;     nil, if v = m + 1
;;     {l1} U {l2} U travLeft(l3, l4, l5, ..., ln, v + 1, m + l2), otherwise
;; }

(defun travLeft (L v m)
    (cond 
        ((null L) nil)
        ((= v (+ 1 m)) nil)
        (t (cons (car L) (cons (cadr L) (travLeft (cddr L) (+ 1 v) (+ (cadr L) m)))))
    )
)

(defun leftSubt (L)
    (travLeft (cddr L) 0 0)
)

;; travRight(l1, l2, l3, ..., ln, v, m) = {
;;     nil, if n = 0
;;     l1, l2, l3, ..., ln, if v = m + 1
;;     travRight(l3, l4, l5, ..., ln, v + 1, m + l2), otherwise
;; }

(defun travRight (L v m)
    (cond 
        ((null L) nil)
        ((= v (+ 1 m)) L)
        (t (travRight (cddr L) (+ 1 v) (+ (cadr L) m)))
    )
)

(defun rightSubt (L)
    (travRight (cddr L) 0 0)
)

;; appears(l1, l2, l3, ..., ln, e) = {
;;     false, if n = 0
;;     true, if l1 = E
;;     appears(leftSubt(L), e) or appears(rightSubt(L), e), otherwise
;; }

(defun appears (L e)
    (cond
        ((null L) nil)
        ((eq (car L) e) t)
        (t (or (appears (leftSubt L) e) (appears (rightSubt L) e)))
    )
)

;; path(l1, l2, l3, ..., ln, x) = {
;;     l1, if l1 = x 
;;     {l1} U path(leftSubt(l1, l2, l3, ..., ln), x), if appears(leftSubt(l1, l2, l3, ..., ln), x)
;;     {l1} U path(rightSubt(l1, l2, l3, ..., ln), x), otherwise
;; }

(defun path (L X)
    (cond
        ((null L) nil)
        ((eq (car L) X) (list X))
        ((appears (leftSubt L) X) (append (list (car L)) (path (leftSubt L) X)))
        (t (append (list (car L)) (path (rightSubt L) X)))
    )
)

; l2 5