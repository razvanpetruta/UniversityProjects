package com.example.restapi.service.user_service;

import com.example.restapi.dtos.userdtos.UserDTO;
import com.example.restapi.dtos.userdtos.UserDTO_Converters;
import com.example.restapi.dtos.userdtos.UserProfileDTO;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.exceptions.UserNotFoundException;
import com.example.restapi.exceptions.UserProfileNotFoundException;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
import com.example.restapi.model.user.UserProfile;
import com.example.restapi.repository.book_repository.BookRepository;
import com.example.restapi.repository.reader_repository.ReaderRepository;
import com.example.restapi.repository.library_repository.LibraryRepository;
import com.example.restapi.repository.user_repository.UserProfileRepository;
import com.example.restapi.repository.user_repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserProfileService implements IUserProfileService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final ModelMapper modelMapper;

    public UserProfileService(UserRepository userRepository, UserProfileRepository userProfileRepository, LibraryRepository libraryRepository, BookRepository bookRepository, ReaderRepository readerRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Long totalLibraries = this.libraryRepository.countByUserID(user.getID());
        Long totalBooks = this.bookRepository.countByUserID(user.getID());
        Long totalReaders = this.readerRepository.countByUserID(user.getID());
        return UserDTO_Converters.convertToUserDTO(user, this.modelMapper, totalLibraries, totalBooks, totalReaders);
    }

    @Override
    public UserProfileDTO updateUserProfile(UserProfileDTO newUserProfile, String username, String _username) {
        User userReq = userRepository.findByUsername(_username)
                .orElseThrow(() -> new UserNotFoundException(_username));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        boolean isUser = userReq.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER
        );

        if (!isUser) {
            throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                    "update this profile", userReq.getUsername()));
        }

        if (!Objects.equals(userReq.getID(), user.getID())) {
            boolean modOrAdmin = userReq.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN || role.getName() == ERole.ROLE_MODERATOR
            );

            if (!modOrAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "update this profile", user.getUsername()));
            }
        }

        UserProfile userProfileUpdated = userProfileRepository.findById(user.getUserProfile().getId())
                .map(userProfile -> {
                    userProfile.setBio(newUserProfile.getBio());
                    userProfile.setLocation(newUserProfile.getLocation());
                    userProfile.setGender(newUserProfile.getGender());
                    userProfile.setMaritalStatus(newUserProfile.getMaritalStatus());
                    userProfile.setBirthDate(newUserProfile.getBirthDate());
                    return userProfileRepository.save(userProfile);
                })
                .orElseThrow(() -> new UserProfileNotFoundException(user.getUserProfile().getId()));

        return UserDTO_Converters.convertToUserProfileDTO(userProfileUpdated, this.modelMapper);
    }
}
