package com.example.restapi.service.library_service;

import com.example.restapi.dtos.bookdtos.BookDTO_onlyLibraryID;
import com.example.restapi.dtos.librarydtos.LibrariesCountDTO;
import com.example.restapi.dtos.librarydtos.LibraryDTO_allBooks;
import com.example.restapi.dtos.librarydtos.LibraryDTO_noBooks;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.reader.Reader;

import java.util.List;

public interface ILibraryService {
    List<LibraryDTO_noBooks> getAllLibraries(Integer pageNo, Integer pageSize);

    long countAllLibraries();

    List<BookDTO_onlyLibraryID> getAllBooksFromLibrary(Long id);

    Library addNewLibrary(Library newLibrary, Long userID);

    LibraryDTO_allBooks getLibraryById(Long id);

    Library replaceLibrary(Library newLibrary, Long libraryID, Long userID);

    void deleteLibrary(Long id, Long userID);

    List<Reader> getReadersFromLibrary(Long libraryID);

    List<LibrariesCountDTO> getLibrariesWithNumberOfBooksDesc();

    List<LibrariesCountDTO> getLibrariesWithNumberOfStudentReadersDesc();

    List<Library> searchLibrariesByNameFullText(String name);
}
