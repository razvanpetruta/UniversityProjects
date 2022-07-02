# Problem 14
"""
Determine the n-th element of the sequence 1,2,3,2,2,5,2,2,3,3,3,7,2,2,3,3,3,... obtained from the
sequence of natural numbers by replacing composed numbers with their prime divisors, each divisor
d being written d times, without memorizing the elements of the sequence.
"""
import math


def prime(n):
    """
    Return True if a number is prime, False otherwise.
    Input: n - the number we check
    Output: True or False
    """
    if n < 2:
        return False

    if n > 2 and n % 2 == 0:
        return False

    for i in range(3, int(math.sqrt(n)) + 1, 2):
        if n % i == 0:
            return False

    return True


def valid_position(n):
    """
    Return True if the user inserted a number greater than or equal to 1, False otherwise.
    Input: n - the number
    Output: True or False
    """
    if n < 1:
        return False

    return True


def increase_cnt(n, cnt, el):
    """
    Return the current position in the sequence and also return the element if we got to
    the wanted position or return 0 if we didn't reach the n-th position.
    Input: n - our needed position
           cnt - our current position
           el - the natural number we operate on
    Output: cnt and searched element or cnt and 0 (in case we didn't reach the n-th position)
    """
    if prime(el):
        cnt += 1
        if cnt >= n:
            return cnt, el
        return cnt, 0
    else:
        d = 2
        while el > 1:
            if el % d == 0:
                cnt += d
            if cnt >= n:
                return cnt, d
            while el % d == 0:
                el //= d
            d += 1
            if d * d > el:
                d = el

        return cnt, 0


def solve(n):
    """
    Return the n-th element in the sequence.
    Input: n - our needed position
    Output: the element on the n-th position
    """
    # if n is 1, 2 or 3 we just return n
    if n <= 3:
        return n

    # if n is greater than 3
    cnt = 3
    el = 4
    # we initialise position with 0 because we want to stop when we will find an element,
    # which will be stored in position and that element cannot be 0, so our while loop will work just fine
    position = 0

    while position == 0:
        cnt, position = increase_cnt(n, cnt, el)
        el += 1

    return position


def get_n():
    n = int(input("Insert the position of the number in the sequence "))

    while not valid_position(n):
        print("The position must be greater than or equal to 1.")
        n = int(input("Insert the position of the number in the sequence "))

    return n


if __name__ == "__main__":
    n = get_n()

    print("The element you are looking for is", solve(n))
