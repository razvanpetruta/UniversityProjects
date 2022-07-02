# Problem number 1
# Generate the first prime number larger than a given natural number n.

import math  # for sqrt


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


def next_prime(n):
    """
    Return the next prime number.
    Input: n - current number
    Output: the next prime number
    """

    # in case our n is prime, we skip it
    n += 1

    while not prime(n):
        n += 1

    return n


if __name__ == "__main__":
    n = int(input("Enter the number: "))

    print("The next prime number is", next_prime(n))

