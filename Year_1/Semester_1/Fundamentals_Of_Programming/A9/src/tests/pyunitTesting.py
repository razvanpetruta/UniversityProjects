import datetime
import unittest

from domain.bookClass import Book
from domain.clientClass import Client
from domain.rentalClass import Rental
from domain.validators import BookValidator, ClientValidator, RentalValidator, RentalValidatorException, \
    BookValidatorException
from repository.bookRepository import BookRepository
from repository.clientRepository import ClientRepository
from repository.rentalRepository import RentalRepository
from repository.repoException import RepositoryException
from services.bookService import BookService, BookServiceException
from services.clientService import ClientService, ClientServiceException
from services.redo import RedoManager, RedoException
from services.rentalService import RentalService, RentalServiceException
from services.undo import UndoManager, UndoException
from ui.consoleClass import Console


class EntityTest(unittest.TestCase):
    def test_book(self):
        book = Book(1, "The wall", "Nobody")
        self.assertEqual(book.book_id, 1)
        self.assertEqual(book.title, "The wall")
        self.assertEqual(book.author, "Nobody")
        book.book_id = 2
        self.assertEqual(book.book_id, 2)
        book.title = "Nothing"
        self.assertEqual(book.title, "Nothing")
        book.author = "Anyone"
        self.assertEqual(book.author, "Anyone")
        self.assertEqual(str(book), "\tid: 2, title: Nothing, author: Anyone")

    def test_client(self):
        client = Client(1, "John")
        self.assertEqual(client.client_id, 1)
        self.assertEqual(client.name, "John")
        client.client_id = 2
        self.assertEqual(client.client_id, 2)
        client.name = "Wick"
        self.assertEqual(client.name, "Wick")
        self.assertEqual(str(client), "\tid: 2, name: Wick")

    def test_rental(self):
        rented_date = datetime.date(2020, 5, 4)
        rental = Rental(1, 2, 3, rented_date, None)
        self.assertEqual(rental.rental_id, 1)
        self.assertEqual(rental.book_id, 2)
        self.assertEqual(rental.client_id, 3)
        self.assertEqual(rental.rented_date, rented_date)
        self.assertEqual(rental.returned_date, None)
        rental.rental_id = 2
        self.assertEqual(rental.rental_id, 2)
        rental.book_id = 5
        self.assertEqual(rental.book_id, 5)
        rental.client_id = 7
        self.assertEqual(rental.client_id, 7)
        new_date = datetime.date(2021, 2, 4)
        rental.rented_date = new_date
        self.assertEqual(rental.rented_date, new_date)
        rental.returned_date = new_date
        self.assertEqual(rental.returned_date, new_date)
        self.assertEqual(str(rental), "\tid: 2, client id: 7, book id: 5, rented date: 2021-02-04, returned date: ")


