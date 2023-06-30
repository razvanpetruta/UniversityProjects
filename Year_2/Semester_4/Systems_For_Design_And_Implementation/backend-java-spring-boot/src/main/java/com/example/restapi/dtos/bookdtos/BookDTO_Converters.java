package com.example.restapi.dtos.bookdtos;

import com.example.restapi.model.book.Book;
import org.modelmapper.ModelMapper;

public class BookDTO_Converters {
    public static BookDTO_onlyLibraryID convertToBookDTO_onlyLibraryID(Book book, ModelMapper modelMapper) {
        BookDTO_onlyLibraryID bookDTO = modelMapper.map(book, BookDTO_onlyLibraryID.class);
        bookDTO.setLibraryID(book.getLibrary().getID());
        bookDTO.setUsername(book.getUser().getUsername());
        return bookDTO;
    }

    public static BookDTO_wholeLibrary convertToBookDTO_wholeLibrary(Book book, ModelMapper modelMapper) {
        BookDTO_wholeLibrary bookDTO = modelMapper.map(book, BookDTO_wholeLibrary.class);
        bookDTO.setUsername(book.getUser().getUsername());
        return bookDTO;
    }
}
