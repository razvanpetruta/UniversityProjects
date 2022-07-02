import datetime

from domain.bookClass import Book
from domain.clientClass import Client
from domain.rentalClass import Rental
from domain.validators import BookValidator, ClientValidator, RentalValidator
from repository.bookRepository import BookRepository
from repository.clientRepository import ClientRepository
from repository.rentalRepository import RentalRepository
from services.bookService import BookService
from services.clientService import ClientService
from services.rentalService import RentalService


def test_book_class():
    """
    Test the Book class.
    """
    book = Book(1, "The wall", "Nobody")
    assert book.book_id == 1
    assert book.title == "The wall"
    assert book.author == "Nobody"
    book.book_id = 11
    assert book.book_id == 11
    book.title = "Nothing"
    assert book.title == "Nothing"
    book.author = "Bianca"
    assert book.author == "Bianca"


def test_client_class():
    """
    Test the Client class.
    """
    client = Client(1, "John Wick")
    assert client.client_id == 1
    assert client.name == "John Wick"
    client.client_id = 111
    assert client.client_id == 111
    client.name = "The only one"
    assert client.name == "The only one"


def test_rental_class():
    """
    Test the Rental class.
    """
    rented_date = datetime.date(2022, 5, 4)
    rental = Rental(1, 2, 3, rented_date, None)
    assert rental.rental_id == 1
    assert rental.book_id == 2
    assert rental.client_id == 3
    assert rental.rented_date.day == 4
    assert rental.rented_date.month == 5
    assert rental.rented_date.year == 2022
    assert rental.returned_date is None
    returned_date = datetime.date(2023, 5, 4)
    rental.returned_date = returned_date
    assert rental.returned_date.day == 4
    assert rental.returned_date.month == 5
    assert rental.returned_date.year == 2023


def test_book_repository():
    """
    Test BookRepository class.
    """
    book_repo = BookRepository(BookValidator)
    book1 = Book(1, "The wall", "Nobody")
    book_repo.save(book1)
    assert book_repo.find_by_id(1) == book1
    book2 = Book(2, "The love", "of no one")
    book_repo.save(book2)
    assert book_repo.find_by_id(2) == book2
    assert book_repo.find_by_id(3) is None
    book_repo.delete_by_id(2)
    assert book_repo.find_by_id(2) is None
    book_repo.update_by_id(1, "Be you", "your heart")
    assert book_repo.find_by_id(1).title == "Be you"
    assert book_repo.find_by_id(1).author == "your heart"
    assert len(book_repo) == 1


def test_client_repository():
    """
    Test ClientRepository class.
    """
    client_repo = ClientRepository(ClientValidator)
    client1 = Client(1, "Mihai")
    client_repo.save(client1)
    assert client_repo.find_by_id(1) == client1
    client2 = Client(2, "George")
    client_repo.save(client2)
    assert client_repo.find_by_id(2) == client2
    assert client_repo.find_by_id(3) is None
    client_repo.delete_by_id(2)
    assert client_repo.find_by_id(2) is None
    client_repo.update_by_id(1, "Eminem")
    assert client_repo.find_by_id(1).name == "Eminem"
    assert len(client_repo) == 1


def test_rental_repository():
    """
    Test RentalRepository class.
    """
    rental_repo = RentalRepository(RentalValidator)
    rented_date = datetime.date(2022, 5, 4)
    rental1 = Rental(1, 2, 3, rented_date, None)
    rental_repo.rent(rental1)
    assert rental_repo.find_by_id(1) == rental1
    assert rental_repo.find_book_in_rentals(2) == rental1
    assert rental_repo.find_book_with_client(2, 3) == rental1
    returned_date = datetime.date(2023, 1, 1)
    rental_repo.return_book(3, 2, returned_date)
    assert rental_repo.find_by_id(1).returned_date == returned_date


def test_book_service():
    """
    Test BookService class.
    """
    book_repo = BookRepository(BookValidator)
    book_service = BookService(book_repo)
    book_service.add(1, "The wall", "Nobody")
    book_service.add(2, "Something", "Anybody")
    assert book_repo.find_by_id(1) is not None
    assert book_repo.find_by_id(2) is not None
    book_service.update(1, "Heart", "Mind")
    assert book_repo.find_by_id(1).title == "Heart"
    assert book_repo.find_by_id(1).author == "Mind"
    book_service.remove(1)
    assert book_repo.find_by_id(1) is None


def test_client_service():
    """
    Test ClientService class.
    """
    client_repo = ClientRepository(ClientValidator)
    client_service = ClientService(client_repo)
    client_service.add(1, "Viorel")
    client_service.add(2, "Mircea")
    assert client_repo.find_by_id(1) is not None
    assert client_repo.find_by_id(2) is not None
    client_service.update(1, "Bravo")
    assert client_repo.find_by_id(1).name == "Bravo"
    client_service.remove(1)
    assert client_repo.find_by_id(1) is None


def test_rental_service():
    """
    Test RentalService class.
    """
    book_repo = BookRepository(BookValidator)
    book1 = Book(1, "The wall", "Nobody")
    book_repo.save(book1)
    book2 = Book(2, "The love", "of no one")
    book_repo.save(book2)
    client_repo = ClientRepository(ClientValidator)
    client1 = Client(1, "Mihai")
    client_repo.save(client1)
    client2 = Client(2, "George")
    client_repo.save(client2)
    rental_repo = RentalRepository(RentalValidator)
    rental_service = RentalService(rental_repo, book_repo, client_repo)
    rental_service.rent_a_book(1, 1, 1, 20, 5, 2020)
    assert rental_repo.find_by_id(1) is not None
    assert rental_repo.find_by_id(1).returned_date is None
    rental_service.return_a_book(1, 1, 12, 12, 2020)
    assert rental_repo.find_by_id(1).returned_date.year == 2020


def run_all_tests():
    """
    Run all tests.
    """
    test_book_class()
    test_client_class()
    test_rental_class()
    test_book_repository()
    test_client_repository()
    test_rental_repository()
    test_book_service()
    test_client_service()
    test_rental_service()
    print("all tests passed successfully...")
