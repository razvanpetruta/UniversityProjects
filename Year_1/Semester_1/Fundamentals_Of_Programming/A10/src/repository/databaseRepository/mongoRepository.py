from datetime import datetime

import pymongo
from pymongo import MongoClient

from domain.bookClass import Book
from domain.clientClass import Client
from domain.rentalClass import Rental
from repository.bookRepository import BookRepository
from repository.clientRepository import ClientRepository
from repository.rentalRepository import RentalRepository


class MongoBookRepository(BookRepository):
    """
    Repository that saves the books in our mongoDB cluster.
    """
    def __init__(self, validator_class, cluster):
        super().__init__(validator_class)
        self.__cluster = cluster
        self.__db = self.__cluster["library"]
        self.__collection = self.__db["books"]
        self.__load_data()

    def __load_data(self):
        for book in self.__collection.find():
            super().save(Book(book["_id"], book["title"], book["author"]))

    def save(self, book):
        super().save(book)
        post = {
            "_id": book.book_id,
            "title": book.title,
            "author": book.author
        }
        self.__collection.insert_one(post)

    def delete_by_id(self, book_id):
        deleted_book = super().delete_by_id(book_id)
        self.__collection.delete_one({"_id": book_id})
        return deleted_book

    def update_by_id(self, book_id, new_title, new_author):
        updated_book = super().update_by_id(book_id, new_title, new_author)
        self.__collection.update_one({"_id": book_id}, {"$set": {"title": new_title, "author": new_author}})
        return updated_book


class MongoClientRepository(ClientRepository):
    """
    Repository that saves the clients in our mongoDB cluster.
    """
    def __init__(self, validator_class, cluster):
        super().__init__(validator_class)
        self.__cluster = cluster
        self.__db = self.__cluster["library"]
        self.__collection = self.__db["clients"]
        self.__load_data()

    def __load_data(self):
        for client in self.__collection.find():
            super().save(Client(client["_id"], client["name"]))

    def save(self, client):
        super().save(client)
        post = {
            "_id": client.client_id,
            "name": client.name
        }
        self.__collection.insert_one(post)

    def delete_by_id(self, client_id):
        deleted_client = super().delete_by_id(client_id)
        self.__collection.delete_one({"_id": client_id})
        return deleted_client

    def update_by_id(self, client_id, new_name):
        updated_client = super().update_by_id(client_id, new_name)
        self.__collection.update_one({"_id": client_id}, {"$set": {"name": new_name}})
        return updated_client


class MongoRentalRepository(RentalRepository):
    """
    Repository that saves the rentals in our mongoDB cluster.
    """
    def __init__(self, validator_class, cluster):
        super().__init__(validator_class)
        self.__cluster = cluster
        self.__db = self.__cluster["library"]
        self.__collection = self.__db["rentals"]
        self.__load_data()

    def __load_data(self):
        for rental in self.__collection.find():
            if rental["returned_date"]["day"] == -1:
                self.__save(Rental(rental["_id"], rental["book_id"], rental["client_id"],
                                   datetime(rental["rented_date"]["year"],
                                            rental["rented_date"]["month"],
                                            rental["rented_date"]["day"]).date(), None))
            else:
                self.__save(Rental(rental["_id"], rental["book_id"], rental["client_id"],
                                   datetime(rental["rented_date"]["year"],
                                            rental["rented_date"]["month"],
                                            rental["rented_date"]["day"]).date(),
                                   datetime(rental["returned_date"]["year"],
                                            rental["returned_date"]["month"],
                                            rental["returned_date"]["day"]).date()))

    def __save(self, rental):
        self._rentals[rental.rental_id] = rental

    def rent(self, rental):
        super().rent(rental)
        post = {
            "_id": rental.rental_id,
            "book_id": rental.book_id,
            "client_id": rental.client_id,
            "rented_date": {
                "day": rental.rented_date.day,
                "month": rental.rented_date.month,
                "year": rental.rented_date.year
            },
            "returned_date": {
                "day": -1,
                "month": -1,
                "year": -1
            }
        }
        self.__collection.insert_one(post)

    def return_book(self, client_id, book_id, returned_date):
        super().return_book(client_id, book_id, returned_date)
        self.__collection.update_one({"client_id": client_id, "book_id": book_id},
                                     {"$set": {"returned_date": {
                                         "day": returned_date.day,
                                         "month": returned_date.month,
                                         "year": returned_date.year
                                     }}})

    def delete_by_id(self, rental_id):
        super().delete_by_id(rental_id)
        self.__collection.delete_one({"_id": rental_id})

    def update_by_id(self, rental_id, book_id, client_id, rented_date, returned_date):
        super().update_by_id(rental_id, book_id, client_id, rented_date, returned_date)
        if returned_date is not None:
            self.__collection.update_one({"_id": rental_id}, {"$set": {
                "book_id": book_id,
                "client_id": client_id,
                "rented_date": {
                    "day": rented_date.day,
                    "month": rented_date.month,
                    "year": rented_date.year
                },
                "returned_date": {
                    "day": returned_date.day,
                    "month": returned_date.month,
                    "year": returned_date.year
                }
            }})
        else:
            self.__collection.update_one({"_id": rental_id}, {"$set": {
                "book_id": book_id,
                "client_id": client_id,
                "rented_date": {
                    "day": rented_date.day,
                    "month": rented_date.month,
                    "year": rented_date.year
                },
                "returned_date": {
                    "day": -1,
                    "month": -1,
                    "year": -1
                }
            }})
