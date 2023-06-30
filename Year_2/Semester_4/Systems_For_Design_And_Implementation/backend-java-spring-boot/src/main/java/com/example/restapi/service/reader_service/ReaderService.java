package com.example.restapi.service.reader_service;

import com.example.restapi.dtos.readerdtos.ReaderDTO_forAll;
import com.example.restapi.dtos.readerdtos.ReaderDTO_forOne;
import com.example.restapi.dtos.readerdtos.ReaderDTO_Converters;
import com.example.restapi.exceptions.ReaderNotFoundException;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.exceptions.UserNotFoundException;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
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
public class ReaderService implements IReaderService {
    private final ReaderRepository readerRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LibraryRepository libraryRepository;

    public ReaderService(ReaderRepository readerRepository, MembershipRepository membershipRepository, UserRepository userRepository, ModelMapper modelMapper, LibraryRepository libraryRepository) {
        this.readerRepository = readerRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.libraryRepository = libraryRepository;
    }

    @Override
    public List<ReaderDTO_forAll> getAllReaders(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("ID"));

        return this.readerRepository.findAll(pageable).getContent().stream().map(
                reader -> ReaderDTO_Converters.convertToReaderDTO_forAll(reader,
                        this.membershipRepository.countByReaderID(reader.getID()))
        ).collect(Collectors.toList());
    }

    @Override
    public Reader addNewReader(Reader newReader, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );

        if (!userOrModOrAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "add a new reader", user.getUsername()));
        }

        newReader.setUser(user);
        user.addReader(newReader);

        return this.readerRepository.save(newReader);
    }

    @Override
    public ReaderDTO_forOne getReaderById(Long id) {
        return ReaderDTO_Converters.convertToReaderDTO_forOne(this.readerRepository.findById(id).orElseThrow(()
                -> new ReaderNotFoundException(id)), modelMapper, libraryRepository);
    }

    @Override
    public Reader replaceReader(Reader newReader, Long readerID, Long userID) {
        Reader reader = this.readerRepository.findById(readerID).orElseThrow(() -> new ReaderNotFoundException(readerID));
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER
        );

        if (!isUser) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "update the reader %s", user.getUsername(), reader.getName()));
        }

        if (!Objects.equals(user.getID(), reader.getUser().getID())) {
            boolean modOrAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "update reader %s", user.getUsername(), reader.getName()));
            }
        }

        reader.setName(newReader.getName());
        reader.setGender(newReader.getGender());
        reader.setStudent(newReader.isStudent());
        reader.setBirthDate(newReader.getBirthDate());
        reader.setEmail(newReader.getEmail());
        reader.setUser(user);

        return this.readerRepository.save(reader);
    }

    @Override
    public void deleteReader(Long id, Long userID) {
        User user = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );

        if (!isAdmin) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "delete reader %s", user.getUsername(), id));
        }

        this.readerRepository.deleteById(id);
    }

    @Override
    public long countAllReaders() {
        return this.readerRepository.count();
    }
}
