class IO:
    @staticmethod
    def print_menu():
        """
        Print the options for the manager.
        """
        print("\nMANAGER:")
        print("\t1. Manage books")
        print("\t2. Manage clients")
        print("\t3. Manage rentals")
        print("\ttype 'undo' anytime to reverse the last operation")
        print("\ttype 'redo' anytime to reverse the last undo")
        print("\ttype 'exit' to end the program...\n")

    @staticmethod
    def print_manage_books_options():
        """
        Print the options for managing the books.
        """
        print("\ta. add a book")
        print("\tb. update a book")
        print("\tc. remove a book")
        print("\td. show available books")
        print("\te. search book by id")
        print("\tf. search book by title")
        print("\tg. search book by author")
        print("\tback = go to manager\n")

    @staticmethod
    def print_manage_clients_options():
        """
        Print the options for managing the clients.
        """
        print("\ta. add a client")
        print("\tb. update a client")
        print("\tc. remove a client")
        print("\td. show available clients")
        print("\te. search client by id")
        print("\tf. search client by name")
        print("\tback = go to MANAGER\n")

    @staticmethod
    def print_manage_rentals_options():
        print("\ta. rent a book")
        print("\tb. return a book")
        print("\tc. show rentals")
        print("\td. show most rented books")
        print("\te. show most rented authors")
        print("\tf. show most active clients")
        print("\tback = go to MANAGER\n")

    @staticmethod
    def read_manage_rentals_option():
        option = input("\toperation: ")
        option = option.strip().lower()
        if option not in ['a', 'b', 'c', 'd', 'e', 'f', 'back', 'undo', 'redo']:
            raise ValueError("Not a correct operation")
        return option

    @staticmethod
    def read_manage_books_option():
        """
        Read the operation for managing the books.
        """
        option = input("\toperation: ")
        option = option.strip().lower()
        if option not in ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'back', 'undo', 'redo']:
            raise ValueError("Not a correct operation")
        return option

    @staticmethod
    def read_manage_clients_option():
        """
        Read the operation for managing the clients.
        """
        option = input("\toperation: ")
        option = option.strip().lower()
        if option not in ['a', 'b', 'c', 'd', 'e', 'f', 'back', 'undo', 'redo']:
            raise ValueError("Not a correct operation")
        return option

    @staticmethod
    def read_menu_option():
        """
        Read the option from the manager.
        """
        option = input("option: ")
        option = option.strip().lower()
        if option == "exit" or option == "undo" or option == "redo":
            return option
        if not option.isnumeric():
            raise ValueError("The option must be an integer")
        option = int(option)
        if option not in [1, 2, 3]:
            raise ValueError("Not a correct option")
        return option

    @staticmethod
    def read_id():
        """
        Read an id.
        """
        _id = input("\tid: ")
        _id = _id.strip()
        if not _id.isnumeric():
            raise ValueError("Id must be an integer")
        return int(_id)

    @staticmethod
    def read_title():
        """
        Read a title.
        """
        title = input("\ttitle: ")
        return title.strip()

    @staticmethod
    def read_author():
        """
        Read an author.
        """
        author = input("\tauthor: ")
        return author.strip()

    @staticmethod
    def read_name():
        """
        Read a name.
        """
        name = input("\tname: ")
        return name.strip()

    @staticmethod
    def read_day():
        day = input("\tday: ")
        if not day.isnumeric():
            raise ValueError("day must be an integer")
        return int(day)

    @staticmethod
    def read_month():
        month = input("\tmonth: ")
        if not month.isnumeric():
            raise ValueError("month must be an integer")
        return int(month)

    @staticmethod
    def read_year():
        year = input("\tyear: ")
        if not year.isnumeric():
            raise ValueError("year must be an integer")
        return int(year)
