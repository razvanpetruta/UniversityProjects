"""
    Module containing UI functions.
"""


# ============================= MODULES =============================
import functions as f


# ============================= UI functions ========================
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
    print("\tsum <start position> to <end position>")
    print("\tproduct <start position> to <end position>")
    print("\tfilter real")
    print("\tfilter modulo [ < | = | > ] <number>")
    print("\tundo")
    print("\texit")


def get_user_info():
    """
    Get the user information.
    :return: the info as a string
    """
    user_info = input("command: ")
    return user_info


def print_exit_message():
    print("Exiting...")


def print_el(el):
    """
    Print an element in a+bi form.
    :param el: the element
    """
    real = f.get_real(el)
    imag = f.get_imag(el)

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


def print_stages(my_list):
    """
    Print all the stages from my_list
    :param my_list: the list that contains the stages
    """
    for stage in my_list:
        print(stage)

