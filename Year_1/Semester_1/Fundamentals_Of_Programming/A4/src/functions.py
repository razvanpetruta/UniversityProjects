"""
    Module containing NON-UI functions for required functionalities.
"""


# ====================== MODULES ==========================================
import math
import ui


# =========================================================================


# ====================== GETTERS AND SETTERS ==============================
def get_real(el):
    """
    Get the real part of an element.
    :param el: the element
    :return: the real part
    """
    return el["real"]


def get_imag(el):
    """
    Get the imaginary part of an element.
    :param el: the element
    :return: the imaginary part
    """
    return el["imag"]


def set_real(el, real):
    """
    Set the real part of an element.
    :param el: the element
    :param real: the real part
    """
    el["real"] = real


def set_imag(el, imag):
    """
    Set the imag part of an element.
    :param el: the element
    :param imag: the imaginary part
    """
    el["imag"] = imag


def get_current(my_list):
    """
    Get the last stage from my_list.
    :param my_list: the list
    :return: the last element of the list
    """
    return my_list[-1]


# =======================================================================


# ======================== INTERMEDIATE functions =======================
def split_command(user_info):
    """
    Get the input from the user and split it into the operation and the
    info regarding that operation.
    :param user_info: information provided by the user
    :return: a tuple containing the operation and the info
    """
    user_info = user_info.strip()
    tokens = user_info.split(" ", 1)

    operation = tokens[0].lower() if len(tokens) > 0 else None
    info = tokens[1].lower() if len(tokens) > 1 else None

    return operation, info


def init_list():
    """
    Initialize the list with 10 elements.
    :return: the list containing 10 elements
    """
    nr_list = []
    first = [
        {
            "real": 1,
            "imag": -1
        },
        {
            "real": 2,
            "imag": 0
        },
        {
            "real": 0,
            "imag": -4
        },
        {
            "real": 2,
            "imag": 2
        },
        {
            "real": 4,
            "imag": 0
        },
        {
            "real": 3,
            "imag": -2
        },
        {
            "real": 0,
            "imag": 0
        },
        {
            "real": 1,
            "imag": 1
        },
        {
            "real": 1,
            "imag": 56
        },
        {
            "real": 2,
            "imag": -2
        }
    ]
    nr_list.append(first)

    return nr_list


def modulo(el):
    """
    Calculate the modulo of an complex number.
    :param el: the complex number
    :return: the modulo of the complex number
    """
    sol = math.sqrt(get_real(el) ** 2 + get_imag(el) ** 2)
    return sol


def equal(a, b):
    """
    Check if 2 complex numbers are equal.
    :param a: the first number
    :param b: the second number
    :return: True or False
    """
    return get_real(a) == get_real(b) and get_imag(a) == get_imag(b)


def get_rid_of_spaces(el):
    """
    Get rid of the spaces in a string.
    :param el: the string we operate on
    :return: the string without spaces
    """
    sol = ""

    for i in el:
        if i != ' ':
            sol += i

    return sol


def construct_complex_from_string(s):
    """
    Construct a complex number from a string.
    :param s: string containing the complex number
    :return: a dictionary containing the real and the imag part
    """
    s = s.strip()
    s = get_rid_of_spaces(s)

    if 'i' in s:
        s = s.replace('i', 'j')

    copy = s
    copy = copy.replace("j", "").replace("+", "").replace("-", "")
    if not(copy == "" or copy.isnumeric()):
        raise ValueError("Make sure the complex number is correct")

    sol = complex(s)
    real = int(sol.real)
    imag = int(sol.imag)

    sol = {
        "real": real,
        "imag": imag
    }

    return sol


def get_interval_from_info(info):
    """
    Get the indexes from a string.
    :param info: the string
    :return: left index and right index
    """
    info = info.strip().lower()

    if "to" not in info:
        raise ValueError("Make sure the command is correct")

    tokens = info.split("to")

    if not (tokens[0].strip().isnumeric() and tokens[1].strip().isnumeric()):
        raise ValueError("Make sure the command is correct")

    left = int(tokens[0].strip())
    right = int(tokens[1].strip())

    return left, right


# ==================================================================


