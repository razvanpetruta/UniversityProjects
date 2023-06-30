package com.example.restapi.dtos.userdtos;

import com.example.restapi.model.user.User;
import com.example.restapi.model.user.UserProfile;
import org.modelmapper.ModelMapper;

public class UserDTO_Converters {
    public static UserProfileDTO convertToUserProfileDTO(UserProfile userProfile, ModelMapper modelMapper) {
        return modelMapper.map(userProfile, UserProfileDTO.class);
    }

    public static UserDTO convertToUserDTO(User user, ModelMapper modelMapper, Long totalLibraries, Long totalBooks, Long totalReaders) {
        UserDTO userDTO = new UserDTO();
        userDTO.setID(user.getID());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(user.getRoles());
        userDTO.setUserProfile(UserDTO_Converters.convertToUserProfileDTO(user.getUserProfile(), modelMapper));
        userDTO.setTotalLibraries(totalLibraries);
        userDTO.setTotalBooks(totalBooks);
        userDTO.setTotalReaders(totalReaders);
        return userDTO;
    }
}
