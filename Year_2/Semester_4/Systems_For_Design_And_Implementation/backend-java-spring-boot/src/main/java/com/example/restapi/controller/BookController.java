package com.example.restapi.controller;

import com.example.restapi.dtos.bookdtos.BookDTO_onlyLibraryID;
import com.example.restapi.dtos.bookdtos.BookDTO_wholeLibrary;
import com.example.restapi.model.book.Book;
import com.example.restapi.model.user.User;
import com.example.restapi.security.jwt.JwtUtils;
import com.example.restapi.service.book_service.IBookService;

import javax.validation.Valid;

import com.example.restapi.service.user_service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:4200", "https://sdi-library-management.netlify.app" }, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class BookController {
    private final IBookService bookService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public BookController(IBookService bookService, UserService userService, JwtUtils jwtUtils) {
        this.bookService = bookService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/books")
    ResponseEntity<List<BookDTO_onlyLibraryID>> allBooks(@RequestParam(required = false) Double minPrice,
                            @RequestParam(defaultValue = "0") Integer pageNo,
                            @RequestParam(defaultValue = "25") Integer pageSize) {
        if (minPrice == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(this.bookService.getAllBooks(pageNo, pageSize));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.bookService.getBooksWithPriceGreater(minPrice, pageNo, pageSize));
    }

    @GetMapping("/books/{id}")
    ResponseEntity<BookDTO_wholeLibrary> oneBook(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.bookService.getBookById(id));
    }

    @GetMapping("/books/count")
    ResponseEntity<Long> countBooks() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.bookService.countAllBooks());
    }

    @GetMapping("/books/countByPrice")
    ResponseEntity<Long> countBooksWithMinPrice(@RequestParam Double minPrice) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.bookService.countBooksWithMinimumPrice(minPrice));
    }

    @PostMapping("/libraries/{libraryID}/books")
    ResponseEntity<Book> newBook(@Valid @RequestBody Book newBook,
                                 @PathVariable Long libraryID,
                                 @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.bookService.addNewBook(newBook, libraryID, user.getID()));
    }

    @PutMapping("/books/{bookID}")
    ResponseEntity<Book> replaceBook(@Valid @RequestBody Book book,
                                     @PathVariable Long bookID,
                                     @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.bookService.replaceBook(book, bookID, user.getID()));
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        this.bookService.deleteBook(id, user.getID());
        return ResponseEntity.accepted().body(HttpStatus.OK);
    }
}