class BookRepositoryTest(unittest.TestCase):
    def setUp(self) -> None:
        self.__repo = BookRepository(BookValidator)

    def tearDown(self) -> None:
        pass

    def test_empty_repo(self):
        self.assertEqual(len(self.__repo), 0)

    def test_add_repo(self):
        self.__repo.save(Book(1, "The wall", "Nobody"))
        self.assertEqual(len(self.__repo), 1)
        with self.assertRaises(RepositoryException):
            self.__repo.save(Book(1, "The love of nobody", "a broken angel"))

    def test_delete_repo(self):
        self.__repo.save(Book(1, "The wall", "Nobody"))
        self.__repo.save(Book(2, "No life", "Arnold"))
        self.assertEqual(len(self.__repo), 2)
        self.__repo.delete_by_id(2)
        self.assertEqual(len(self.__repo), 1)
        with self.assertRaises(RepositoryException):
            self.__repo.delete_by_id(2)

    def test_update_repo(self):
        self.__repo.save(Book(2, "No life", "Arnold"))
        self.assertEqual(len(self.__repo), 1)
        self.__repo.update_by_id(2, "The wall", "Samuel")
        self.assertEqual(self.__repo.find_by_id(2).title, "The wall")
        with self.assertRaises(RepositoryException):
            self.__repo.update_by_id(1, "Nothing", "Marko")

    def test_books_property(self):
        book1 = Book(1, "The wall", "Nobody")
        book2 = Book(2, "No life", "Arnold")
        self.__repo.save(book1)
        self.__repo.save(book2)
        self.assertEqual(self.__repo.books, {1: book1, 2: book2})

    def test_find_by_author(self):
        book1 = Book(1, "The wall", "Nobody")
        self.__repo.save(book1)
        book2 = Book(2, "No life", "Arnold")
        self.__repo.save(book2)
        book3 = Book(3, "The house", "Barnie")
        self.__repo.save(book3)
        book4 = Book(4, "No life", "Arn")
        self.__repo.save(book4)
        self.assertEqual(self.__repo.find_by_author("arn"), [book4, book2, book3])
        with self.assertRaises(RepositoryException):
            self.__repo.find_by_author("")

    def test_find_by_title(self):
        book1 = Book(1, "The wall", "Nobody")
        self.__repo.save(book1)
        book2 = Book(2, "The", "Arnold")
        self.__repo.save(book2)
        book3 = Book(3, "Over the Bridge", "Barnie")
        self.__repo.save(book3)
        book4 = Book(4, "No life", "Arn")
        self.__repo.save(book4)
        self.assertEqual(self.__repo.find_by_title("the"), [book2, book1, book3])
        with self.assertRaises(RepositoryException):
            self.__repo.find_by_title("")


class ClientRepositoryTest(unittest.TestCase):
    def setUp(self) -> None:
        self.__repo = ClientRepository(ClientValidator)

    def tearDown(self) -> None:
        pass

    def test_empty_repo(self):
        self.assertEqual(len(self.__repo), 0)

    def test_add_repo(self):
        self.__repo.save(Client(1, "John"))
        self.assertEqual(len(self.__repo), 1)
        with self.assertRaises(RepositoryException):
            self.__repo.save(Client(1, "Mike"))

    def test_delete_repo(self):
        self.__repo.save(Client(1, "John"))
        self.__repo.save(Client(2, "Wick"))
        self.assertEqual(len(self.__repo), 2)
        self.__repo.delete_by_id(2)
        self.assertEqual(len(self.__repo), 1)
        with self.assertRaises(RepositoryException):
            self.__repo.delete_by_id(2)

    def test_update_repo(self):
        self.__repo.save(Client(1, "John"))
        self.__repo.save(Client(2, "Wick"))
        self.__repo.update_by_id(1, "Mike")
        with self.assertRaises(RepositoryException):
            self.__repo.update_by_id(4, "Johnson")

    def test_clients_property(self):
        client1 = Client(1, "Arnold")
        client2 = Client(2, "Mike")
        self.__repo.save(client1)
        self.__repo.save(client2)
        self.assertEqual(self.__repo.clients, {1: client1, 2: client2})

    def test_find_by_name(self):
        client1 = Client(1, "John")
        client2 = Client(2, "Mike Johnson")
        client3 = Client(3, "Jo")
        client4 = Client(4, "Dom Toretto")
        self.__repo.save(client1)
        self.__repo.save(client2)
        self.__repo.save(client3)
        self.__repo.save(client4)
        self.assertEqual(self.__repo.find_by_name("jo"), [client3, client1, client2])
        with self.assertRaises(RepositoryException):
            self.__repo.find_by_name("")


