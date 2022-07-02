import math


# print available commands
def print_commands():
    """
    Print the possible commands.
    """
    print("Possible commands:")
    print("\tadd <number>")
    print("\tinsert <number> at <position>")
    print("\tremove <position>")
    print("\tremove <start position> to <end position>")
    print("\treplace <old number> with <new number>")
    print("\tlist")
    print("\tlist real <start position> to <end position>")
    print("\tlist modulo [ < or = or > ] <number>")
    print("\texit")


# ==================== TESTS ======================
def test_split_command():
    """
    Run tests for split_command function.
    """
    assert split_command("add 2+3i") == ("add", "2+3i")
    assert split_command("   add 2-3i   ") == ("add", "2-3i")
    assert split_command("ADD   2+3i  ") == ("add", "  2+3i")
    assert split_command("Add") == ("add", None)
    assert split_command("list") == ("list", None)


def test_construct_complex_from_string():
    """
    Run tests for construct_complex_from_string function.
    """
    assert construct_complex_from_string("1+2i") == {"real": 1, "imag": 2}
    assert construct_complex_from_string("-1-2i") == {"real": -1, "imag": -2}
    assert construct_complex_from_string("0") == {"real": 0, "imag": 0}
    assert construct_complex_from_string("-11") == {"real": -11, "imag": 0}
    assert construct_complex_from_string("-12i") == {"real": 0, "imag": -12}
    assert construct_complex_from_string("i") == {"real": 0, "imag": 1}
    assert construct_complex_from_string("1-1i") == {"real": 1, "imag": -1}
    assert construct_complex_from_string("1-i") == {"real": 1, "imag": -1}


def test_modulo():
    """
    Run tests for modulo function.
    """
    assert modulo({"real": 6, "imag": 8}) == 10
    assert modulo({"real": -6, "imag": -8}) == 10
    assert modulo({"real": 0, "imag": 0}) == 0
    assert modulo({"real": 0, "imag": -4}) == 4
    assert modulo({"real": 4, "imag": 0}) == 4
    assert modulo({"real": 1, "imag": -1}) == math.sqrt(2)
    assert modulo({"real": 2, "imag": -3}) == math.sqrt(13)
    assert modulo({"real": 5, "imag": -1}) == math.sqrt(26)


def test_solve_list_real():
    """
    Run tests for solve_list_real.
    """
    assert solve_list_real(0, 4, [
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 0},
        {"real": 1, "imag": 1}
    ]) == [{"real": 4, "imag": 0}, {"real": 2, "imag": 0}, {"real": 4, "imag": 0}]
    assert solve_list_real(0, 4, [
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ]) == [{"real": 2, "imag": 0}]
    assert solve_list_real(0, 4, [
        {"real": 4, "imag": 2},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ]) == []


def test_solve_list_modulo_equal():
    """
    Run tests for solve_list_modulo_equal.
    """
    assert solve_list_modulo_equal([
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}
    ], 4) == [{"real": 4, "imag": 0}, {"real": 0, "imag": -4}]
    assert solve_list_modulo_equal([
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 0, "imag": 0},
        {"real": 1, "imag": 1}
    ], 0) == [{"real": 0, "imag": 0}]
    assert solve_list_modulo_equal([
        {"real": 4, "imag": 2},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ], 10) == []


def test_solve_list_modulo_smaller():
    """
    Run tests for solve_list_modulo_smaller.
    """
    assert solve_list_modulo_smaller([
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}
    ], 100) == [
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}]

    assert solve_list_modulo_equal([
        {"real": 4, "imag": 2},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ], 0) == []


def test_solve_list_modulo_greater():
    """
    Run tests for solve_list_modulo_greater.
    """
    assert solve_list_modulo_greater([
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}
    ], 0) == [
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}]

    assert solve_list_modulo_equal([
        {"real": 4, "imag": 2},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ], 1000) == []


def test_check_info_list():
    """
    Run tests for check_info_list function.
    """
    assert check_info_list("modulo = 4", [
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}
    ]) == [{"real": 4, "imag": 0}, {"real": 0, "imag": -4}]


