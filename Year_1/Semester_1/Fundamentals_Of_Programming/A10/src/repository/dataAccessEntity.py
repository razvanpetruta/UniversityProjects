import json
import os
import pickle
from datetime import datetime

from domain.bookClass import Book
from domain.clientClass import Client
from domain.rentalClass import Rental


class BookDataAccess:
    """
    Methods for reading/writing book from/to files.
    """

    @staticmethod
    def read_from_text_file(line):
        book_data = line.split(",")
        book = Book(int(book_data[0]), book_data[1], book_data[2].rstrip("\n"))
        return book

    @staticmethod
    def write_in_text_file(books, file_pointer):
        books_list = []
        for key in books:
            books_list.append(books[key])
        i = 0
        for book in books_list:
            if i == 0:
                file_pointer.write(f"{book.book_id},{book.title},{book.author}")
            else:
                file_pointer.write(f"\n{book.book_id},{book.title},{book.author}")
            i += 1

    @staticmethod
    def read_from_binary_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path, "rb") as file_pointer:
            data = pickle.load(file_pointer)
            return data

    @staticmethod
    def write_in_binary_file(file_path, data):
        with open(file_path, "wb") as file_pointer:
            pickle.dump(data, file_pointer)

    @staticmethod
    def read_from_json_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path) as file_pointer:
            data = json.load(file_pointer)
            sol = []
            for book_dictionary in data:
                sol.append(Book(book_dictionary["book_id"], book_dictionary["title"], book_dictionary["author"]))
            return sol

    @staticmethod
    def write_in_json_file(file_path, data):
        with open(file_path, "w") as file_pointer:
            sol = []
            for key in data:
                book_dictionary = {
                    "book_id": data[key].book_id,
                    "title": data[key].title,
                    "author": data[key].author
                }
                sol.append(book_dictionary)
            json.dump(sol, file_pointer)


class ClientDataAccess:
    """
    Methods for reading/writing clients from/to files.
    """

    @staticmethod
    def read_from_text_file(line):
        client_data = line.split(",")
        client = Client(int(client_data[0]), client_data[1].rstrip("\n"))
        return client

    @staticmethod
    def write_in_text_file(clients, file_pointer):
        clients_list = []
        for key in clients:
            clients_list.append(clients[key])
        i = 0
        for client in clients_list:
            if i == 0:
                file_pointer.write(f"{client.client_id},{client.name}")
            else:
                file_pointer.write(f"\n{client.client_id},{client.name}")
            i += 1

    @staticmethod
    def read_from_binary_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path, "rb") as file_pointer:
            data = pickle.load(file_pointer)
            return data

    @staticmethod
    def write_in_binary_file(file_path, data):
        with open(file_path, "wb") as file_pointer:
            pickle.dump(data, file_pointer)

    @staticmethod
    def read_from_json_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path) as file_pointer:
            data = json.load(file_pointer)
            sol = []
            for client_dictionary in data:
                sol.append(Client(client_dictionary["client_id"], client_dictionary["name"]))
            return sol

    @staticmethod
    def write_in_json_file(file_path, data):
        with open(file_path, "w") as file_pointer:
            sol = []
            for key in data:
                client_dictionary = {
                    "client_id": data[key].client_id,
                    "name": data[key].name
                }
                sol.append(client_dictionary)
            json.dump(sol, file_pointer)


class RentalDataAccess:
    """
    Methods for reading/writing rentals from/to files.
    """

    @staticmethod
    def read_from_text_file(line):
        rental_data = line.split(",")
        if rental_data[4].rstrip("\n") == "-":
            rental = Rental(int(rental_data[0]), int(rental_data[1]), int(rental_data[2]),
                            datetime.fromisoformat(rental_data[3]).date(), None)
            return rental
        else:
            rental = Rental(int(rental_data[0]), int(rental_data[1]), int(rental_data[2]),
                            datetime.fromisoformat(rental_data[3]).date(),
                            datetime.fromisoformat(rental_data[4].rstrip("\n")).date())
            return rental

    @staticmethod
    def write_in_text_file(rentals, file_pointer):
        rentals_list = []
        for key in rentals:
            rentals_list.append(rentals[key])
        i = 0
        for rental in rentals_list:
            if i == 0:
                if rental.returned_date is None:
                    file_pointer.write(f"{rental.rental_id},{rental.book_id},{rental.client_id},{rental.rented_date},"
                                       f"-")
                else:
                    file_pointer.write(f"{rental.rental_id},{rental.book_id},{rental.client_id},{rental.rented_date},"
                                       f"{rental.returned_date}")
            else:
                if rental.returned_date is None:
                    file_pointer.write(f"\n{rental.rental_id},{rental.book_id},{rental.client_id},{rental.rented_date},"
                                       f"-")
                else:
                    file_pointer.write(f"\n{rental.rental_id},{rental.book_id},{rental.client_id},{rental.rented_date},"
                                       f"{rental.returned_date}")
            i += 1

    @staticmethod
    def read_from_binary_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path, "rb") as file_pointer:
            data = pickle.load(file_pointer)
            return data

    @staticmethod
    def write_in_binary_file(file_path, data):
        with open(file_path, "wb") as file_pointer:
            pickle.dump(data, file_pointer)

    @staticmethod
    def read_from_json_file(file_path):
        if os.path.getsize(file_path) == 0:
            return {}
        with open(file_path) as file_pointer:
            data = json.load(file_pointer)
            sol = []
            for rental_dictionary in data:
                if rental_dictionary["returned_date"] is not None:
                    sol.append(Rental(rental_dictionary["rental_id"],
                                      rental_dictionary["book_id"],
                                      rental_dictionary["client_id"],
                                      datetime(rental_dictionary["rented_date"]["year"],
                                               rental_dictionary["rented_date"]["month"],
                                               rental_dictionary["rented_date"]["day"]).date(),
                                      datetime(rental_dictionary["returned_date"]["year"],
                                               rental_dictionary["returned_date"]["month"],
                                               rental_dictionary["returned_date"]["day"]).date()
                                      ))
                else:
                    sol.append(Rental(rental_dictionary["rental_id"],
                                      rental_dictionary["book_id"],
                                      rental_dictionary["client_id"],
                                      datetime(rental_dictionary["rented_date"]["year"],
                                               rental_dictionary["rented_date"]["month"],
                                               rental_dictionary["rented_date"]["day"]).date(),
                                      None
                                      ))
            return sol

    @staticmethod
    def write_in_json_file(file_path, data):
        with open(file_path, "w") as file_pointer:
            sol = []
            for key in data:
                if data[key].returned_date is not None:
                    rental_dictionary = {
                        "rental_id": data[key].rental_id,
                        "book_id": data[key].book_id,
                        "client_id": data[key].client_id,
                        "rented_date": {
                            "day": data[key].rented_date.day,
                            "month": data[key].rented_date.month,
                            "year": data[key].rented_date.year
                        },
                        "returned_date": {
                            "day": data[key].returned_date.day,
                            "month": data[key].returned_date.month,
                            "year": data[key].returned_date.year
                        }
                    }
                    sol.append(rental_dictionary)
                else:
                    rental_dictionary = {
                        "rental_id": data[key].rental_id,
                        "book_id": data[key].book_id,
                        "client_id": data[key].client_id,
                        "rented_date": {
                            "day": data[key].rented_date.day,
                            "month": data[key].rented_date.month,
                            "year": data[key].rented_date.year
                        },
                        "returned_date": None
                    }
                    sol.append(rental_dictionary)
            json.dump(sol, file_pointer)
