package com.example.restapi.service.library_service;

import com.example.restapi.dtos.bookdtos.BookDTO_Converters;
import com.example.restapi.dtos.bookdtos.BookDTO_onlyLibraryID;
import com.example.restapi.dtos.librarydtos.LibrariesCountDTO;
import com.example.restapi.dtos.librarydtos.LibraryDTO_Converters;
import com.example.restapi.dtos.librarydtos.LibraryDTO_allBooks;
import com.example.restapi.dtos.librarydtos.LibraryDTO_noBooks;
import com.example.restapi.exceptions.LibraryNotFoundException;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.exceptions.UserNotFoundException;
import com.example.restapi.model.library.Library;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
import com.example.restapi.repository.book_repository.BookRepository;
import com.example.restapi.repository.library_repository.LibraryRepository;
import com.example.restapi.repository.membership_repository.MembershipRepository;
import com.example.restapi.repository.reader_repository.ReaderRepository;
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
public class LibraryService implements ILibraryService {
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final ReaderRepository readerRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public LibraryService(BookRepository bookRepository, LibraryRepository libraryRepository, ReaderRepository readerRepository, MembershipRepository membershipRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.readerRepository = readerRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LibraryDTO_noBooks> getAllLibraries(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("ID"));

        return this.libraryRepository.findAll(pageable).getContent().stream().map(
                library -> LibraryDTO_Converters.convertToLibraryDTO_noBooks(library,
                        this.membershipRepository.countByLibraryID(library.getID()),
                        this.bookRepository.countByLibraryID(library.getID()))
        ).collect(Collectors.toList());
    }

    @Override
    public long countAllLibraries() {
        return this.libraryRepository.count();
    }

    @Override
    public List<BookDTO_onlyLibraryID> getAllBooksFromLibrary(Long id) {
        return this.bookRepository.findByLibraryID(id).stream().map(
                (book) -> BookDTO_Converters.convertToBookDTO_onlyLibraryID(book, this.modelMapper)
        ).collect(Collectors.toList());
    }

    @Override
    public Library addNewLibrary(Library newLibrary, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                        role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );

        if (!userOrModOrAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "add a new library", user.getUsername()));
        }

        newLibrary.setUser(user);
        user.addLibrary(newLibrary);

        return this.libraryRepository.save(newLibrary);
    }

    @Override
    public LibraryDTO_allBooks getLibraryById(Long id) {
        return LibraryDTO_Converters.convertToLibraryDTO_forOne(this.libraryRepository.findById(id).orElseThrow(() ->
                new LibraryNotFoundException(id)), modelMapper, readerRepository);
    }

    @Override
    public Library replaceLibrary(Library newLibrary, Long libraryID, Long userID) {
        Library library = this.libraryRepository.findById(libraryID).orElseThrow(() -> new LibraryNotFoundException(libraryID));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER
        );

        if (!isUser) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "add a new library", user.getUsername()));
        }

        if (!Objects.equals(user.getID(), library.getUser().getID())) {
            boolean modOrAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "update library %s", user.getUsername(), library.getName()));
            }
        }

        library.setName(newLibrary.getName());
        library.setEmail(newLibrary.getEmail());
        library.setAddress(newLibrary.getAddress());
        library.setWebsite(newLibrary.getWebsite());
        library.setYearOfConstruction(newLibrary.getYearOfConstruction());
        library.setUser(user);

        return this.libraryRepository.save(library);
    }

    @Override
    public void deleteLibrary(Long id, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "delete library %s", user.getUsername(), id));
        }

        this.libraryRepository.deleteById(id);
    }

    @Override
    public List<Reader> getReadersFromLibrary(Long libraryID) {
        return this.readerRepository.findReadersByMembershipsLibraryID(libraryID);
    }

    @Override
    public List<LibrariesCountDTO> getLibrariesWithNumberOfBooksDesc() {
        return this.libraryRepository.findLibrariesGroupByCountBooksDesc();
    }

    @Override
    public List<LibrariesCountDTO> getLibrariesWithNumberOfStudentReadersDesc() {
        return this.libraryRepository.findLibrariesGroupByCountStudentReadersDesc();
    }

    @Override
    public List<Library> searchLibrariesByNameFullText(String name) {
        return this.libraryRepository.findTop20BySearchTerm(name);
    }
}