# ====================== LIST command ==============================
def solve_list_real(left, right, nr_list):
    """
    Check for real numbers in range of left and right.
    :param left: left margin
    :param right: right margin
    :param nr_list: the list we check
    :return: a list containing the real numbers from the interval
    """
    if right < left or left < 0 or right < 0:
        raise ValueError("The interval seems wrong")

    sol = []

    for i in range(left, right + 1):
        if get_imag(nr_list[i]) == 0:
            sol.append(nr_list[i])

    return sol


def solve_list_modulo_equal(nr_list, val):
    """
    Check for complex numbers that have modulo equals to the value provided.
    :param nr_list: the list we check
    :param val: the value we compare to
    :return: the list containing the solutions
    """
    sol = []

    for i in range(len(nr_list)):
        if modulo(nr_list[i]) == val:
            sol.append(nr_list[i])

    return sol


def solve_list_modulo_smaller(nr_list, val):
    """
    Check for complex numbers that have modulo smaller than the value provided.
    :param nr_list: the list we check
    :param val: the value we compare to
    :return: the list containing the solutions
    """
    sol = []

    for i in range(len(nr_list)):
        if modulo(nr_list[i]) < val:
            sol.append(nr_list[i])

    return sol


def solve_list_modulo_greater(nr_list, val):
    """
    Check for complex numbers that have modulo greater than the value provided.
    :param nr_list: the list we check
    :param val: the value we compare to
    :return: the list containing the solutions
    """
    sol = []

    for i in range(len(nr_list)):
        if modulo(nr_list[i]) > val:
            sol.append(nr_list[i])

    return sol


def check_info_list(info, nr_list):
    """
    Handle the "list" command.
    :param info: information for the list command
    :param nr_list: the list we check
    :return: a list containing the solutions
    """
    if not ("real" in info or "modulo" in info):
        raise ValueError("We can only list some \"real\" info and \"modulo\" info")

    info = info.strip()
    tokens = info.split()

    for i in range(len(tokens)):
        tokens[i] = tokens[i].strip().lower()

    sol = []

    if "real" in tokens:
        if len(tokens) < 4 or "to" not in tokens:
            raise IndexError("The command seems wrong")

        if not (tokens[1].lstrip('-').isnumeric() and tokens[3].lstrip('-').isnumeric()):
            raise ValueError("Missing indexes or incorrect indexes")

        left = int(tokens[1])
        right = int(tokens[3])
        sol.extend(solve_list_real(left, right, nr_list))

    elif "modulo" in tokens:
        if len(tokens) < 3:
            raise IndexError("The command seems wrong")

        if not tokens[2].strip("-").replace(".", "").isnumeric():
            raise ValueError("Missing modulo or incorrect modulo")

        val = float(tokens[2])
        if tokens[1] == "=":
            sol.extend(solve_list_modulo_equal(nr_list, val))
        elif tokens[1] == "<":
            sol.extend(solve_list_modulo_smaller(nr_list, val))
        elif tokens[1] == ">":
            sol.extend(solve_list_modulo_greater(nr_list, val))
        else:
            raise ValueError("The only options are \"<\", \"=\" or \">\"")
    else:
        raise ValueError("Make sure that the command is correct")

    return sol


def solve_list(info, my_list):
    """
    LIST command.
    """
    nr_list = get_current(my_list)

    if info is None:
        ui.print_list(nr_list)
    else:
        sol = check_info_list(info, nr_list)
        ui.print_list(sol)


# ====================================================================


# ============================= ADD command ==========================
def check_info_add(info, nr_list):
    """
    Add command -> add a number to the list.
    :param info: info containing the number
    :param nr_list: the list in which we add the number
    """
    new_list = nr_list[:]

    info = info.strip()
    info = get_rid_of_spaces(info)

    el = construct_complex_from_string(info)
    new_list.append(el)

    return new_list


