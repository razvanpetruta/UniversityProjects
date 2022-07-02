import random
from src.domain.book_entity import Book


class Generator:
    # we have a private list of isbns
    __list_of_isbns = []

    @staticmethod
    def random_isbn():
        """
        Generate a random ISBN.
        :return: generated string
        """
        # ISBN = International Standard Book Number: xxx-x-xx-xxxxxx-x
        isbn = ""
        for i in range(3):
            c = str(random.randint(0, 9))
            isbn += c
        isbn += '-'
        isbn += str(random.randint(0, 9))
        isbn += '-'
        for i in range(2):
            c = str(random.randint(0, 9))
            isbn += c
        isbn += '-'
        for i in range(6):
            c = str(random.randint(0, 9))
            isbn += c
        isbn += '-'
        isbn += str(random.randint(0, 9))
        return isbn

    @staticmethod
    def generate_random_isbn():
        """
        Generate the isbn until it is unique.
        :return: isbn
        """
        isbn = Generator.random_isbn()
        while isbn in Generator.__list_of_isbns:
            isbn = Generator.random_isbn()
        return isbn

    @staticmethod
    def generate_random_title():
        """
        Generate a random title of a book
        :return: the title
        """
        first = ["Flirty", "Sultry", "Kinky", "Lusty", "Curvy", "Steamy", "Crazy", "Fleshy", "Happy"]
        second = ["Zombies", "Mummies", "Ghosts", "Demons", "Angels", "Sirens", "Dogs", "Cats", "Reapers"]
        title = ""
        title += random.choice(first)
        title += ' '
        title += random.choice(second)
        return title

    @staticmethod
    def generate_random_author():
        """
        Generate a random author of a book.
        :return: the author
        """
        first = ["Ana", "Martin", "John", "Aura", "Daniel", "Bob", "Arnold", "James", "Chloe", "Lucifer"]
        second = ["Morningstar", "Decker", "Smith", "Johnson", "Brown", "Miller", "Davis", "Lee", "Lopez"]
        author = ""
        author += random.choice(first)
        author += ' '
        author += random.choice(second)
        return author

    @staticmethod
    def generate_random_book():
        """
        Generate a random book.
        :return: random book
        """
        isbn = Generator.generate_random_isbn()
        Generator.__list_of_isbns.append(isbn)
        title = Generator.generate_random_title()
        author = Generator.generate_random_author()
        new_book = Book(isbn, title, author)
        return new_book

    @staticmethod
    def generate_entries():
        """
        Generate 10 random entries.
        :return: the entries
        """
        entries = []
        for i in range(10):
            new_book = Generator.generate_random_book()
            entries.append(new_book)
        return entries


class BookService:
    def __init__(self, stage, validator):
        """
        Initialise the book services for a certain stage.
        :param stage: the stage
        :param validator: validator for books
        """
        self.__stage = stage
        self.__validator = validator

    @property
    def stage(self):
        """
        Getter method.
        :return: the stage of the process
        """
        return self.__stage

    @stage.setter
    def stage(self, value):
        """
        Setter method.
        :param value: the new stage to be set
        """
        self.__stage = value

    def add_book(self, isbn, title, author):
        """
        Add a new book to the current stage.
        :param isbn: the isbn of the book
        :param title: the title of the book
        :param author: the author of the book
        """
        new_book = Book(isbn, title, author)
        self.__validator.validate(new_book)
        self.__stage.append(new_book)

    def remove_title(self, word):
        """
        Remove the books that have the title starting with a certain word.
        :param word: the word
        """
        copy = self.__stage[:]
        for book in copy:
            if book.title.lower().strip().startswith(word):
                self.__stage.remove(book)
        if len(copy) == len(self.__stage):
            raise ValueError("No title starts with \"" + word + "\"")
