from src.domain.book_entity import Book
from src.domain.validators import BookValidator
from src.services.services import BookService


def test_book_class():
    """
    Run tests using the Book class.
    """
    book = Book("1", "The world", "Nobody")
    assert book.isbn == "1"
    assert book.title == "The world"
    assert book.author == "Nobody"
    book.isbn = "2"
    assert book.isbn == "2"
    book.title = "Nothing"
    assert book.title == "Nothing"
    book.author = "Anybody"
    assert book.author == "Anybody"


def test_book_service_class():
    """
    Run tests for BookService class.
    """
    book1 = Book("1", "The world", "Nobody")
    book2 = Book("2", "Nothing", "Anybody")
    book3 = Book("3", "Something", "No one")
    books = [book1, book2]
    book_validator = BookValidator()
    book_service = BookService(books, book_validator)
    assert book_service.stage == books
    book_service.stage = [book3, book1]
    assert book_service.stage == [book3, book1]
    book_service.add_book("3", "Nothing", "Anybody")
    assert book_service.stage[-1].isbn == "3"
    assert book_service.stage[-1].title == "Nothing"
    assert book_service.stage[-1].author == "Anybody"
    book_service.remove_title("the")
    assert len(book_service.stage) == 2


def run_all_tests():
    """
    Run all tests.
    """
    test_book_class()
    test_book_service_class()
    print("Tests passed successfully")
