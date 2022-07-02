from pymongo import MongoClient

from configuration.configurationSettings import Settings
from domain.validators import BookValidator, ClientValidator, RentalValidator
from repository.databaseRepository.mongoRepository import MongoBookRepository, MongoClientRepository, \
    MongoRentalRepository
from repository.jsonRepository.JSONFileBookRepository import JSONFileBookRepository
from repository.jsonRepository.JSONFileClientRepository import JSONFileClientRepository
from repository.jsonRepository.JSONFileRentalRepository import JSONFileRentalRepository
from repository.binaryRepository.binaryFileBookRepository import BinaryFileBookRepository
from repository.binaryRepository.binaryFileClientRepository import BinaryFileClientRepository
from repository.binaryRepository.binaryFileRentalRepository import BinaryFileRentalRepository
from repository.bookRepository import BookRepository
from repository.clientRepository import ClientRepository
from repository.rentalRepository import RentalRepository
from repository.textRepository.textFileBookRepository import TextFileBookRepository
from repository.textRepository.textFileClientRepository import TextFileClientRepository
from repository.textRepository.textFileRentalRepository import TextFileRentalRepository
from services.bookService import BookService
from services.clientService import ClientService
from services.rentalService import RentalService
from ui.consoleClass import Console


def repositories(settings):
    if settings.repository == "inmemory":
        return BookRepository(BookValidator), \
               ClientRepository(ClientValidator), \
               RentalRepository(RentalValidator)

    elif settings.repository == "textfiles":
        return TextFileBookRepository(BookValidator, settings.books_path), \
               TextFileClientRepository(ClientValidator, settings.clients_path), \
               TextFileRentalRepository(RentalValidator, settings.rentals_path)

    elif settings.repository == "binaryfiles":
        return BinaryFileBookRepository(BookValidator, settings.books_path), \
               BinaryFileClientRepository(ClientValidator, settings.clients_path), \
               BinaryFileRentalRepository(RentalValidator, settings.rentals_path)

    elif settings.repository == "jsonfiles":
        return JSONFileBookRepository(BookValidator, settings.books_path), \
               JSONFileClientRepository(ClientValidator, settings.clients_path), \
               JSONFileRentalRepository(RentalValidator, settings.rentals_path)


if __name__ == "__main__":
    # book_repo = TextFileBookRepository(BookValidator, "../data/textFileBooks.txt")
    # client_repo = TextFileClientRepository(ClientValidator, "../data/textFileClients.txt")
    # rental_repo = TextFileRentalRepository(RentalValidator, "../data/textFileRentals.txt")

    # book_repo = BinaryFileBookRepository(BookValidator, "../data/binaryFileBooks.bin")
    # client_repo = BinaryFileClientRepository(ClientValidator, "../data/binaryFileClients.bin")
    # rental_repo = BinaryFileRentalRepository(RentalValidator, "../data/binaryFileRentals.bin")

    # book_repo = JSONFileBookRepository(BookValidator, "../data/jsonFileBooks.json")
    # client_repo = JSONFileClientRepository(ClientValidator, "../data/jsonFileClients.json")
    # rental_repo = JSONFileRentalRepository(RentalValidator, "../data/jsonFileRentals.json")

    settings = Settings()
    if settings.repository != "mongo":
        book_repo, client_repo, rental_repo = repositories(settings)
        book_service = BookService(book_repo)
        client_service = ClientService(client_repo)
        rental_service = RentalService(rental_repo, book_repo, client_repo)
        console = Console(book_service, client_service, rental_service)
        console.run()
    else:
        cluster = MongoClient("uri")
        book_repo = MongoBookRepository(BookValidator, cluster)
        client_repo = MongoClientRepository(ClientValidator, cluster)
        rental_repo = MongoRentalRepository(RentalValidator, cluster)
        book_service = BookService(book_repo)
        client_service = ClientService(client_repo)
        rental_service = RentalService(rental_repo, book_repo, client_repo)
        console = Console(book_service, client_service, rental_service)
        console.run()
        cluster.close()
