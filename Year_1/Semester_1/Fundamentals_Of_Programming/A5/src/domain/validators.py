class BookValidatorException(Exception):
    """
    Class for raising BookValidatorExceptions.
    """
    pass


class BookValidator:
    @staticmethod
    def validate(book):
        """
        Raise values if the book doesn't fulfill the specifications.
        :param book: the book to be verified
        """
        if len(book.title) == 0 or len(book.author) == 0:
            raise BookValidatorException("Invalid book title or author")
