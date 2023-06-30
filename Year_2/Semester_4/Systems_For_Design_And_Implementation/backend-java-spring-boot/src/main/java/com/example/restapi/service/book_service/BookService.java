package com.example.restapi.service.book_service;

import com.example.restapi.dtos.bookdtos.BookDTO_Converters;
import com.example.restapi.dtos.bookdtos.BookDTO_onlyLibraryID;
import com.example.restapi.dtos.bookdtos.BookDTO_wholeLibrary;
import com.example.restapi.exceptions.BookNotFoundException;
import com.example.restapi.exceptions.LibraryNotFoundException;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.exceptions.UserNotFoundException;
import com.example.restapi.model.book.Book;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
import com.example.restapi.repository.book_repository.BookRepository;
import com.example.restapi.repository.library_repository.LibraryRepository;
import com.example.restapi.repository.user_repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, LibraryRepository libraryRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookDTO_onlyLibraryID> getAllBooks(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("ID"));

        return this.bookRepository.findAll(pageable).getContent().stream().map(
                (book) -> BookDTO_Converters.convertToBookDTO_onlyLibraryID(book, this.modelMapper)
        ).collect(Collectors.toList());
    }

    @Override
    public Book addNewBook(Book newBook, Long libraryID, Long userID) {
        Library library = this.libraryRepository.findById(libraryID).orElseThrow(() -> new LibraryNotFoundException(libraryID));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );

        if (!userOrModOrAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "add a new book", user.getUsername()));
        }

        newBook.setLibrary(library);
        library.addBook(newBook);

        newBook.setUser(user);
        user.addBook(newBook);

        return this.bookRepository.save(newBook);
    }

    @Override
    public BookDTO_wholeLibrary getBookById(Long id) {
        return BookDTO_Converters.convertToBookDTO_wholeLibrary(this.bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException(id)), this.modelMapper);
    }

    @Override
    public Book replaceBook(Book bookDTO, Long bookID, Long userID) {
        Book book = this.bookRepository.findById(bookID).orElseThrow(() -> new BookNotFoundException(bookID));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER
        );

        if (!isUser) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "add a new book", user.getUsername()));
        }

        if (!Objects.equals(user.getID(), book.getUser().getID())) {
            boolean modOrAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "update book %s", user.getUsername(), book.getTitle()));
            }
        }

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setPrice(bookDTO.getPrice());
        book.setPublishedYear(bookDTO.getPublishedYear());
        book.setDescription(bookDTO.getDescription());
        book.setUser(user);

        return this.bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id, Long userID) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "delete book %s", user.getUsername(), id));
        }

        Library library = this.libraryRepository.findById(book.getLibrary().getID()).orElseThrow(() -> new LibraryNotFoundException(book.getLibrary().getID()));

        library.removeBook(book);

        this.bookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO_onlyLibraryID> getBooksWithPriceGreater(Double price, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("price"));

        return this.bookRepository.findByPriceGreaterThanEqual(price, pageable).getContent().stream().map(
                (book) -> BookDTO_Converters.convertToBookDTO_onlyLibraryID(book, this.modelMapper)
        ).collect(Collectors.toList());
    }

    @Override
    public long countAllBooks() {
        return this.bookRepository.count();
    }

    @Override
    public long countBooksWithMinimumPrice(Double price) {
        return this.bookRepository.countByPriceGreaterThanEqual(price);
    }
}
