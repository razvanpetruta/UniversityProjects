from src.domain.validators import BookValidatorException
from src.services.services import Generator
from termcolor import colored


class InputOutput:
    @staticmethod
    def print_options():
        """
        Print the possible commands.
        """
        print("\n1. Add a book")
        print("2. Show the books")
        print("3. Remove titles that start with a given word")
        print("4. Undo the last operation")
        print("5. Exit\n")

    @staticmethod
    def input_option():
        """
        Read the option from the user.
        :return: the option
        """
        option = input("option = ")
        if not option.isnumeric():
            raise ValueError("The option is not valid")
        option = int(option)
        if not (option in [1, 2, 3, 4, 5]):
            raise ValueError("The option is not valid")
        return int(option)

    @staticmethod
    def print_book(my_book):
        """
        Print book's information in a nice way.
        :param my_book: the book
        """
        print(my_book.isbn + " " + "\"" + my_book.title + "\" written by " + my_book.author)

    @staticmethod
    def print_books(books):
        """
        Print all the books.
        :param books: the list of books
        """
        if len(books) == 0:
            raise ValueError("The list is empty")
        for book in books:
            InputOutput.print_book(book)

    @staticmethod
    def input_title():
        """
        Read the title of a book from the user.
        :return: the title
        """
        title = input("\ttitle: ")
        title = title.strip()
        return title

    @staticmethod
    def input_author():
        """
        Read the author of a book from the user.
        :return: the author
        """
        author = input("\tauthor: ")
        author = author.strip()
        return author

    @staticmethod
    def input_book():
        """
        Read the book's information from the user and return a new book.
        :return: the new book
        """
        print("Enter the information about the book")
        print("\tisbn will be generated automatically")
        isbn = Generator.generate_random_isbn()
        title = InputOutput.input_title()
        author = InputOutput.input_author()
        return isbn, title, author

    @staticmethod
    def input_remove_info():
        info = input("remove title starting with: ")
        info = info.strip().lower()
        if len(info) == 0:
            raise ValueError("The title cannot be empty")
        return info


class Console:
    def __init__(self, initial_entry, book_service, io_service):
        """
        Initialise the console.
        :param initial_entry: the 10 randomly generated entries which will
        be added to the list that contains all the states/stages of the program
        :param book_service: services that work with a current stage
        :param io_service: services for input and output
        """
        self.__book_service = book_service
        self.__io_service = io_service
        self.__states = [initial_entry[:]]

    def run(self):
        while True:
            try:
                self.__io_service.print_options()
                option = self.__io_service.input_option()

                # EXIT option
                if option == 5:
                    print(colored("Exiting...", "blue"))
                    return

                # ADD option
                elif option == 1:
                    isbn, title, author = self.__io_service.input_book()
                    self.__book_service.add_book(isbn, title, author)
                    copy = self.__book_service.stage[:]
                    self.__states.append(copy)
                    print(colored("book added", "blue"))

                # DISPLAY option
                elif option == 2:
                    self.__io_service.print_books(self.__states[-1])

                # REMOVE option
                elif option == 3:
                    info = self.__io_service.input_remove_info()
                    self.__book_service.remove_title(info)
                    copy = self.__book_service.stage[:]
                    self.__states.append(copy)
                    print(colored("titles removed", "blue"))

                # UNDO option
                else:
                    if len(self.__states) <= 1:
                        raise ValueError("Cannot undo anymore, you are at first state")
                    del self.__states[-1]
                    self.__book_service.stage = self.__states[-1][:]
                    print(colored("undo done", "blue"))

            except ValueError as ve:
                print(colored(str(ve), "yellow"))
            except BookValidatorException as bve:
                print(colored(str(bve), "yellow"))
