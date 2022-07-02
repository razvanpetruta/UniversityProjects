from domain.bookClass import Book
from domain.validators import LibraryException
from services.generators import Generator
from services.handlers import UndoHandlers
from services.undo import UndoManager


class BookServiceException(LibraryException):
    pass


class BookService:
    def __init__(self, repo):
        """
        The functions used to modify the repository.
        :param repo: the repository of books
        """
        self.__repo = repo

    def search_by_id(self, book_id):
        """
        Search a book by its id.
        :param book_id: the id we are looking for
        :return: the book object
        """
        book = self.__repo.find_by_id(book_id)
        if book is None:
            raise BookServiceException(f"the book id: {book_id} does not exist")
        return book

    def search_by_title(self, info):
        """
        Search a book by partial title info.
        :param info: the partial info
        :return: a list with matchings
        """
        matchings_list = self.__repo.find_by_title(info.strip())
        if len(matchings_list) == 0:
            raise BookServiceException("No matchings found")
        return matchings_list

    def search_by_author(self, info):
        """
        Search a book by partial author info.
        :param info: the partial info
        :return: a list with matchings
        """
        matchings_list = self.__repo.find_by_author(info.strip())
        if len(matchings_list) == 0:
            raise BookServiceException("No matchings found")
        return matchings_list

    def add(self, book_id, title, author):
        """
        Create a book object and add it to the repository.
        :param book_id: the book id
        :param title: the book title
        :param author: the author
        """
        new_book = Book(book_id, title, author)
        self.__repo.save(new_book)
        UndoManager.register_operation(self, UndoHandlers.ADD_BOOK, (new_book.book_id, ))

    def update(self, book_id, title, author):
        """
        Update a book in repository.
        :param book_id: the id of the book that needs to be updated
        :param title: the new title
        :param author: the new author
        """
        old_stage = self.__repo.update_by_id(book_id, title, author)
        UndoManager.register_operation(self, UndoHandlers.UPDATE_BOOK, (old_stage.book_id, old_stage.title,
                                                                        old_stage.author))

    def remove(self, book_id):
        """
        Remove a book from the repository.
        :param book_id: the book id that needs to be removed
        """
        deleted_book = self.__repo.delete_by_id(book_id)
        UndoManager.register_operation(self, UndoHandlers.REMOVE_BOOK, (deleted_book.book_id, deleted_book.title,
                                       deleted_book.author))

    def list_of_books(self):
        """
        :return: a list of the books objects
        """
        books_list = []
        for key in self.__repo.books:
            books_list.append(self.__repo.books[key])
        return books_list

    def populate(self):
        """
        Using Generator class, populate the repository with 5 randomly generated entries.
        """
        while len(self.__repo) != 5:
            book_id = Generator.generate_random_id()
            while book_id in self.__repo.books:
                book_id = Generator.generate_random_id()
            title = Generator.generate_random_title()
            author = Generator.generate_random_name()
            self.__repo.save(Book(book_id, title, author))
