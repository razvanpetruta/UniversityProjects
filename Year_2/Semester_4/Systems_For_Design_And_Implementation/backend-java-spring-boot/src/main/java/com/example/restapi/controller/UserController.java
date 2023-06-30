package com.example.restapi.controller;

import com.example.restapi.dtos.SQLRunResponseDTO;
import com.example.restapi.dtos.userdtos.UserDTO;
import com.example.restapi.dtos.userdtos.UserProfileDTO;
import com.example.restapi.dtos.userdtos.UserRolesDTO;
import com.example.restapi.dtos.userdtos.UserWithRolesDTO;
import com.example.restapi.model.user.UserSettings;
import com.example.restapi.model.user.User;
import com.example.restapi.repository.user_repository.UserSettingsRepository;
import com.example.restapi.security.jwt.JwtUtils;
import com.example.restapi.service.user_service.IUserProfileService;
import com.example.restapi.service.user_service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = { "http://localhost:4200", "https://sdi-library-management.netlify.app" }, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    private final IUserProfileService userProfileService;
    private final UserSettingsRepository userSettingsRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    UserController(IUserProfileService userProfileService, UserSettingsRepository userSettingsRepository, UserService userService, JwtUtils jwtUtils) {
        this.userProfileService = userProfileService;
        this.userSettingsRepository = userSettingsRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/user-profile/{username}")
    UserDTO getProfile(@PathVariable String username) {
        return this.userProfileService.getUserProfile(username);
    }

    @GetMapping("/user-settings")
    ResponseEntity<Integer> getElementsPerPage() {
        return
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(this.userSettingsRepository.getById(1L).getElementsPerPage());
    }

    @PostMapping("/user-settings")
    ResponseEntity<?> setElementsPerPage(@RequestBody UserSettings settings) {
        UserSettings userSettings = this.userSettingsRepository.getById(1L);
        userSettings.setElementsPerPage(settings.getElementsPerPage());
        this.userSettingsRepository.save(userSettings);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SQLRunResponseDTO("Successfully updated the number of elements per page"));
    }

    @GetMapping("/users-search")
    ResponseEntity<List<UserWithRolesDTO>> getUsersByUsername(@RequestParam(required = false) String username,
                                                              @RequestHeader("Authorization") String token) {
        String _username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(_username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.getTop20UsersByUsername(username, user.getID()));
    }

    @PutMapping("/user-profile/{username}")
    UserProfileDTO updateUser(@Valid @RequestBody UserProfileDTO newUserProfile,
                              @PathVariable String username,
                              @RequestHeader("Authorization") String token) {
        String _username = this.jwtUtils.getUserNameFromJwtToken(token);

        return this.userProfileService.updateUserProfile(newUserProfile, username, _username);
    }

    @PutMapping("/user-update-roles/{username}")
    UserWithRolesDTO updateUserRoles(@RequestBody UserRolesDTO roles,
                                     @PathVariable String username,
                                     @RequestHeader("Authorization") String token) {
        String _username = this.jwtUtils.getUserNameFromJwtToken(token);

        return this.userService.updateUserRoles(username, roles, _username);
    }
}
