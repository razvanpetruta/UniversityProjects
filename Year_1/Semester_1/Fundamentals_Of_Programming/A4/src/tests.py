"""
    Module containing the tests for NON-UI functions.
"""


# =========================== MODULES ==============================
import functions as f
import math


# =========================== TESTS ================================
def test_split_command():
    """
    Run tests for split_command function.
    """
    assert f.split_command("add 2+3i") == ("add", "2+3i")
    assert f.split_command("   add 2-3i   ") == ("add", "2-3i")
    assert f.split_command("ADD   2+3i  ") == ("add", "  2+3i")
    assert f.split_command("Add") == ("add", None)
    assert f.split_command("list") == ("list", None)


def test_construct_complex_from_string():
    """
    Run tests for construct_complex_from_string function.
    """
    assert f.construct_complex_from_string("1+2i") == {"real": 1, "imag": 2}
    assert f.construct_complex_from_string("-1-2i") == {"real": -1, "imag": -2}
    assert f.construct_complex_from_string("0") == {"real": 0, "imag": 0}
    assert f.construct_complex_from_string("-11") == {"real": -11, "imag": 0}
    assert f.construct_complex_from_string("-12i") == {"real": 0, "imag": -12}
    assert f.construct_complex_from_string("i") == {"real": 0, "imag": 1}
    assert f.construct_complex_from_string("1-1i") == {"real": 1, "imag": -1}
    assert f.construct_complex_from_string("1-i") == {"real": 1, "imag": -1}
    assert f.construct_complex_from_string("   1  -  i") == {"real": 1, "imag": -1}
    assert f.construct_complex_from_string("-i") == {"real": 0, "imag": -1}
    assert f.construct_complex_from_string("i") == {"real": 0, "imag": 1}


def test_modulo():
    """
    Run tests for modulo function.
    """
    assert f.modulo({"real": 6, "imag": 8}) == 10
    assert f.modulo({"real": -6, "imag": -8}) == 10
    assert f.modulo({"real": 0, "imag": 0}) == 0
    assert f.modulo({"real": 0, "imag": -4}) == 4
    assert f.modulo({"real": 4, "imag": 0}) == 4
    assert f.modulo({"real": 1, "imag": -1}) == math.sqrt(2)
    assert f.modulo({"real": 2, "imag": -3}) == math.sqrt(13)
    assert f.modulo({"real": 5, "imag": -1}) == math.sqrt(26)


