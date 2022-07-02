# ======================== UI FUNCTIONS =============================
def read_complex_number():
    """
    Read a complex number.
    :return: a list containing the real and imaginary part
    """
    print("Enter a complex number: ")

    real_part = int(input("real part = "))
    imag_part = int(input("imaginary part = "))

    nr = []

    set_real_part(nr, real_part)
    set_imag_part(nr, imag_part)

    return nr


def write_complex_number(nr):
    """
    Print a complex number as a + bi.
    :param nr: the complex number
    """
    if get_imag_part(nr) >= 0:
        print(str(get_real_part(nr)) + " + " + str(get_imag_part(nr)) + 'i')
    else:
        print(str(get_real_part(nr)) + " - " + str(-1 * get_imag_part(nr)) + 'i')


def print_options():
    """
    Print the options menu.
    """
    print("What do you want to do? (select the number)")
    print("1. Read a list of complex numbers (in z = a + bi form) from the console.")
    print("2. Display the entire list of numbers on the console.")
    print("3. Display the longest sequence that has a certain property.")
    print("4. Exit the application.")


def print_option3_properties():
    """
    Print the properties for option 3.
    """
    print("a. Numbers with a strictly increasing real part.")
    print("b. Real numbers")


def get_property():
    """
    Read the property for option 3.
    :return: 'a' or 'b'
    """
    print("Please choose a or b")
    prop = input("property = ")
    return prop


def get_option():
    """
    Read the option.
    :return: 1, 2, 3 or 4
    """
    option = int(input("option = "))
    return option


def error_options():
    """
    Print an error if the user inputs an invalid option.
    """
    print("You must choose 1, 2, 3 or 4!")


def print_list(nr_list):
    """
    Print the complex numbers of a list.
    :param nr_list: the list of complex numbers
    """
    for i in range(0, len(nr_list)):
        write_complex_number(nr_list[i])


def add_elements():
    """
    Read a list of complex numbers which will be added to the current list.
    :return: a list with the new elements
    """
    new_list = []
    nr_of_el = int(input("number of elements = "))

    for i in range(0, nr_of_el):
        el = read_complex_number()
        new_list.append(el)

    return new_list


def list_is_empty():
    """
    Print the corresponding message in case our list is empty.
    """
    print("The list is empty")


def no_real_numbers():
    """
    Print the corresponding message in case our list does not have real numbers.
    """
    print("There is no real number in the list")


def exit_message():
    """
    Print an exit message.
    """
    print("Exiting...")


# =========================== NON UI FUNCTIONS ================================
def set_real_part(nr, real_part):
    """
    Set the real part of a number.
    :param nr: list that will contain the real and imag part of the number
    :param real_part: the real part
    """
    nr.append(real_part)


def set_imag_part(nr, imag_part):
    """
    Set the imaginary part of a number.
    :param nr: list that will contain the real and imag part of the number
    :param imag_part: the imaginary part
    """
    nr.append(imag_part)


def get_real_part(nr):
    """
    Return the real part of a complex number.
    :param nr: the complex number
    :return: real part
    """
    return nr[0]


def get_imag_part(nr):
    """
    Return the imaginary part of a complex number.
    :param nr: the complex number
    :return: imaginary part
    """
    return nr[1]


def valid_option(option):
    """
    Check if the option inserted by the user is valid.
    :param option: the inserted option
    :return: True or False
    """
    if option in [1, 2, 3, 4]:
        return True
    return False


def init_list():
    """
    Initialise the current list with 10 elements.
    :return: the current list
    """
    nr_list = [[1, 2], [1, 0], [2, 0], [1, 0], [2, 2], [3, 3], [4, 0], [5, 0], [6, 0], [7, 0]]
    return nr_list


def extend_list(nr_list, new_list):
    """
    Extend the current list with the one inserted by the user.
    :param nr_list: current list
    :param new_list: the list provided by the user
    """
    nr_list.extend(new_list)


def valid_property(prop):
    """
    Check if the property from option 3 is valid.
    :param prop: the property provided by the user
    :return: True or False
    """
    if prop in ['a', 'b']:
        return True
    return False


def check_property():
    """
    Check the property from option 3 until it's correct.
    :return: property
    """
    prop = get_property()

    while not valid_property(prop):
        prop = get_property()

    return prop


def check_option():
    """
    Check the option until it's correct.
    :return: option
    """
    option = get_option()

    while not valid_option(option):
        error_options()
        option = get_option()

    return option


def get_delimited_list(left, right, nr_list):
    """
    Get sequence of the list.
    :param left: the left margin
    :param right: the right margin
    :param nr_list: the list from which we extract the sequence
    :return: the sequence as a list
    """
    sol = []
    for i in range(left, right + 1):
        sol.append(nr_list[i])
    return sol


def strictly_increasing_real(nr_list):
    """
    Solve option 3, property a. Return the longest sequence of complex numbers
    that have strictly increasing real part. Solving this problem is
    based on finding the left margin and the right margin of the sequence that
    fulfil the condition mentioned above.
    :param nr_list: the list we check
    :return: sequence as a list
    """
    left = -1
    right = -1
    length = 0
    max_length = 0
    i = 1

    while i < len(nr_list):
        if get_real_part(nr_list[i]) > get_real_part(nr_list[i - 1]):
            length += 1
        else:
            if length > max_length:
                max_length = length
                left = i - length - 1
                right = i - 1
            length = 0
        i += 1

    if length > max_length:
        left = i - length - 1
        right = i - 1

    if left == -1:
        return []

    sol = get_delimited_list(left, right, nr_list)

    return sol


def real_numbers(nr_list):
    """
    Solve option 3, property b. Return the longest sequence of real numbers
    (each number has the imaginary part equal to 0). Solving this problem is
    based on finding the left margin and the right margin of the sequence that
    fulfil the condition mentioned above.
    :param nr_list: the list we check
    :return: sequence as a list
    """
    left = -1
    right = -1
    length = 0
    max_length = 0
    i = 0

    while i < len(nr_list):
        if get_imag_part(nr_list[i]) == 0:
            length += 1
        else:
            if length > max_length:
                max_length = length
                left = i - length
                right = i - 1
            length = 0
        i += 1

    if length > max_length:
        left = i - length
        right = i - 1

    if left == -1:
        return []

    sol = get_delimited_list(left, right, nr_list)

    return sol


def solve_task1(nr_list):
    """
    Solve task 1.
    :param nr_list: the list
    """
    new_list = add_elements()
    extend_list(nr_list, new_list)


def solve_task2(nr_list):
    """
    Solve task 2.
    :param nr_list: the list
    """
    print_list(nr_list)


def solve_task3(nr_list):
    """
    Solve task 3.
    :param nr_list: the list
    """
    print_option3_properties()
    prop = check_property()
    if prop == 'a':
        sol = strictly_increasing_real(nr_list)
        if len(sol) > 0:
            print_list(sol)
        else:
            list_is_empty()
    else:
        sol = real_numbers(nr_list)
        if len(sol) > 0:
            print_list(sol)
        else:
            no_real_numbers()


def start():
    """
    Main function.
    """
    nr_list = init_list()

    options = {
        1: solve_task1,
        2: solve_task2,
        3: solve_task3
    }

    while True:
        print_options()
        option = check_option()

        if option == 4:
            exit_message()
            return

        # solve the other 3 tasks
        options[option](nr_list)


if __name__ == "__main__":
    start()
