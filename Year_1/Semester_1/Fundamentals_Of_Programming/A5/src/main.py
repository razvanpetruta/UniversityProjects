from src import test
from src.domain.validators import BookValidator
from src.services.services import BookService, Generator
from src.ui.ui import Console, InputOutput


if __name__ == "__main__":
    test.run_all_tests()
    book_validator = BookValidator()
    initial_entry = Generator.generate_entries()
    book_service = BookService(initial_entry, book_validator)
    input_output = InputOutput()
    console = Console(initial_entry, book_service, input_output)
    console.run()
