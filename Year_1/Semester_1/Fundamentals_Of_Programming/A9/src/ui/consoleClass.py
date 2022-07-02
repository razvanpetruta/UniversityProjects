from services.redo import RedoManager
from services.undo import UndoManager
from ui.InputOutput import IO
from termcolor import colored


class Console:
    def __init__(self, book_service, client_service, rental_service):
        """
        Manage the books, clients.
        :param book_service: the book service
        :param client_service: the client service
        """
        self.__book_service = book_service
        self.__client_service = client_service
        self.__rental_service = rental_service

    # Manage books
    def add_book(self):
        """
        Read the necessary data in order to add a book and perform the addition.
        """
        book_id = IO.read_id()
        title = IO.read_title()
        author = IO.read_author()
        self.__book_service.add(book_id, title, author)
        print(colored("\tbook added...\n", "blue"))
        RedoManager.empty_history()

    def update_book(self):
        """
        Read the necessary data in order to update a book and perform the update.
        """
        book_id = IO.read_id()
        title = IO.read_title()
        author = IO.read_author()
        self.__book_service.update(book_id, title, author)
        print(colored("\tbook updated...\n", "blue"))
        RedoManager.empty_history()

    def remove_book(self):
        """
        Read the necessary data in order to remove a book and perform the removal.
        """
        book_id = IO.read_id()
        self.__book_service.remove(book_id)
        print(colored("\tbook removed...\n", "blue"))
        RedoManager.empty_history()

    def show_books(self):
        """
        Show the books from the repository.
        """
        self.print_books()

    def print_books(self):
        """
        Print the books in a nice way.
        """
        for book in self.__book_service.list_of_books():
            print(book)
        print("")

    def show_book_by_id(self):
        """
        Show a book with a specific id.
        """
        book_id = IO.read_id()
        print(self.__book_service.search_by_id(book_id))
        print("")

    def show_books_by_title(self):
        """
        Show books with a specific title or partial title.
        """
        title = IO.read_title()
        for book in self.__book_service.search_by_title(title):
            print(book)
        print("")

    def show_books_by_author(self):
        """
        Show books with a specific author or partial author info.
        """
        author = IO.read_author()
        for book in self.__book_service.search_by_author(author):
            print(book)
        print("")

    def manage_books(self):
        """
        Manage the books.
        """
        options = {
            'a': self.add_book,
            'b': self.update_book,
            'c': self.remove_book,
            'd': self.show_books,
            'e': self.show_book_by_id,
            'f': self.show_books_by_title,
            'g': self.show_books_by_author,
            'undo': Console.perform_undo,
            'redo': Console.perform_redo
        }
        while True:
            IO.print_manage_books_options()
            option = IO.read_manage_books_option()
            if option == 'back':
                return
            options[option]()
    # ========================================================================

    # Manage clients
    def add_client(self):
        """
        Read the necessary data in order to add a client and perform the addition.
        """
        client_id = IO.read_id()
        name = IO.read_name()
        self.__client_service.add(client_id, name)
        print(colored("\tclient added...\n", "blue"))
        RedoManager.empty_history()

    def update_client(self):
        """
        Read the necessary data in order to update a client and perform the update.
        """
        client_id = IO.read_id()
        name = IO.read_name()
        self.__client_service.update(client_id, name)
        print(colored("\tclient updated...\n", "blue"))
        RedoManager.empty_history()

    def remove_client(self):
        """
        Read the necessary data in order to remove a client and perform the removal.
        """
        client_id = IO.read_id()
        self.__client_service.remove(client_id)
        print(colored("\tclient removed...\n", "blue"))
        RedoManager.empty_history()

    def show_clients(self):
        """
        Show the clients from the repository.
        """
        self.print_clients()

    def print_clients(self):
        """
        Print the clients in a nice way.
        """
        for client in self.__client_service.list_of_clients():
            print(client)
        print("")

    def show_client_by_id(self):
        _id = IO.read_id()
        print(self.__client_service.search_by_id(_id))
        print("")

    def show_clients_by_name(self):
        name = IO.read_name()
        for client in self.__client_service.search_by_name(name):
            print(client)
        print("")

    def manage_clients(self):
        """
        Manage the clients.
        """
        options = {
            'a': self.add_client,
            'b': self.update_client,
            'c': self.remove_client,
            'd': self.show_clients,
            'e': self.show_client_by_id,
            'f': self.show_clients_by_name,
            'undo': Console.perform_undo,
            'redo': Console.perform_redo
        }
        while True:
            IO.print_manage_clients_options()
            option = IO.read_manage_clients_option()
            if option == 'back':
                return
            options[option]()
    # ======================================================================

    # Manage rentals
    def make_a_rental(self):
        """
        Get the necessary information in order to rent a book and perform the task.
        """
        print("\trental", end="")
        rental_id = IO.read_id()
        print("\t  book", end="")
        book_id = IO.read_id()
        print("\tclient", end="")
        client_id = IO.read_id()
        day = IO.read_day()
        month = IO.read_month()
        year = IO.read_year()
        self.__rental_service.rent_a_book(rental_id, book_id, client_id, day, month, year)
        print(colored("\trental made...\n", "blue"))
        RedoManager.empty_history()

    def return_rental(self):
        """
        Get the necessary information in order to return a book and perform the task.
        """
        print("\tclient", end="")
        client_id = IO.read_id()
        print("\t  book", end="")
        book_id = IO.read_id()
        day = IO.read_day()
        month = IO.read_month()
        year = IO.read_year()
        self.__rental_service.return_a_book(client_id, book_id, day, month, year)
        print(colored("\tbook returned...\n", "blue"))
        RedoManager.empty_history()

    def print_rentals(self):
        """
        Print the rentals in a nice way.
        """
        for rental in self.__rental_service.list_of_rentals():
            print(rental, end="")
            if rental.returned_date is None:
                print("-")
            else:
                print(str(rental.returned_date))
        print("")

    def show_rentals(self):
        """
        Show the rentals.
        """
        self.print_rentals()

    def show_most_rented_books(self):
        """
        Show the list of books with the number of rentals they have.
        """
        for book in self.__rental_service.statistics_books():
            print(self.__book_service.search_by_id(book[0]), end="")
            print(f", times rented: {book[1]}")
        print("")

    def show_most_rented_authors(self):
        """
        Show the list of authors with the number of rentals their books have.
        """
        for author in self.__rental_service.statistics_authors():
            print(f"\t{author[0]}, rented times: {author[1]}")
        print("")

    def show_most_active_clients(self):
        """
        Show the list of clients with the number of days they hold the rented books.
        """
        for client in self.__rental_service.statistics_clients():
            print(f"{self.__client_service.search_by_id(client[0])}, active days: {client[1]}")
        print("")

    def manage_rentals(self):
        """
        Manage rentals.
        """
        options = {
            'a': self.make_a_rental,
            'b': self.return_rental,
            'c': self.show_rentals,
            'd': self.show_most_rented_books,
            'e': self.show_most_rented_authors,
            'f': self.show_most_active_clients,
            'undo': Console.perform_undo,
            'redo': Console.perform_redo
        }
        while True:
            IO.print_manage_rentals_options()
            option = IO.read_manage_rentals_option()
            if option == 'back':
                return
            options[option]()

    @staticmethod
    def perform_undo():
        UndoManager.undo()
        print(colored("\tundo performed", "blue"))
        print("")

    @staticmethod
    def perform_redo():
        RedoManager.redo()
        print(colored("\tredo performed", "blue"))
        print("")

    def run(self):
        """
        The main function.
        """
        options = {
            1: self.manage_books,
            2: self.manage_clients,
            3: self.manage_rentals,
            'undo': Console.perform_undo,
            'redo': Console.perform_redo
        }
        # self.__book_service.populate()
        # self.__client_service.populate()
        while True:
            try:
                IO.print_menu()
                option = IO.read_menu_option()
                if option == "exit":
                    return
                options[option]()
            except Exception as e:
                print(colored(str(e), "yellow"))
