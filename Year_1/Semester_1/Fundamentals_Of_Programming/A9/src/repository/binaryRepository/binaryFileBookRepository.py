from repository.bookRepository import BookRepository
from repository.dataAccessEntity import BookDataAccess


class BinaryFileBookRepository(BookRepository):
    """
    Class for book repository specific to a binary file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        data = BookDataAccess.read_from_binary_file(self.__file_path)
        for key in data:
            super().save(data[key])

    def save(self, book):
        super().save(book)
        BookDataAccess.write_in_binary_file(self.__file_path, self.books)

    def delete_by_id(self, book_id):
        deleted_book = super().delete_by_id(book_id)
        BookDataAccess.write_in_binary_file(self.__file_path, self.books)
        return deleted_book

    def update_by_id(self, book_id, new_title, new_author):
        updated_book = super().update_by_id(book_id, new_title, new_author)
        BookDataAccess.write_in_binary_file(self.__file_path, self.books)
        return updated_book