def test_solve_list_real():
    """
    Run tests for solve_list_real.
    """
    assert f.solve_list_real(0, 4, [
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 0},
        {"real": 1, "imag": 1}
    ]) == [{"real": 4, "imag": 0}, {"real": 2, "imag": 0}, {"real": 4, "imag": 0}]
    assert f.solve_list_real(0, 4, [
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 4, "imag": 2},
        {"real": 1, "imag": 1}
    ]) == [{"real": 2, "imag": 0}]
    assert f.solve_list_real(0, 4, [
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
    assert f.solve_list_modulo_equal([
        {"real": 4, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 0},
        {"real": 0, "imag": -4},
        {"real": 1, "imag": 1}
    ], 4) == [{"real": 4, "imag": 0}, {"real": 0, "imag": -4}]
    assert f.solve_list_modulo_equal([
        {"real": 2, "imag": 0},
        {"real": 4, "imag": 4},
        {"real": 2, "imag": 2},
        {"real": 0, "imag": 0},
        {"real": 1, "imag": 1}
    ], 0) == [{"real": 0, "imag": 0}]
    assert f.solve_list_modulo_equal([
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
    assert f.solve_list_modulo_smaller([
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

    assert f.solve_list_modulo_equal([
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
    assert f.solve_list_modulo_greater([
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

    assert f.solve_list_modulo_equal([
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
    assert f.check_info_list("modulo = 4", [
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
    assert f.get_rid_of_spaces("  1   + 3 i") == "1+3i"
    assert f.get_rid_of_spaces("insert 1 + 2 i at   2") == "insert1+2iat2"


def test_equal():
    """
    Run tests for equal function.
    """
    assert f.equal({"real": 1, "imag": 2}, {"real": 1, "imag": 2}) is True
    assert f.equal({"real": 1111, "imag": 2}, {"real": 1111, "imag": 2}) is True
    assert f.equal({"real": 0, "imag": 2}, {"real": 0, "imag": 2}) is True
    assert f.equal({"real": 1, "imag": 2}, {"real": 2, "imag": 2}) is False
    assert f.equal({"real": 0, "imag": 0}, {"real": 0, "imag": 0}) is True
    assert f.equal({"real": -1, "imag": -2}, {"real": -1, "imag": -2}) is True
    assert f.equal({"real": -1, "imag": -2}, {"real": -1, "imag": 2}) is False


def test_get_current():
    """
    Run tests for get_current function.
    """
    assert f.get_current([1, 2, 3]) == 3
    assert f.get_current([1]) == 1
    assert f.get_current([[1, 2, 3], [5, 6]]) == [5, 6]
    assert f.get_current([[1, 2, 3], [2, 2], [3]]) == [3]


def test_check_info_add():
    """
    Run tests for check_info_add function.
    """
    assert f.check_info_add("1", [{"real": 1, "imag": 2}, {"real": 1, "imag": 2}]) == \
           [{"real": 1, "imag": 2}, {"real": 1, "imag": 2}, {"real": 1, "imag": 0}]
    assert f.check_info_add("1 + 3 i", [{"real": 1, "imag": 2}]) == \
           [{"real": 1, "imag": 2}, {"real": 1, "imag": 3}]
    assert f.check_info_add("-i", []) == [{"real": 0, "imag": -1}]


def test_replace_value():
    """
    Run test for replace_value function.
    """
    assert f.replace_value({"real": 1, "imag": 1}, {"real": 0, "imag": 0},
                           [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}]) == \
           [{"real": 0, "imag": 0}, {"real": 2, "imag": 2}]


def test_check_info_replace():
    """
    Run tests for check_info_replace function.
    """
    assert f.check_info_replace("1+1i with 0", [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}]) == \
           [{"real": 0, "imag": 0}, {"real": 2, "imag": 2}]


def test_remove_index():
    """
    Run tests for remove_index function.
    """
    assert f.remove_index(1, [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}]) == \
        [{"real": 1, "imag": 1}]


def test_remove_sequence():
    """
    Run tests for remove_sequence function.
    """
    assert f.remove_sequence(1, 2, [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}, {"real": 2, "imag": 2}]) == \
        [{"real": 1, "imag": 1}]


def test_check_info_remove():
    """
    Run tests for check_info_remove function.
    """
    assert f.check_info_remove("1 to 2", [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}, {"real": 2, "imag": 2}]) == \
           [{"real": 1, "imag": 1}]
    assert f.check_info_remove("1", [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}, {"real": 3, "imag": 3}]) == \
           [{"real": 1, "imag": 1}, {"real": 3, "imag": 3}]


def test_check_info_insert():
    """
    Run tests for check_info_insert function.
    """
    assert f.check_info_insert("1 - i at 1", [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}]) == \
        [{"real": 1, "imag": 1}, {"real": 1, "imag": -1}, {"real": 2, "imag": 2}]


def test_sum_range():
    """
    Run tests for sum_range function.
    """
    assert f.sum_range(0, 3, [
        {"real": 1, "imag": 1},
        {"real": 2, "imag": 2},
        {"real": 3, "imag": 3},
        {"real": 4, "imag": -1}]) == {"real": 10, "imag": 5}


def test_product_range():
    """
    Run tests for product_range function.
    """
    assert f.product_range(0, 2, [{"real": 1, "imag": 1}, {"real": 2, "imag": 2}, {"real": 3, "imag": 4}]) == \
           {"real": -16, "imag": 12}


def test_filter_real():
    """
    Run tests for filter_real function.
    """
    assert f.filter_real([{"real": 1, "imag": 1},
                          {"real": 1, "imag": 0},
                          {"real": 21, "imag": 0},
                          {"real": 1, "imag": 1}]) == [{"real": 1, "imag": 0},
                                                        {"real": 21, "imag": 0}]


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
    test_equal()
    test_get_current()
    test_check_info_add()
    test_replace_value()
    test_check_info_replace()
    test_remove_index()
    test_remove_sequence()
    test_check_info_remove()
    test_check_info_insert()
    test_sum_range()
    test_product_range()
    test_filter_real()
    print("All tests passed successfully!")
