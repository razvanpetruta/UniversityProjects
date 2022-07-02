from repository.bookRepository import BookRepository
from repository.dataAccessEntity import BookDataAccess


class TextFileBookRepository(BookRepository):
    """
    Class for book repository specific to a text file.
    """
    def __init__(self, validator_class, file_path):
        super().__init__(validator_class)
        self.__file_path = file_path
        self.__load_content()

    def __load_content(self):
        with open(self.__file_path) as file_pointer:
            for line in file_pointer:
                book = BookDataAccess.read_from_text_file(line)
                super().save(book)

    def __upload_content(self):
        with open(self.__file_path, "w") as file_pointer:
            BookDataAccess.write_in_text_file(self.books, file_pointer)

    def save(self, book):
        super().save(book)
        self.__upload_content()

    def delete_by_id(self, book_id):
        deleted_book = super().delete_by_id(book_id)
        self.__upload_content()
        return deleted_book

    def update_by_id(self, book_id, new_title, new_author):
        updated_book = super().update_by_id(book_id, new_title, new_author)
        self.__upload_content()
        return updated_book