class RentalRepositoryTest(unittest.TestCase):
    def setUp(self) -> None:
        self.__repo = RentalRepository(RentalValidator)

    def tearDown(self) -> None:
        pass

    def test_empty_repo(self):
        self.assertEqual(len(self.__repo), 0)

    def test_rent(self):
        rented_date = datetime.date(2020, 5, 4)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.assertEqual(len(self.__repo), 1)

    def test_return_book(self):
        rented_date = datetime.date(2020, 5, 4)
        returned_date = datetime.date(2020, 9, 9)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.__repo.return_book(3, 2, returned_date)
        self.assertEqual(self.__repo.find_by_id(1).returned_date, returned_date)

    def test_delete(self):
        rented_date = datetime.date(2020, 5, 4)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.__repo.delete_by_id(1)
        self.assertEqual(len(self.__repo), 0)
        with self.assertRaises(RepositoryException):
            self.__repo.delete_by_id(1)

    def test_update(self):
        rented_date = datetime.date(2020, 5, 4)
        returned_date = datetime.date(2020, 9, 9)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.__repo.update_by_id(1, 4, 5, rented_date, returned_date)
        self.assertEqual(self.__repo.find_by_id(1).returned_date, returned_date)
        with self.assertRaises(RepositoryException):
            self.__repo.update_by_id(12, 8, 9, None, None)

    def test_find_book_in_rentals(self):
        rented_date = datetime.date(2020, 5, 4)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.assertEqual(self.__repo.find_book_in_rentals(2), rental)
        self.assertEqual(self.__repo.find_book_in_rentals(111), None)

    def test_rent_date_greater_than_return_date(self):
        rented_date = datetime.date(2020, 5, 4)
        returned_date = datetime.date(2020, 9, 9)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.__repo.return_book(3, 2, returned_date)
        self.assertEqual(self.__repo.rent_date_greater_than_return_date(2, datetime.date(2020, 9, 10)), None)
        self.assertEqual(self.__repo.rent_date_greater_than_return_date(2, datetime.date(2010, 9, 10)), rental)

    def test_find_book_with_client(self):
        rented_date = datetime.date(2020, 5, 4)
        rental = Rental(1, 2, 3, rented_date, None)
        self.__repo.rent(rental)
        self.assertEqual(self.__repo.find_book_with_client(2, 3), rental)
        self.assertEqual(self.__repo.find_book_with_client(5, 6), None)

    def test_rentals_property(self):
        rented_date = datetime.date(2020, 5, 4)
        rental1 = Rental(1, 2, 3, rented_date, None)
        rental2 = Rental(2, 11, 2, rented_date, None)
        rental3 = Rental(3, 4, 5, rented_date, None)
        rental4 = Rental(4, 22, 2, rented_date, None)
        self.__repo.rent(rental1)
        self.__repo.rent(rental2)
        self.__repo.rent(rental3)
        self.__repo.rent(rental4)
        self.assertEqual(self.__repo.rentals, {1: rental1, 2: rental2, 3: rental3, 4: rental4})

    def test_most_rented_books(self):
        rented_date = datetime.date(2020, 5, 4)
        rental1 = Rental(1, 2, 3, rented_date, None)
        rental2 = Rental(2, 1, 2, rented_date, None)
        rental3 = Rental(3, 4, 5, rented_date, None)
        rental4 = Rental(4, 8, 2, rented_date, None)
        self.__repo.rent(rental1)
        self.__repo.rent(rental2)
        self.__repo.rent(rental3)
        self.__repo.rent(rental4)
        self.assertEqual(self.__repo.most_rented_books(), {2: 1, 1: 1, 4: 1, 8: 1})

    def test_most_active_clients(self):
        rented_date = datetime.date(2021, 5, 5)
        rental1 = Rental(1, 2, 3, rented_date, None)
        rental2 = Rental(2, 9, 2, rented_date, None)
        rental3 = Rental(3, 4, 5, rented_date, None)
        rental4 = Rental(4, 8, 2, rented_date, None)
        self.__repo.rent(rental1)
        self.__repo.rent(rental2)
        self.__repo.rent(rental3)
        self.__repo.rent(rental4)
        returned_date = datetime.date(2021, 5, 10)
        self.__repo.return_book(3, 2, returned_date)
        self.__repo.return_book(2, 9, returned_date)
        self.__repo.return_book(5, 4, returned_date)
        self.__repo.return_book(2, 8, returned_date)
        self.assertEqual(self.__repo.most_active_clients(), {3: 5, 2: 10, 5: 5})