def solve_add(info, my_list):
    """
    ADD command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to enter a number in order to add")
    else:
        new_list = check_info_add(info, nr_list)
        my_list.append(new_list)


# =================================================================


# ========================= REPLACE command =======================
def replace_value(source, destination, nr_list):
    """
    Replace every appearance of the source value with the destination value from nr_list.
    :param source: the source value
    :param destination: the destination value
    :param nr_list: the list in which we replace the values
    """
    if source not in nr_list:
        raise ValueError("The element you're trying to replace does not exist")

    new_list = []

    for el in nr_list:
        if equal(el, source):
            new_list.append(destination)
        else:
            new_list.append(el)

    return new_list


def check_info_replace(info, nr_list):
    """
    Replace a value with other value.
    :param info: info containing the numbers
    :param nr_list: the list in which we replace
    :return: the new list with replaced values
    """
    info = info.strip().lower()
    info = get_rid_of_spaces(info)

    if "with" not in info:
        raise ValueError("Make sure the command is correct")

    tokens = info.split("with")
    source = construct_complex_from_string(tokens[0])
    destination = construct_complex_from_string(tokens[1])

    new_list = replace_value(source, destination, nr_list)

    return new_list


def solve_replace(info, my_list):
    """
    REPLACE command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("Make sure your command is correct")
    else:
        new_list = check_info_replace(info, nr_list)
        my_list.append(new_list)


# ==============================================================


# ===================== REMOVE command =========================
def remove_index(index, nr_list):
    """
    Remove an index from a list.
    :param index: the index that needs to be removed
    :param nr_list: the list from which we remove the index
    """
    if index >= len(nr_list) or index < 0:
        raise IndexError("The index is not correct")

    new_list = nr_list[:]

    del new_list[index]

    return new_list


def remove_sequence(left, right, nr_list):
    """
    Remove a sequence from a list.
    :param left: the left margin
    :param right: the right margin
    :param nr_list: the list from which we remove
    """
    if right < left or (left < 0 or right < 0) or right >= len(nr_list):
        raise IndexError("The indexes are not correct")

    new_list = nr_list[:]

    del new_list[left:right + 1]

    return new_list


def check_info_remove(info, nr_list):
    """
    Remove command -> remove an index or and delimited interval.
    :param info: info containing the index or the interval
    :param nr_list: the list from which we eliminate numbers
    """
    info = info.strip().lower()
    info = get_rid_of_spaces(info)

    new_list = []

    if "to" in info:
        left, right = get_interval_from_info(info)
        new_list.extend(remove_sequence(left, right, nr_list))
    else:
        if not info.isnumeric():
            raise ValueError("Make sure the command is correct")
        index = int(info)
        new_list.extend(remove_index(index, nr_list))

    return new_list


def solve_remove(info, my_list):
    """
    REMOVE command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to enter an index in order to remove")
    else:
        new_list = check_info_remove(info, nr_list)
        my_list.append(new_list)


# ================================================================

# ========================= INSERT command =======================
def check_info_insert(info, nr_list):
    """
    Insert command -> insert a number to a certain position.
    :param info: info containing the number and the position
    :param nr_list: the list in which we insert the number
    """
    info = info.strip().lower()

    if "at" not in info:
        raise ValueError("Make sure that the command is correct")

    tokens = info.split("at")

    el = construct_complex_from_string(get_rid_of_spaces(tokens[0]))

    if not tokens[1].strip().isnumeric():
        raise ValueError("The position is not correct")

    position = int(tokens[1].strip())

    if position > len(nr_list):
        raise IndexError("The position is not correct")

    new_list = nr_list[:]

    new_list.insert(position, el)

    return new_list


def solve_insert(info, my_list):
    """
    INSERT command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to enter a number in order to insert")
    else:
        new_list = check_info_insert(info, nr_list)
        my_list.append(new_list)


# ===============================================================


# ========================== UNDO command =======================
def solve_undo(info, my_list):
    """
    Undo the last operation that modified the list.
    """
    if len(my_list) > 1:
        del my_list[-1]
    else:
        raise ValueError("Cannot undo, you are at the first list")


# ===============================================================


# ========================== SUM command ========================
def sum_range(left, right, nr_list):
    """
    Sum the elements from a given range.
    :param left: left margin
    :param right: right margin
    :param nr_list: the list
    :return: the sum
    """
    if right < left or right >= len(nr_list):
        raise ValueError("The indexes seem wrong")

    sol = {"real": 0, "imag": 0}

    for i in range(left, right + 1):
        set_real(sol, get_real(sol) + get_real(nr_list[i]))
        set_imag(sol, get_imag(sol) + get_imag(nr_list[i]))

    return sol


def check_info_sum(info, nr_list):
    """
    Determine the range and sum the elements from that range.
    :param info: info containing the range
    :param nr_list: the list
    :return: the sum
    """
    left, right = get_interval_from_info(info)

    sol = sum_range(left, right, nr_list)

    return sol


