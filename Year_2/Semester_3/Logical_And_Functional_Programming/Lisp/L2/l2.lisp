; 5. Write a function that computes the sum of even numbers and then decrease the sum of odd numbers, at any level of a list.

;; checkEvenOdd(x) = {
;;     x, if x is even
;;     -x, otherwise
;; }
(defun checkEvenOdd (x)
    (cond
        ((= (mod x 2) 0) x)
        (t (* x -1))
    )
)

;; sumEvenMinusSumOdd(l) = {
;;     checkEvenOdd(l), if l is numberp
;;     0, if l is an atom
;;     sum(sumEvenMinusSumOdd(li)) i=1,n, otherwise
;; }
(defun sumEvenMinusSumOdd (l)
    (cond
        ((numberp l) (checkEvenOdd l))
        ((atom l) 0)
        (t (apply '+ (mapcar 'sumEvenMinusSumOdd l)))
    )
)