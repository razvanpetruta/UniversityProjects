package com.example.restapi.service.user_service;

import com.example.restapi.dtos.userdtos.UserDTO;
import com.example.restapi.dtos.userdtos.UserProfileDTO;

public interface IUserProfileService {
    UserDTO getUserProfile(String username);

    UserProfileDTO updateUserProfile(UserProfileDTO newUserProfile, String username, String _username);
}
