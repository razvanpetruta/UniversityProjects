"""
    Module containing the main function: run()
"""


# ====================== MODULES ==========================================
import ui
import functions as f
import tests


# =========================================================================


# ============================= RUN function ==============================
def run():
    """
    Run function.
    """
    my_list = f.init_list()
    commands = {
        "list": f.solve_list,
        "add": f.solve_add,
        "replace": f.solve_replace,
        "remove": f.solve_remove,
        "insert": f.solve_insert,
        "undo": f.solve_undo,
        "sum": f.solve_sum,
        "product": f.solve_product,
        "filter": f.solve_filter
    }

    tests.run_all_tests()

    ui.print_commands()

    while True:
        user_info = ui.get_user_info()
        operation, info = f.split_command(user_info)

        if operation == "exit":
            ui.print_exit_message()
            return

        try:
            commands[operation](info, my_list)
        except KeyError as ke:
            print(str(ke) + " is not a valid command")
        except ValueError as ve:
            print(str(ve))
        except IndexError as ie:
            print(str(ie))


if __name__ == "__main__":
    run()
