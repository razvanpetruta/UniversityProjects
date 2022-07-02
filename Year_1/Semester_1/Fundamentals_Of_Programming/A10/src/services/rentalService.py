import datetime
from domain.rentalClass import Rental
from domain.validators import RentalValidator, LibraryException
from services.handlers import UndoHandlers
from services.undo import UndoManager

from src.algorithmsAndStructures.sortingAlgorithm.sorting import Sorting


class RentalServiceException(LibraryException):
    pass


class RentalService:
    def __init__(self, rental_repo, book_repo, client_repo):
        """
        The functions used to manage the rentals.
        :param rental_repo: rental repository
        :param book_repo: book repository
        :param client_repo: client repository
        """
        self.__rental_repo = rental_repo
        self.__book_repo = book_repo
        self.__client_repo = client_repo

    def search_by_id(self, rental_id):
        rental = self.__rental_repo.find_by_id(rental_id)
        if rental is None:
            raise RentalServiceException(f"the rental id: {rental_id} does not exist")
        return rental

    def rent_a_book(self, rental_id, book_id, client_id, day, month, year):
        """
        Rent a book.
        :param rental_id: the rental id
        :param book_id: the book id
        :param client_id: the client id
        :param day: the day in which the rental was made
        :param month: the month
        :param year: the year
        """
        RentalValidator.validate_date(day, month, year)
        rented_date = datetime.date(year, month, day)
        if rented_date > datetime.date.today():
            raise RentalServiceException("The rented date must be smaller than the current date")
        new_rental = Rental(rental_id, book_id, client_id, rented_date, None)
        # Check if the book exists in book repository
        if self.__book_repo.find_by_id(new_rental.book_id) is None:
            raise RentalServiceException(f"we don't have the book with id: {new_rental.book_id}")
        # Check if the client exists in client repository
        if self.__client_repo.find_by_id(new_rental.client_id) is None:
            raise RentalServiceException(f"the client with the id: {new_rental.client_id} doesn't exist")
        # Check for duplicate rental id
        if self.__rental_repo.find_by_id(new_rental.rental_id) is not None:
            raise RentalServiceException(f"duplicate rental id {new_rental.rental_id}")
        # Check if the book is not already rented by other person
        if self.__rental_repo.find_book_in_rentals(new_rental.book_id) is not None:
            raise RentalServiceException(f"the book with id: {new_rental.book_id} is already rented by a customer")
        # Check if the date in which the new rent is made is greater than the returned date
        found = self.__rental_repo.rent_date_greater_than_return_date(new_rental.book_id, new_rental.rented_date)
        if found is not None:
            raise RentalServiceException(f"The book with id: {new_rental.book_id} was returned in {found.returned_date}, "
                                         f"cannot rent it in {new_rental.rented_date}")
        self.__rental_repo.rent(new_rental)
        UndoManager.register_operation(self, UndoHandlers.RENT_BOOK, (new_rental.rental_id, ))

    def return_a_book(self, client_id, book_id, day, month, year):
        """
        Return a book.
        :param client_id: the client id
        :param book_id: the book id
        :param day: the day in which the return is made
        :param month: the month
        :param year: the year
        """
        RentalValidator.validate_date(day, month, year)
        returned_date = datetime.date(year, month, day)
        if returned_date > datetime.date.today():
            raise RentalServiceException("The returned date must be smaller than the current date")
        # Check if the book was rented in order to return it
        rented_book = self.__rental_repo.find_book_with_client(book_id, client_id)
        if rented_book is None:
            raise RentalServiceException(f"The client with id: {client_id} didn't rent that book in order to return it")
        # The returned date must be after rented date
        if returned_date < rented_book.rented_date:
            raise RentalServiceException("Cannot return before renting")
        self.__rental_repo.return_book(client_id, book_id, returned_date)
        UndoManager.register_operation(self, UndoHandlers.RETURN_BOOK, (rented_book.rental_id, rented_book.book_id,
                                                                        rented_book.client_id, rented_book.rented_date))

    def inverse_return_a_book(self, rental_id, book_id, client_id, rented_date):
        """
        Set the return date of a rental to None.
        :param rented_date: the rented date
        :param rental_id: the rental id
        :param book_id: the book id
        :param client_id: the client id
        """
        self.__rental_repo.update_by_id(rental_id, book_id, client_id, rented_date, None)

    def remove_rental(self, rental_id):
        """
        Remove a rental by its id.
        """
        self.__rental_repo.delete_by_id(rental_id)

    def list_of_rentals(self):
        """
        Return a list of the rentals.
        :return: a list
        """
        rentals_list = []
        for key in self.__rental_repo.rentals:
            rentals_list.append(self.__rental_repo.rentals[key])
        return rentals_list

    def statistics_books(self):
        """
        Get a list containing the book_ids with associated number of rentals, sorted in descending order.
        :return: a descending list
        """
        def custom_sort(l1, l2):
            return l1[1] >= l2[1]

        books = self.__rental_repo.most_rented_books()
        for key in self.__book_repo.books:
            if key not in books:
                books[key] = 0
        books_list = list(books.items())
        Sorting.sort(books_list, custom_sort)
        return books_list

    def statistics_authors(self):
        """
        Get a list containing the authors with associated number of rentals, sorted in descending order.
        :return: a descending list
        """
        def custom_sort(l1, l2):
            return l1[1] >= l2[1]

        books = self.__rental_repo.most_rented_books()
        for key in self.__book_repo.books:
            if key not in books:
                books[key] = 0
        authors = dict()
        for _id in books:
            author = self.__book_repo.find_by_id(_id).author
            if author not in authors:
                authors[author] = books[_id]
            else:
                authors[author] += books[_id]
        authors_list = list(authors.items())
        Sorting.sort(authors_list, custom_sort)
        return authors_list

    def statistics_clients(self):
        """
        Get a list containing the clients with associated number of days of renting, sorted in descending order.
        :return: a descending list
        """
        def custom_sort(l1, l2):
            return l1[1] >= l2[1]

        clients = self.__rental_repo.most_active_clients()
        for key in self.__client_repo.clients:
            if key not in clients:
                clients[key] = 0
        clients_list = list(clients.items())
        Sorting.sort(clients_list, custom_sort)
        return clients_list
