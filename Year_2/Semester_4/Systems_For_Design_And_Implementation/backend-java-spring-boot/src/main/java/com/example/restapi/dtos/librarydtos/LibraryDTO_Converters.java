package com.example.restapi.dtos.librarydtos;

import com.example.restapi.dtos.readerdtos.ReaderDTO_withMembership;
import com.example.restapi.exceptions.ReaderNotFoundException;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.repository.reader_repository.ReaderRepository;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class LibraryDTO_Converters {
    public static LibraryDTO_noBooks convertToLibraryDTO_noBooks(Library library, Long countReaders, Long countBooks) {
        LibraryDTO_noBooks newLibrary = new LibraryDTO_noBooks();
        newLibrary.setID(library.getID());
        newLibrary.setName(library.getName());
        newLibrary.setEmail(library.getEmail());
        newLibrary.setAddress(library.getAddress());
        newLibrary.setWebsite(library.getWebsite());
        newLibrary.setYearOfConstruction(library.getYearOfConstruction());
        newLibrary.setTotalReaders(countReaders);
        newLibrary.setTotalBooks(countBooks);
        newLibrary.setUsername(library.getUser().getUsername());

        return newLibrary;
    }

    public static LibraryDTO_allBooks convertToLibraryDTO_forOne(Library library, ModelMapper modelMapper, ReaderRepository readerRepository) {
        LibraryDTO_allBooks libraryDTO = modelMapper.map(library, LibraryDTO_allBooks.class);
        Set<ReaderDTO_withMembership> readers = library.getMemberships().stream()
                .map(membership -> {
                    Reader reader = readerRepository.findById(membership.getID().getReaderID()).orElseThrow(() -> new ReaderNotFoundException(membership.getID().getReaderID()));
                    ReaderDTO_withMembership readerDTO = new ReaderDTO_withMembership();
                    readerDTO.setID(reader.getID());
                    readerDTO.setName(reader.getName());
                    readerDTO.setEmail(reader.getEmail());
                    readerDTO.setGender(reader.getGender());
                    readerDTO.setStudent(reader.isStudent());
                    readerDTO.setBirthDate(reader.getBirthDate());
                    readerDTO.setStartDate(membership.getStartDate());
                    readerDTO.setEndDate(membership.getEndDate());
                    return readerDTO;
                }).collect(Collectors.toSet());
        libraryDTO.setReaders(readers);
        libraryDTO.setUsername(library.getUser().getUsername());
        return libraryDTO;
    }
}
