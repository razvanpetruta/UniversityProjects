import datetime

from repository.repoException import RepositoryException


class RentalRepository:
    def __init__(self, validator_class):
        """
        This repository is used for managing the rentals. We will store the rentals as dictionaries,
        key: rental_object, in order to find the rentals by their id.
        :param validator_class: the validator class used to validate the rentals
        """
        self.__validator = validator_class
        self.__rentals = {}

    def find_by_id(self, rental_id):
        """
        Find a rental by its id.
        :param rental_id: the id we are looking for
        :return: the rental object or None
        """
        if rental_id in self.__rentals:
            return self.__rentals[rental_id]
        return None

    def find_book_in_rentals(self, book_id):
        """
        Find a book id which was not returned.
        :param book_id: the book id
        """
        for key in self.__rentals:
            if self.__rentals[key].book_id == book_id and self.__rentals[key].returned_date is None:
                return self.__rentals[key]
        return None

    def rent_date_greater_than_return_date(self, book_id, rented_date):
        """
        Check if the rented data is smaller than the returned date.
        :param book_id: the book id
        :param rented_date: rented date
        """
        for key in self.__rentals:
            if self.__rentals[key].book_id == book_id and self.__rentals[key].returned_date > rented_date:
                return self.__rentals[key]
        return None

    def find_book_with_client(self, book_id, client_id):
        """
        Find in rentals the client id which has the book id associated.
        :param book_id: the book id
        :param client_id: the client id
        """
        for key in self.__rentals:
            if self.__rentals[key].book_id == book_id and self.__rentals[key].client_id == client_id and \
                    self.__rentals[key].returned_date is None:
                return self.__rentals[key]
        return None

    def rent(self, rental):
        """
        Rent an available book
        :param rental: the rental object containing the information.
        """
        self.__validator.validate(rental)
        self.__rentals[rental.rental_id] = rental

    def return_book(self, client_id, book_id, returned_date):
        """
        Return a rented book.
        :param client_id: the client id
        :param book_id: the book id
        :param returned_date: the returned date
        """
        rented_book = self.find_book_with_client(book_id, client_id)
        rented_book.returned_date = returned_date

    def delete_by_id(self, rental_id):
        """
        Delete a rental by its id.
        :param rental_id: the rental id that will be deleted
        """
        if self.find_by_id(rental_id) is None:
            raise RepositoryException("The id of the rental does not exist")
        self.__rentals.pop(rental_id)

    def update_by_id(self, rental_id, book_id, client_id, rented_date, returned_date):
        """
        Update a rental information.
        :param rental_id: rental id
        :param book_id: new book id
        :param client_id: new client id
        :param rented_date: new rented date
        :param returned_date: new returned date
        """
        victim = self.find_by_id(rental_id)
        if victim is None:
            raise RepositoryException("The id of the rental does not exist")
        victim.book_id = book_id
        victim.client_id = client_id
        victim.rented_date = rented_date
        victim.returned_date = returned_date

    def most_rented_books(self):
        """
        Check for each book in rentals repo the number of times they were rented.
        :return: a dictionary of form book_id: number of rentals of that book
        """
        book_rentals = dict()
        for key in self.__rentals:
            book_id = self.__rentals[key].book_id
            if book_id not in book_rentals:
                book_rentals[book_id] = 1
            else:
                book_rentals[book_id] += 1
        return book_rentals

    def most_active_clients(self):
        """
        Check for each client in rentals repo the number of days they hold a book.
        :return: a dictionary of form client_id: number of days of renting
        """
        client_days = dict()
        for key in self.__rentals:
            client_id = self.__rentals[key].client_id
            days = 0
            if self.__rentals[key].returned_date is None:
                days += (datetime.date.today() - self.__rentals[key].rented_date).days
            else:
                days += (self.__rentals[key].returned_date - self.__rentals[key].rented_date).days
            if client_id not in client_days:
                client_days[client_id] = days
            else:
                client_days[client_id] += days
        return client_days

    @property
    def rentals(self):
        return self.__rentals

    def __len__(self):
        return len(self.__rentals)
