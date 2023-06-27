package com.example.web.controller;

import com.example.web.domain.Role;
import com.example.web.domain.User;
import com.example.web.domain.UserProfile;
import com.example.web.dtos.MessageDTO;
import com.example.web.dtos.UserCredentialsDTO;
import com.example.web.dtos.UserInfoResponse;
import com.example.web.repository.RoleRepository;
import com.example.web.repository.UserProfileRepository;
import com.example.web.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<MessageDTO> registerUser(@RequestBody UserCredentialsDTO registrationDTO) {
        // Check if the username is already taken
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDTO("Username already taken!"));
        }

        // Create a new user object
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        UserProfile userProfile = new UserProfile();
        userProfileRepository.save(userProfile);

        user.setUserProfile(userProfile);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: User role not found."));
        roles.add(userRole);

        user.setRoles(roles);

        // Save the user to the database
        userRepository.save(user);

        return ResponseEntity.ok(new MessageDTO("Successfully registered!"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> loginUser(@RequestBody UserCredentialsDTO loginDTO, HttpServletRequest request) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        // Set the authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create a new session or retrieve the existing one
        HttpSession session = request.getSession(true);
        session.setAttribute("username", loginDTO.getUsername());

        User user = this.userRepository.findByUsername(loginDTO.getUsername()).orElseThrow();
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();


        return ResponseEntity.ok(new UserInfoResponse(user.getID(), user.getUsername(), roles));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
            return ResponseEntity.ok(new MessageDTO("Successfully logged out!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageDTO("No active session"));
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<MessageDTO> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String username = (String) session.getAttribute("username");
            return ResponseEntity.ok(new MessageDTO("username: " + username));
        } else {
            return ResponseEntity.ok().body(new MessageDTO("No active session"));
        }
    }
}
