from domain.validators import BookValidator, ClientValidator, RentalValidator
from repository.bookRepository import BookRepository
from repository.clientRepository import ClientRepository
from repository.rentalRepository import RentalRepository
from services.bookService import BookService
from services.clientService import ClientService
from services.rentalService import RentalService
# from tests import tests
from ui.consoleClass import Console

if __name__ == "__main__":
    # tests.run_all_tests()
    book_repo = BookRepository(BookValidator)
    client_repo = ClientRepository(ClientValidator)
    rental_repo = RentalRepository(RentalValidator)
    book_service = BookService(book_repo)
    client_service = ClientService(client_repo)
    rental_service = RentalService(rental_repo, book_repo, client_repo)
    console = Console(book_service, client_service, rental_service)
    console.run()
