from domain.bookClass import Book
from repository.repoException import RepositoryException


class BookRepository:
    def __init__(self, validator_class):
        """
        This repository is used for managing the books. We will store the books as dictionaries,
        key: book_object, in order to find the books by their id.
        :param validator_class: the validator class used to validate the books
        """
        self.__validator = validator_class
        self.__books = {}

    def find_by_id(self, book_id):
        """
        Look of in the books for the one with the id = book_id.
        :param book_id: the id we are looking for
        :return: the object or None (if we did not find the id)
        """
        if book_id in self.__books:
            return self.__books[book_id]
        return None

    def find_by_title(self, info):
        """
        Search books by title from a given info.
        :param info: the partial info
        :return: a list of matching books
        """
        if info == "":
            raise RepositoryException("Please provide relevant information")
        full_matchings = []
        start_with_matchings = []
        other_matchings = []
        for _id in self.__books:
            if self.__books[_id].title.lower() == info.lower():
                full_matchings.append(self.__books[_id])
            elif self.__books[_id].title.lower().startswith(info.lower()):
                start_with_matchings.append(self.__books[_id])
            elif info.lower() in self.__books[_id].title.lower():
                other_matchings.append(self.__books[_id])
        matchings = []
        matchings.extend(full_matchings)
        matchings.extend(start_with_matchings)
        matchings.extend(other_matchings)
        return matchings

    def find_by_author(self, info):
        """
        Search books by author from a given info.
        :param info: the info containing the name.
        :return: the list of matchings
        """
        if info == "":
            raise RepositoryException("Please provide relevant information")
        full_matchings = []
        start_with_matchings = []
        other_matchings = []
        for _id in self.__books:
            if self.__books[_id].author.lower() == info.lower():
                full_matchings.append(self.__books[_id])
            elif self.__books[_id].author.lower().startswith(info.lower()):
                start_with_matchings.append(self.__books[_id])
            elif info.lower() in self.__books[_id].author.lower():
                other_matchings.append(self.__books[_id])
        matchings = []
        matchings.extend(full_matchings)
        matchings.extend(start_with_matchings)
        matchings.extend(other_matchings)
        return matchings

    def save(self, book):
        """
        Save a book to the repository, if it doesn't already exists.
        :param book: the book object
        """
        self.__validator.validate(book)
        if self.find_by_id(book.book_id) is not None:
            raise RepositoryException(f"duplicate id {book.book_id}")
        self.__books[book.book_id] = book

    def delete_by_id(self, book_id):
        """
        Delete a book by its id.
        :param book_id: the id that should be deleted
        """
        if self.find_by_id(book_id) is None:
            raise RepositoryException("The id of the book does not exist")
        return self.__books.pop(book_id)

    def update_by_id(self, book_id, new_title, new_author):
        """
        Update a book information by its id.
        :param book_id: the id of the book that will be updated
        :param new_title: the new title
        :param new_author: the new author
        """
        victim = self.find_by_id(book_id)
        if victim is None:
            raise RepositoryException("The id of the book does not exist")
        old_title = victim.title
        old_author = victim.author
        victim.title = new_title
        victim.author = new_author
        return Book(victim.book_id, old_title, old_author)

    @property
    def books(self):
        return self.__books

    def __len__(self):
        return len(self.__books)
