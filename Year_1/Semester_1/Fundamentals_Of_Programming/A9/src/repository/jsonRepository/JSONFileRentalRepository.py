from repository.dataAccessEntity import RentalDataAccess
from repository.rentalRepository import RentalRepository


class JSONFileRentalRepository(RentalRepository):
    """
    Class for rental repository specific to a json file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        data = RentalDataAccess.read_from_json_file(self.__file_path)
        for rental in data:
            self.__save(rental)

    def __save(self, rental):
        self._rentals[rental.rental_id] = rental

    def rent(self, rental):
        super().rent(rental)
        RentalDataAccess.write_in_json_file(self.__file_path, self.rentals)

    def return_book(self, client_id, book_id, returned_date):
        super().return_book(client_id, book_id, returned_date)
        RentalDataAccess.write_in_json_file(self.__file_path, self.rentals)

    def delete_by_id(self, rental_id):
        super().delete_by_id(rental_id)
        RentalDataAccess.write_in_json_file(self.__file_path, self.rentals)

    def update_by_id(self, rental_id, book_id, client_id, rented_date, returned_date):
        super().update_by_id(rental_id, book_id, client_id, rented_date, returned_date)
        RentalDataAccess.write_in_json_file(self.__file_path, self.rentals)
