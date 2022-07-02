# Problem number 11
"""
The numbers n1 and n2 have the property P if their writing in base 10 uses the same
digits (e.g. 2113 and 323121). Determine whether two given natural numbers have property P.
"""


def get_digits(n):
    """
    Return a list with 10 elements (indexed from 0 to 9), where 1 means that the digit which
    corresponds to the index was used and 0 means that the digit wasn't used.
    Input: n - the number
    Output: the frequency list
    """

    digits = [0] * 10

    if n == 0:
        digits[0] = 1

    while n != 0:
        digits[n % 10] = 1
        n //= 10

    return digits


def check_property(list1, list2):
    """
    Return True if the numbers built with those 2 lists use the same digits, False otherwise
    Input: frequency lists of the used digits.
    Output: True or False
    """

    for i in range(0, 10):
        if list1[i] != list2[i]:
            return False

    return True


if __name__ == "__main__":
    a = int(input("Enter the first number: "))
    b = int(input("Enter the second number: "))

    a_digits = get_digits(a)
    b_digits = get_digits(b)

    if check_property(a_digits, b_digits):
        print("The numbers have the property P")
    else:
        print("The numbers don't have the property P")