def test_get_rid_of_spaces():
    """
    Run tests for get_rid_of_spaces function.
    """
    assert get_rid_of_spaces("  1   + 3 i") == "1+3i"
    assert get_rid_of_spaces("insert 1 + 2 i at   2") == "insert1+2iat2"


def run_all_tests():
    """
    Run tests for various functions.
    """
    test_split_command()
    test_construct_complex_from_string()
    test_modulo()
    test_solve_list_real()
    test_solve_list_modulo_equal()
    test_solve_list_modulo_smaller()
    test_solve_list_modulo_greater()
    test_check_info_list()
    test_get_rid_of_spaces()
    print("All tests passed successfully!")


# ==================================================================
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
    nr_list = [
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

    return nr_list


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


def print_el(el):
    """
    Print an element in a+bi form.
    :param el: the element
    """
    real = get_real(el)
    imag = get_imag(el)

    if real == 0 and imag == 0:
        print("0")
    elif imag == 0:
        print(str(real))
    elif real == 0:
        print(str(imag) + "i")
    else:
        print(str(real), end="")
        if imag > 0:
            print("+" + str(imag) + "i")
        else:
            print(str(imag) + "i")


# =========== LIST command ================
def print_list(nr_list):
    """
    Print the list of complex numbers.
    :param nr_list: the list of numbers
    """
    if len(nr_list) == 0:
        print("The list is empty")
        return

    for el in nr_list:
        print_el(el)


def modulo(el):
    """
    Calculate the modulo of an complex number.
    :param el: the complex number
    :return: the modulo of the complex number
    """
    sol = math.sqrt(get_real(el) ** 2 + get_imag(el) ** 2)
    return sol


def solve_list_real(left, right, nr_list):
    """
    Check for real numbers in range of left and right.
    :param left: left margin
    :param right: right margin
    :param nr_list: the list we check
    :return: a list containing the real numbers from the interval
    """
    if right < left:
        print("The interval seems wrong...")
        return

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
        print("We can only list some \"real\" info and \"modulo\" info")
        return

    info = info.strip()
    tokens = info.split()

    for i in range(len(tokens)):
        tokens[i] = tokens[i].strip().lower()

    sol = []

    if "real" in tokens:
        try:
            left = int(tokens[1])
            right = int(tokens[3])
            sol = solve_list_real(left, right, nr_list)
        except ValueError:
            print("Make sure the positions are valid")
            return
        except IndexError:
            print("The positions are wrong or exceed the list")
            return

    elif "modulo" in tokens:
        try:
            val = float(tokens[2])
            if tokens[1] == "=":
                sol = solve_list_modulo_equal(nr_list, val)
            elif tokens[1] == "<":
                sol = solve_list_modulo_smaller(nr_list, val)
            elif tokens[1] == ">":
                sol = solve_list_modulo_greater(nr_list, val)
            else:
                print("The only options are \"<\", \"=\" or \">\"")
                return
        except ValueError:
            print("Make sure that modulo is correct")
            return
        except IndexError:
            print("Make sure that the command is correct")
            return
    else:
        print("Make sure that the command is correct")
        return

    return sol


# ================== build number ==============================
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

    sol = complex(s)
    real = int(sol.real)
    imag = int(sol.imag)

    sol = {
        "real": real,
        "imag": imag
    }

    return sol


# ======================== ADD command ==================
def check_info_add(info, nr_list):
    """
    Add command -> add a number to the list.
    :param info: info containing the number
    :param nr_list: the list in which we add the number
    """
    info = info.strip()
    info = get_rid_of_spaces(info)
    try:
        el = construct_complex_from_string(info)
        nr_list.append(el)
    except ValueError:
        print("Input a correct complex number!")


# ======================= INSERT command ================
def check_info_insert(info, nr_list):
    """
    Insert command -> insert a number to a certain position.
    :param info: info containing the number and the position
    :param nr_list: the list in which we insert the number
    """
    info = info.strip().lower()
    tokens = info.split("at")
    try:
        el = construct_complex_from_string(get_rid_of_spaces(tokens[0]))
        position = int(tokens[1].strip())
        nr_list.insert(position, el)
    except ValueError:
        print("Make sure you inserted a correct complex number and a correct position")
    except IndexError:
        print("Make sure you specified the position")


# ===================== REMOVE command ==================
def remove_index(index, nr_list):
    """
    Remove an index from a list.
    :param index: the index that needs to be removed
    :param nr_list: the list from which we remove the index
    """
    try:
        del nr_list[index]
    except IndexError:
        print("Position is out of range")


def remove_sequence(left, right, nr_list):
    """
    Remove a sequence from a list.
    :param left: the left margin
    :param right: the right margin
    :param nr_list: the list from which we remove
    """
    if right < left or (left < 0 or right < 0) or right >= len(nr_list):
        print("The interval seems wrong...")
        return

    del nr_list[left:right + 1]


def check_info_remove(info, nr_list):
    """
    Remove command -> remove an index or and delimited interval.
    :param info: info containing the index or the interval
    :param nr_list: the list from which we eliminate numbers
    """
    info = info.strip().lower()
    info = get_rid_of_spaces(info)

    if "to" in info:
        try:
            tokens = info.split("to")
            left = int(tokens[0])
            right = int(tokens[1])
            remove_sequence(left, right, nr_list)
        except ValueError:
            print("Make sure you specified the range correctly")
    else:
        try:
            index = int(info)
            remove_index(index, nr_list)
        except ValueError:
            print("Make sure you specified the position correctly")


# ======================= REPLACE command ===================
def replace_value(source, destination, nr_list):
    """
    Replace every appearance of the source value with the destination value from nr_list.
    :param source: the source value
    :param destination: the destination value
    :param nr_list: the list in which we replace the values
    """
    if source not in nr_list:
        print("The element you're trying to replace does not exist")
        return

    for i in range(len(nr_list)):
        if nr_list[i] == source:
            set_real(nr_list[i], get_real(destination))
            set_imag(nr_list[i], get_imag(destination))


def check_info_replace(info, nr_list):
    info = info.strip().lower()
    info = get_rid_of_spaces(info)

    try:
        tokens = info.split("with")
        source = construct_complex_from_string(tokens[0])
        destination = construct_complex_from_string(tokens[1])
        replace_value(source, destination, nr_list)
    except ValueError:
        print("Make sure that your command is correct (correct numbers separated by \"with\")")
    except IndexError:
        print("Make sure you specified the range correctly")


# ==========================================================================================
def solve_list(info, nr_list):
    """
    LIST command.
    """
    if info is None:
        print_list(nr_list)
    else:
        try:
            sol = check_info_list(info, nr_list)
            print_list(sol)
        except TypeError:
            print("Try again!")


def solve_add(info, nr_list):
    """
    ADD command.
    """
    if info is None:
        print("You need to enter a number in order to add")
    else:
        check_info_add(info, nr_list)


def solve_insert(info, nr_list):
    """
    INSERT command.
    """
    if info is None:
        print("You need to enter a number in order to insert")
    else:
        check_info_insert(info, nr_list)


def solve_remove(info, nr_list):
    """
    REMOVE command.
    """
    if info is None:
        print("You need to enter an index in order to remove")
    else:
        check_info_remove(info, nr_list)


def solve_replace(info, nr_list):
    """
    REPLACE command.
    """
    if info is None:
        print("You need to input the value you want to replace and the value you replace with")
    else:
        check_info_replace(info, nr_list)


def run():
    """
    Run function.
    """
    nr_list = init_list()
    commands = {
        "list": solve_list,
        "add": solve_add,
        "insert": solve_insert,
        "remove": solve_remove,
        "replace": solve_replace
    }

    print_commands()

    while True:
        user_info = input("command: ")
        operation, info = split_command(user_info)

        if operation == "exit":
            print("Exiting...")
            return

        try:
            commands[operation](info, nr_list)
        except KeyError as ke:
            print("Command is not recognised", ke)


if __name__ == "__main__":
    # run_all_tests()
    run()
