from enum import Enum

from services.redo import RedoManager
from services.undo import UndoManager


def add_book_handler_undo(book_service, book_id):
    """
    The inverse operation for adding a book is removing it.
    :param book_service: the service we operate on
    :param book_id: the book id we need in order to delete a the book added
    """
    victim = book_service.search_by_id(book_id)
    RedoManager.register_operation(book_service, RedoHandlers.REMOVE_BOOK, (victim.book_id, victim.title,
                                                                            victim.author))
    book_service.remove(book_id)
    UndoManager.delete_last_register()


def remove_book_handler_undo(book_service, book_id, book_title, book_author):
    """
    The inverse operation for removing a book is adding it.
    :param book_service: the service
    :param book_id: the book id
    :param book_title: the book title
    :param book_author: the book author
    """
    book_service.add(book_id, book_title, book_author)
    RedoManager.register_operation(book_service, RedoHandlers.ADD_BOOK, (book_id, ))
    UndoManager.delete_last_register()


def update_book_handler_undo(book_service, book_id, book_title, book_author):
    """
    The inverse operation for updating a book is updating it again with the old fields.
    :param book_service: the service
    :param book_id: the book id
    :param book_title: the book title
    :param book_author: the book author
    """
    victim = book_service.search_by_id(book_id)
    RedoManager.register_operation(book_service, RedoHandlers.UPDATE_BOOK, (book_id, victim.title, victim.author))
    book_service.update(book_id, book_title, book_author)
    UndoManager.delete_last_register()


def add_client_handler_undo(client_service, client_id):
    """
    The inverse operation for removing a client is deleting a client.
    :param client_service: the service
    :param client_id: the client id that will be removed
    """
    victim = client_service.search_by_id(client_id)
    RedoManager.register_operation(client_service, RedoHandlers.REMOVE_CLIENT, (client_id, victim.name))
    client_service.remove(client_id)
    UndoManager.delete_last_register()


def remove_client_handler_undo(client_service, client_id, client_name):
    """
    The inverse operation for removing a client is adding it again.
    :param client_service: the service
    :param client_id: the client id
    :param client_name: the client name
    """
    client_service.add(client_id, client_name)
    RedoManager.register_operation(client_service, RedoHandlers.ADD_CLIENT, (client_id, ))
    UndoManager.delete_last_register()


def update_client_handler_undo(client_service, client_id, client_name):
    """
    The inverse operation for updating a client is updating it with the old fields.
    :param client_service: the service
    :param client_id: the client id
    :param client_name: the client name
    """
    victim = client_service.search_by_id(client_id)
    RedoManager.register_operation(client_service, RedoHandlers.UPDATE_CLIENT, (client_id, victim.name))
    client_service.update(client_id, client_name)
    UndoManager.delete_last_register()


def rent_a_book_handler_undo(rental_service, rental_id):
    """
    The inverse operation for renting a book is removing the id of the rental from the rentals.
    :param rental_service: the service
    :param rental_id: the rental id
    """
    victim = rental_service.search_by_id(rental_id)
    RedoManager.register_operation(rental_service, RedoHandlers.RENT_BOOK, (victim.rental_id, victim.book_id,
                                                                            victim.client_id, victim.rented_date.day,
                                                                            victim.rented_date.month,
                                                                            victim.rented_date.year))
    rental_service.remove_rental(rental_id)


def return_a_book_handler_undo(rental_service, rental_id, book_id, client_id, rented_date):
    """
    The inverse operation for renting a book is setting the returned date to None.
    :param rental_service: the service
    :param rental_id: rental id
    :param book_id: book id
    :param client_id: client id
    :param rented_date: rented book
    """
    old_rental = rental_service.search_by_id(rental_id)
    RedoManager.register_operation(rental_service, RedoHandlers.RETURN_BOOK, (old_rental.client_id,
                                                                              old_rental.book_id,
                                                                              old_rental.returned_date.day,
                                                                              old_rental.returned_date.month,
                                                                              old_rental.returned_date.year))
    rental_service.inverse_return_a_book(rental_id, book_id, client_id, rented_date)


def remove_book_handler_redo(book_service, book_id, title, author):
    """
    Redo the remove from the undo function.
    :param book_service: the service
    :param book_id: the book id
    :param title: the book title
    :param author: the book author
    """
    book_service.add(book_id, title, author)


def add_book_handler_redo(book_service, book_id):
    """
    Redo the add from the undo function.
    :param book_service: the service
    :param book_id: the book id
    """
    book_service.remove(book_id)


def update_book_handler_redo(book_service, book_id, title, author):
    """
    Redo the update from the undo function.
    :param book_service: the service
    :param book_id: the book id
    :param title: the book title
    :param author: the book author
    """
    book_service.update(book_id, title, author)


def remove_client_handler_redo(client_service, client_id, client_name):
    """
    Redo the remove from the undo function.
    :param client_service: the service
    :param client_id: the client id
    :param client_name: the client name
    """
    client_service.add(client_id, client_name)


def add_client_handler_redo(client_service, client_id):
    """
    Redo the add from the undo function.
    :param client_service: the service
    :param client_id: the client id
    """
    client_service.remove(client_id)


def update_client_handler_redo(client_service, client_id, client_name):
    """
    Redo the update from the undo function.
    :param client_service: the service
    :param client_id: the client id
    :param client_name: the client name
    """
    client_service.update(client_id, client_name)


def rent_a_book_handler_redo(rental_service, rental_id, book_id, client_id, day, month, year):
    """
    Redo the rent book function.
    :param rental_service: the service
    :param rental_id: the rental id
    :param book_id: the book id
    :param client_id: the client id
    :param day: day of rental
    :param month: month of rental
    :param year: year of rental
    """
    rental_service.rent_a_book(rental_id, book_id, client_id, day, month, year)


def return_a_book_handler_redo(rental_service, client_id, book_id, day, month, year):
    """
    Redo the return book function.
    :param rental_service: the service
    :param client_id: the client id
    :param book_id: the book id
    :param day: the day of the returned date
    :param month: the month
    :param year: the year
    """
    rental_service.return_a_book(client_id, book_id, day, month, year)


class UndoHandlers(Enum):
    """
    Keep track of the available undo handlers.
    """
    ADD_BOOK = add_book_handler_undo
    REMOVE_BOOK = remove_book_handler_undo
    UPDATE_BOOK = update_book_handler_undo
    ADD_CLIENT = add_client_handler_undo
    REMOVE_CLIENT = remove_client_handler_undo
    UPDATE_CLIENT = update_client_handler_undo
    RENT_BOOK = rent_a_book_handler_undo
    RETURN_BOOK = return_a_book_handler_undo


class RedoHandlers(Enum):
    """
    Keep track of the available redo handlers.
    """
    REMOVE_BOOK = remove_book_handler_redo
    ADD_BOOK = add_book_handler_redo
    UPDATE_BOOK = update_book_handler_redo
    REMOVE_CLIENT = remove_client_handler_redo
    ADD_CLIENT = add_client_handler_redo
    UPDATE_CLIENT = update_client_handler_redo
    RENT_BOOK = rent_a_book_handler_redo
    RETURN_BOOK = return_a_book_handler_redo