class BookServiceTest(unittest.TestCase):
    def setUp(self) -> None:
        __repo = BookRepository(BookValidator)
        __repo.save(Book(1, "The wall", "Nobody"))
        self.__book_service = BookService(__repo)

    def tearDown(self) -> None:
        pass

    def test_add(self):
        self.__book_service.add(2, "Nothing", "No one")
        self.assertNotEqual(self.__book_service.search_by_id(2), None)

    def test_update(self):
        self.__book_service.update(1, "Nothing", "No one")
        self.assertEqual(self.__book_service.search_by_id(1).title, "Nothing")

    def test_remove(self):
        self.__book_service.remove(1)
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_id(1)

    def test_list_of_books(self):
        self.assertEqual(self.__book_service.list_of_books(), [self.__book_service.search_by_id(1)])

    def test_populate(self):
        self.__book_service.populate()
        self.assertEqual(len(self.__book_service.list_of_books()), 20)

    def test_search_by_title(self):
        self.assertEqual(self.__book_service.search_by_title("the"), [self.__book_service.search_by_id(1)])
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_title("ana")

    def test_search_by_author(self):
        self.assertEqual(self.__book_service.search_by_author("no"), [self.__book_service.search_by_id(1)])
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_author("ana")


class ClientServiceTest(unittest.TestCase):
    def setUp(self) -> None:
        __repo = ClientRepository(ClientValidator)
        __repo.save(Client(1, "Arnold"))
        self.__client_service = ClientService(__repo)

    def tearDown(self) -> None:
        pass

    def test_add(self):
        self.__client_service.add(2, "Mark")
        self.assertNotEqual(self.__client_service.search_by_id(2), None)

    def test_update(self):
        self.__client_service.update(1, "John")
        self.assertEqual(self.__client_service.search_by_id(1).name, "John")

    def test_remove(self):
        self.__client_service.remove(1)
        with self.assertRaises(ClientServiceException):
            self.__client_service.search_by_id(1)

    def test_list_of_clients(self):
        self.assertEqual(self.__client_service.list_of_clients(), [self.__client_service.search_by_id(1)])

    def test_populate(self):
        self.__client_service.populate()
        self.assertEqual(len(self.__client_service.list_of_clients()), 20)

    def test_search_by_name(self):
        self.assertEqual(self.__client_service.search_by_name("nol"), [self.__client_service.search_by_id(1)])
        with self.assertRaises(ClientServiceException):
            self.__client_service.search_by_name("daniel")


