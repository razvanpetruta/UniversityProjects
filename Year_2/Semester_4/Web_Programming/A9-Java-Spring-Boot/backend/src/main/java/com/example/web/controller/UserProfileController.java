package com.example.web.controller;

import com.example.web.domain.User;
import com.example.web.domain.UserProfile;
import com.example.web.dtos.MessageDTO;
import com.example.web.repository.UserProfileRepository;
import com.example.web.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Validated
public class UserProfileController {
    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;


    public UserProfileController(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user-profile/{username}")
    ResponseEntity<UserProfile> getProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow();
        return ResponseEntity.ok(user.getUserProfile());
    }

    @PutMapping("/user-profile/{username}")
    ResponseEntity<?> updateUser(@Valid @RequestBody UserProfile newUserProfile,
                              @PathVariable String username, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionUsername = (String) session.getAttribute("username");

            if (!Objects.equals(sessionUsername, username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new MessageDTO("You do not have permission to update this profile"));
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow();

            UserProfile userProfileUpdated = userProfileRepository.findById(user.getUserProfile().getID())
                    .map(userProfile -> {
                        userProfile.setName(newUserProfile.getName());
                        userProfile.setEmail(newUserProfile.getEmail());
                        userProfile.setAge(newUserProfile.getAge());
                        userProfile.setImageURL(newUserProfile.getImageURL());
                        userProfile.setHomeTown(newUserProfile.getHomeTown());
                        return userProfileRepository.save(userProfile);
                    })
                    .orElseThrow();

            return ResponseEntity.ok(userProfileUpdated);
        } else {
            return ResponseEntity.ok().body(new MessageDTO("No active session"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserProfile>> searchProfiles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String homeTown,
            @RequestParam(required = false) Integer maxAge
    ) {
        List<UserProfile> profiles = userProfileRepository.findAll();

        if (!Objects.equals(name, "")) {
            profiles = profiles.stream().filter(userProfile -> userProfile.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if (!Objects.equals(email, "")) {
            profiles = profiles.stream().filter(userProfile -> userProfile.getEmail().toLowerCase().contains(email.toLowerCase())).collect(Collectors.toList());
        }
        if (!Objects.equals(homeTown, "")) {
            profiles = profiles.stream().filter(userProfile -> userProfile.getHomeTown().toLowerCase().contains(homeTown.toLowerCase())).collect(Collectors.toList());
        }
        if (maxAge != 100) {
            profiles = profiles.stream().filter(userProfile -> userProfile.getAge() <= maxAge).collect(Collectors.toList());
        }

        return ResponseEntity.ok(profiles);
    }
}
