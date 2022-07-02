class Rental:
    def __init__(self, rental_id, book_id, client_id, rented_date, returned_date):
        """
        Initialise rental object.
        :param rental_id: the rental id, it's unique
        :param book_id: the book id
        :param client_id: the client id
        :param rented_date: the rented date
        :param returned_date: the returned date
        """
        self.__rental_id = rental_id
        self.__book_id = book_id
        self.__client_id = client_id
        self.__rented_date = rented_date
        self.__returned_date = returned_date

    @property
    def rental_id(self):
        return self.__rental_id

    @rental_id.setter
    def rental_id(self, value):
        self.__rental_id = value

    @property
    def book_id(self):
        return self.__book_id

    @book_id.setter
    def book_id(self, value):
        self.__book_id = value

    @property
    def client_id(self):
        return self.__client_id

    @client_id.setter
    def client_id(self, value):
        self.__client_id = value

    @property
    def rented_date(self):
        return self.__rented_date

    @rented_date.setter
    def rented_date(self, value):
        self.__rented_date = value

    @property
    def returned_date(self):
        return self.__returned_date

    @returned_date.setter
    def returned_date(self, value):
        self.__returned_date = value

    def __str__(self):
        return f"\tid: {self.rental_id}, client id: {self.client_id}, book id: {self.book_id}, " \
               f"rented date: {self.rented_date}, returned date: "