def solve_sum(info, my_list):
    """
    Solve sum command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to enter the range")
    else:
        ui.print_el(check_info_sum(info, nr_list))


# ===============================================================


# ===================== PRODUCT command =========================
def product_range(left, right, nr_list):
    """
    Return the product of complex numbers from a given range.
    :param left: left margin
    :param right: right margin
    :param nr_list: the list
    :return: the product
    """
    if right < left or right >= len(nr_list):
        raise ValueError("The indexes seem wrong")

    sol = {"real": get_real(nr_list[left]), "imag": get_imag(nr_list[left])}

    for i in range(left + 1, right + 1):
        a1 = get_real(sol)
        b1 = get_imag(sol)
        a2 = get_real(nr_list[i])
        b2 = get_imag(nr_list[i])
        sol["real"] = a1 * a2 - b1 * b2
        sol["imag"] = a1 * b2 + a2 * b1

    return sol


def check_info_product(info, nr_list):
    """
    Determine the range and the product of elements from that range.
    :param info: info containing the range
    :param nr_list: the list
    :return: the product
    """
    left, right = get_interval_from_info(info)

    sol = product_range(left, right, nr_list)

    return sol


def solve_product(info, my_list):
    """
    Solve product command
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to enter the range")
    else:
        ui.print_el(check_info_product(info, nr_list))


# =================================================================


# ====================== FILTER command ===========================
def filter_real(nr_list):
    """
    Return a list containing only real numbers.
    :param nr_list: the list
    :return: the list containing only real numbers
    """
    sol = []

    for el in nr_list:
        if get_imag(el) == 0:
            sol.append(el)

    return sol


def filter_modulo_equal(nr_list, val):
    """
    Return a list containing elements with the modulo equals to a certain value.
    :param nr_list: the list
    :param val: the value
    :return: the new list
    """
    sol = []

    for el in nr_list:
        if modulo(el) == val:
            sol.append(el)

    return sol


def filter_modulo_smaller(nr_list, val):
    """
    Return a list containing elements with the modulo smaller than a certain value.
    :param nr_list: the list
    :param val: the value
    :return: the new list
    """
    sol = []

    for el in nr_list:
        if modulo(el) < val:
            sol.append(el)

    return sol


def filter_modulo_greater(nr_list, val):
    """
    Return a list containing elements with the modulo greater than a certain value.
    :param nr_list: the list
    :param val: the value
    :return: the new list
    """
    sol = []

    for el in nr_list:
        if modulo(el) > val:
            sol.append(el)

    return sol


def check_info_filter(info, nr_list):
    """
    Check for condition and return a list that fulfill the condition.
    :param info: the necessary info for determining the condition
    :param nr_list: the list
    :return: the solution list
    """
    info = info.strip().lower()

    tokens = info.split()
    for i in range(len(tokens)):
        tokens[i] = tokens[i].strip()

    sol = []

    if "real" in tokens:
        if len(tokens) > 1:
            raise ValueError("Not a correct command for \"filter real\"")
        sol.extend(filter_real(nr_list))
    elif "modulo" in tokens:
        if len(tokens) > 3:
            raise ValueError("Make sure the command is correct")
        if not tokens[2].strip().replace(".", "").isnumeric():
            raise ValueError("Make sure the command is correct")
        if tokens[1] != '<' and tokens[1] != '=' and tokens[1] != '>':
            raise ValueError("Modulo can only test \"<\", \"=\" or \">\"")

        val = float(tokens[2])
        if tokens[1] == '=':
            sol.extend(filter_modulo_equal(nr_list, val))
        elif tokens[1] == '<':
            sol.extend(filter_modulo_smaller(nr_list, val))
        else:
            sol.extend(filter_modulo_greater(nr_list, val))
    else:
        raise ValueError("Make sure you filter some \"real\" or \"modulo\" info")

    if len(sol) == 0:
        raise ValueError("No element matches the condition")

    return sol


def solve_filter(info, my_list):
    """
    Solve filter command.
    """
    nr_list = get_current(my_list)

    if info is None:
        raise ValueError("You need to input more info")
    else:
        new_list = check_info_filter(info, nr_list)
        my_list.append(new_list)


# =============================================================================