class RentalServiceTest(unittest.TestCase):
    def setUp(self) -> None:
        __book_repo = BookRepository(BookValidator)
        __book_repo.save(Book(1, "The wall", "Nobody"))
        __book_repo.save(Book(11, "Something", "Anyone"))
        __client_repo = ClientRepository(ClientValidator)
        __client_repo.save(Client(1, "Mark"))
        __client_repo.save(Client(11, "Martin"))
        __rental_repo = RentalRepository(RentalValidator)
        self.__rental_service = RentalService(__rental_repo, __book_repo, __client_repo)

    def tearDown(self) -> None:
        pass

    def test_rent_a_book(self):
        self.__rental_service.rent_a_book(1, 1, 1, 10, 11, 2020)
        self.assertEqual(len(self.__rental_service.list_of_rentals()), 1)
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(1, 1, 1, 10, 11, 2022)
        self.assertEqual(str(rse.exception), "The rented date must be smaller than the current date")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(1, 2, 1, 10, 11, 2020)
        self.assertEqual(str(rse.exception), "we don't have the book with id: 2")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(1, 1, 2, 10, 11, 2020)
        self.assertEqual(str(rse.exception), "the client with the id: 2 doesn't exist")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(1, 1, 1, 10, 11, 2020)
        self.assertEqual(str(rse.exception), "duplicate rental id 1")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(2, 1, 1, 10, 11, 2020)
        self.assertEqual(str(rse.exception), "the book with id: 1 is already rented by a customer")
        self.__rental_service.return_a_book(1, 1, 11, 11, 2020)
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.rent_a_book(2, 1, 1, 10, 11, 2020)
        self.assertEqual(str(rse.exception), "The book with id: 1 was returned in 2020-11-11, cannot rent it in "
                                             "2020-11-10")

    def test_return_a_book(self):
        self.__rental_service.rent_a_book(1, 1, 1, 10, 11, 2020)
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.return_a_book(1, 1, 10, 10, 2022)
        self.assertEqual(str(rse.exception), "The returned date must be smaller than the current date")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.return_a_book(1, 2, 12, 12, 2020)
        self.assertEqual(str(rse.exception), "The client with id: 1 didn't rent that book in order to return it")
        with self.assertRaises(RentalServiceException) as rse:
            self.__rental_service.return_a_book(1, 1, 10, 10, 2010)
        self.assertEqual(str(rse.exception), "Cannot return before renting")
        self.__rental_service.return_a_book(1, 1, 11, 11, 2020)

    def test_statics_books(self):
        self.assertEqual(self.__rental_service.statistics_books(), [(1, 0), (11, 0)])
        self.__rental_service.rent_a_book(1, 11, 1, 10, 11, 2020)
        self.assertEqual(self.__rental_service.statistics_books(), [(11, 1), (1, 0)])

    def test_statistics_author(self):
        self.__rental_service.rent_a_book(1, 11, 1, 10, 11, 2020)
        self.__rental_service.return_a_book(1, 11, 15, 11, 2020)
        self.assertEqual(self.__rental_service.statistics_authors(), [("Anyone", 1), ("Nobody", 0)])

    def test_statistics_clients(self):
        self.__rental_service.rent_a_book(1, 11, 1, 10, 11, 2020)
        self.__rental_service.return_a_book(1, 11, 15, 11, 2020)
        self.assertEqual(self.__rental_service.statistics_clients(), [(1, 5), (11, 0)])


class DateTest(unittest.TestCase):
    def test_validate_date(self):
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(30, 2, 2020)
        self.assertEqual(str(rve.exception), "Not a valid day")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(30, 2, 2021)
        self.assertEqual(str(rve.exception), "Not a valid day")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(-1, -2, 2020)
        self.assertEqual(str(rve.exception), "The day and the month must be positive")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(1, 2, -2020)
        self.assertEqual(str(rve.exception), "Invalid year")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(1, 22, 2020)
        self.assertEqual(str(rve.exception), "The month cannot be greater than 12")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(33, 3, 2020)
        self.assertEqual(str(rve.exception), "Not a valid day")
        with self.assertRaises(RentalValidatorException) as rve:
            RentalValidator.validate_date(33, 6, 2020)
        self.assertEqual(str(rve.exception), "Not a valid day")

    def test_validate_book(self):
        with self.assertRaises(BookValidatorException) as bve:
            BookValidator.validate(Book(1, "", "Ana"))
        self.assertEqual(str(bve.exception), "The book title cannot be empty")
        with self.assertRaises(BookValidatorException) as bve:
            BookValidator.validate(Book(1, "Something", ""))
        self.assertEqual(str(bve.exception), "The book author cannot be empty")


