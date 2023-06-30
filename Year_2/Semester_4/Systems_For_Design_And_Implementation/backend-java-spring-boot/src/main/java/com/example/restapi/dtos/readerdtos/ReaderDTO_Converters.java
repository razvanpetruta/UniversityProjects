package com.example.restapi.dtos.readerdtos;

import com.example.restapi.dtos.librarydtos.LibraryDTO_withMembership;
import com.example.restapi.exceptions.LibraryNotFoundException;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.repository.library_repository.LibraryRepository;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class ReaderDTO_Converters {
    public static ReaderDTO_forAll convertToReaderDTO_forAll(Reader reader, Long countReaders) {
        ReaderDTO_forAll newReader = new ReaderDTO_forAll();
        newReader.setID(reader.getID());
        newReader.setName(reader.getName());
        newReader.setEmail(reader.getEmail());
        newReader.setStudent(reader.isStudent());
        newReader.setGender(reader.getGender());
        newReader.setBirthDate(reader.getBirthDate());
        newReader.setTotalLibraries(countReaders);
        newReader.setUsername(reader.getUser().getUsername());
        return newReader;
    }

    public static ReaderDTO_forOne convertToReaderDTO_forOne(Reader reader, ModelMapper modelMapper, LibraryRepository libraryRepository) {
        ReaderDTO_forOne readerDTO = modelMapper.map(reader, ReaderDTO_forOne.class);
        Set<LibraryDTO_withMembership> libraries = reader.getMemberships().stream()
                .map((membership -> {
                    Library library = libraryRepository.findById(membership.getID().getLibraryID()).orElseThrow(() -> new LibraryNotFoundException(membership.getID().getLibraryID()));
                    LibraryDTO_withMembership libraryDTO = new LibraryDTO_withMembership();
                    libraryDTO.setID(library.getID());
                    libraryDTO.setName(library.getName());
                    libraryDTO.setEmail(library.getEmail());
                    libraryDTO.setAddress(library.getAddress());
                    libraryDTO.setWebsite(library.getWebsite());
                    libraryDTO.setYearOfConstruction(library.getYearOfConstruction());
                    libraryDTO.setStartDate(membership.getStartDate());
                    libraryDTO.setEndDate(membership.getEndDate());
                    return libraryDTO;
                })).collect(Collectors.toSet());
        readerDTO.setLibraries(libraries);
        readerDTO.setUsername(reader.getUser().getUsername());
        return readerDTO;
    }
}
