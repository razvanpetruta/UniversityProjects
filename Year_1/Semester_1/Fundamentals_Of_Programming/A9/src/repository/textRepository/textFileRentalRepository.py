from repository.dataAccessEntity import RentalDataAccess
from repository.rentalRepository import RentalRepository


class TextFileRentalRepository(RentalRepository):
    """
    Class for rental repository specific to a text file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        with open(self.__file_path) as file_pointer:
            for line in file_pointer:
                rental = RentalDataAccess.read_from_text_file(line)
                self.__save(rental)

    def __save(self, rental):
        self._rentals[rental.rental_id] = rental

    def __upload_content(self):
        with open(self.__file_path, "w") as file_pointer:
            RentalDataAccess.write_in_text_file(self.rentals, file_pointer)

    def rent(self, rental):
        super().rent(rental)
        self.__upload_content()

    def return_book(self, client_id, book_id, returned_date):
        super().return_book(client_id, book_id, returned_date)
        self.__upload_content()

    def delete_by_id(self, rental_id):
        super().delete_by_id(rental_id)
        self.__upload_content()

    def update_by_id(self, rental_id, book_id, client_id, rented_date, returned_date):
        super().update_by_id(rental_id, book_id, client_id, rented_date, returned_date)
        self.__upload_content()
