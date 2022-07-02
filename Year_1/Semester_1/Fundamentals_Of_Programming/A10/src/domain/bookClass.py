class Book:
    def __init__(self, book_id, title, author):
        """
        Initialise a book object.
        :param book_id: id of the book, it's unique
        :param title: title of the book
        :param author: author of the book
        """
        self.__book_id = book_id
        self.__title = title
        self.__author = author

    @property
    def book_id(self):
        return self.__book_id

    @book_id.setter
    def book_id(self, value):
        self.__book_id = value

    @property
    def title(self):
        return self.__title

    @title.setter
    def title(self, value):
        self.__title = value

    @property
    def author(self):
        return self.__author

    @author.setter
    def author(self, value):
        self.__author = value

    def __str__(self):
        return f"\tid: {self.book_id}, title: {self.title}, author: {self.author}"