class UndoRedoTest(unittest.TestCase):
    def setUp(self) -> None:
        book_repo = BookRepository(BookValidator)
        client_repo = ClientRepository(ClientValidator)
        rental_repo = RentalRepository(RentalValidator)
        book_service = BookService(book_repo)
        client_service = ClientService(client_repo)
        rental_service = RentalService(rental_repo, book_repo, client_repo)
        self.__rental_service = rental_service
        self.__client_service = client_service
        self.__book_service = book_service
        UndoManager.empty_for_testing()
        RedoManager.empty_for_testing()

    def tearDown(self) -> None:
        pass

    def test_undo_redo_add_book(self):
        self.__book_service.add(1, "The wall", "Nobody")
        self.assertNotEqual(self.__book_service.search_by_id(1), None)
        UndoManager.undo()
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_id(1)
        RedoManager.redo()
        self.assertNotEqual(self.__book_service.search_by_id(1), None)
        with self.assertRaises(RedoException):
            RedoManager.redo()
        UndoManager.undo()
        with self.assertRaises(UndoException):
            UndoManager.undo()

    def test_undo_redo_remove_book(self):
        self.__book_service.add(1, "The wall", "Nobody")
        self.__book_service.remove(1)
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_id(1)
        UndoManager.undo()
        self.assertNotEqual(self.__book_service.search_by_id(1), None)
        RedoManager.redo()
        with self.assertRaises(BookServiceException):
            self.__book_service.search_by_id(1)

    def test_undo_redo_update_book(self):
        self.__book_service.add(1, "The wall", "Nobody")
        self.__book_service.update(1, "Something", "Anyone")
        UndoManager.undo()
        self.assertEqual(self.__book_service.search_by_id(1).title, "The wall")
        RedoManager.redo()
        self.assertEqual(self.__book_service.search_by_id(1).title, "Something")
        UndoManager.undo()
        self.assertEqual(self.__book_service.search_by_id(1).title, "The wall")
        RedoManager.redo()
        self.assertEqual(self.__book_service.search_by_id(1).title, "Something")

    def test_undo_redo_add_client(self):
        self.__client_service.add(1, "Mark")
        UndoManager.undo()
        with self.assertRaises(ClientServiceException):
            self.__client_service.search_by_id(1)
        RedoManager.redo()
        self.assertNotEqual(self.__client_service.search_by_id(1), None)

    def test_undo_redo_remove_client(self):
        self.__client_service.add(2, "John")
        self.__client_service.remove(2)
        with self.assertRaises(ClientServiceException):
            self.__client_service.search_by_id(2)
        UndoManager.undo()
        self.assertNotEqual(self.__client_service.search_by_id(2), None)
        RedoManager.redo()
        with self.assertRaises(ClientServiceException):
            self.__client_service.search_by_id(2)

    def test_undo_redo_update_client(self):
        self.__client_service.add(3, "Leo")
        self.__client_service.update(3, "Cristi")
        UndoManager.undo()
        self.assertEqual(self.__client_service.search_by_id(3).name, "Leo")
        RedoManager.redo()
        self.assertEqual(self.__client_service.search_by_id(3).name, "Cristi")

    def test_undo_redo_rent_book(self):
        self.__book_service.add(99, "Nothing", "Nobody")
        self.__client_service.add(99, "SuperMan")
        self.__rental_service.rent_a_book(1, 99, 99, 2, 2, 2010)
        UndoManager.undo()
        with self.assertRaises(RentalServiceException):
            self.__rental_service.search_by_id(1)
        RedoManager.redo()
        self.assertNotEqual(self.__rental_service.search_by_id(1), None)
        UndoManager.undo()
        with self.assertRaises(RentalServiceException):
            self.__rental_service.search_by_id(1)

    def test_undo_redo_return_book(self):
        self.__book_service.add(99, "Nothing", "Nobody")
        self.__client_service.add(99, "SuperMan")
        self.__rental_service.rent_a_book(1, 99, 99, 2, 2, 2010)
        self.__rental_service.return_a_book(99, 99, 5, 5, 2010)
        UndoManager.undo()
        self.assertEqual(self.__rental_service.search_by_id(1).returned_date, None)
        RedoManager.redo()
        self.assertNotEqual(self.__rental_service.search_by_id(1).returned_date, None)
