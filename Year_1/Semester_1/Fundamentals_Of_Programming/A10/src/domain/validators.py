class LibraryException(Exception):
    """
    Library exception class.
    """
    pass


class BookValidatorException(LibraryException):
    """
    Exception class for working with books.
    """
    pass


class BookValidator:
    """
    Book validator class.
    """
    @staticmethod
    def validate(book):
        if book.title.strip() == "":
            raise BookValidatorException("The book title cannot be empty")
        if book.author.strip() == "":
            raise BookValidatorException("The book author cannot be empty")


class ClientValidatorException(LibraryException):
    """
    Exception class for working with clients.
    """
    pass


class ClientValidator:
    """
    Client validator class.
    """
    @staticmethod
    def validate(client):
        if client.name.strip() == "":
            raise ClientValidatorException("The client name cannot be empty")


class RentalValidatorException(LibraryException):
    """
    Exception class for working with rentals.
    """
    pass


class RentalValidator:
    """
    Rental validator class.
    """
    @staticmethod
    def validate(rental):
        if rental.returned_date is not None:
            raise RentalValidatorException("The returned date must be none")

    @staticmethod
    def leap_year(year):
        """
        Check if a year is a leap year.
        :param year: the year we check
        :return: True or False
        """
        if year % 400 == 0 or year % 100 != 0 and year % 4 == 0:
            return True
        return False

    @staticmethod
    def validate_date(day, month, year):
        """
        Validate the year, the month and the day in order to construct a date type.
        :param day: the day
        :param month: the month
        :param year: the year
        """
        # special for February
        if RentalValidator.leap_year(year):
            if month == 2 and day > 29:
                raise RentalValidatorException("Not a valid day")
        else:
            if month == 2 and day > 28:
                raise RentalValidatorException("Not a valid day")
        if day < 0 or month < 0:
            raise RentalValidatorException("The day and the month must be positive")
        if year < 0:
            raise RentalValidatorException("Invalid year")
        if month > 12:
            raise RentalValidatorException("The month cannot be greater than 12")
        if month in [1, 3, 5, 7, 8, 10, 12]:
            if day > 31:
                raise RentalValidatorException("Not a valid day")
        else:
            if day > 30:
                raise RentalValidatorException("Not a valid day")
