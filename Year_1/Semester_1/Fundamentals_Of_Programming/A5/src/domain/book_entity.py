class Book:
    def __init__(self, isbn, title, author):
        """
        Initialise a book.
        :param isbn: string, unique
        :param title: string
        :param author: string
        """
        self.__isbn = isbn
        self.__author = author
        self.__title = title

    @property
    def isbn(self):
        """
        Getter method.
        :return: the isbn
        """
        return self.__isbn

    @isbn.setter
    def isbn(self, value):
        """
        Setter method
        :param value: the value to be set for the isbn
        """
        self.__isbn = value

    @property
    def title(self):
        """
        Getter method.
        :return: the title
        """
        return self.__title

    @title.setter
    def title(self, value):
        """
        Setter method
        :param value: the value to be set for the title
        """
        self.__title = value

    @property
    def author(self):
        """
        Getter method.
        :return: the author
        """
        return self.__author

    @author.setter
    def author(self, value):
        """
        Setter method
        :param value: the value to be set for the title
        """
        self.__author = value
