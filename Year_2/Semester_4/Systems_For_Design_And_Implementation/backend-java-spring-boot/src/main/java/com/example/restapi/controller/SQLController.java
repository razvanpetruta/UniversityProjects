package com.example.restapi.controller;

import com.example.restapi.dtos.SQLRunResponseDTO;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.User;
import com.example.restapi.security.jwt.JwtUtils;
import com.example.restapi.service.user_service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@CrossOrigin(origins = { "http://localhost:4200", "https://sdi-library-management.netlify.app" }, allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class SQLController {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public SQLController(JdbcTemplate jdbcTemplate, UserService userService, JwtUtils jwtUtils) {
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/run-delete-books-script")
    ResponseEntity<?> deleteAllBooks(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "delete all books", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\delete_books.sql"));
//            String sql = Files.readString(Paths.get(currentDir + "/src/main/java/com/example/restapi/sql_scripts/delete_books.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all books"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-delete-libraries-script")
    ResponseEntity<?> deleteAllLibraries(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "delete all libraries", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\delete_libraries.sql"));
//            String sql = Files.readString(Paths.get(currentDir + "/src/main/java/com/example/restapi/sql_scripts/delete_libraries.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all libraries"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you deleted memberships first)"));
        }
    }

    @PostMapping("/run-delete-memberships-script")
    ResponseEntity<?> deleteAllMemberships(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "delete all memberships", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\delete_memberships.sql"));
//            String sql = Files.readString(Paths.get(currentDir + "/src/main/java/com/example/restapi/sql_scripts/delete_memberships.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all memberships"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-delete-readers-script")
    ResponseEntity<?> deleteAllReaders(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "delete all readers", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String sql = Files.readString(Paths.get(currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\delete_readers.sql"));
//            String sql = Files.readString(Paths.get(currentDir + "/src/main/java/com/example/restapi/sql_scripts/delete_readers.sql"));
            jdbcTemplate.update(sql);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully deleted all readers"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you deleted memberships first)"));
        }
    }

    @PostMapping("/run-insert-libraries-script")
    ResponseEntity<?> insertAllLibraries(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "insert all libraries", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\insert_libraries.sql";
//            String fullPath = currentDir + "/src/main/java/com/example/restapi/sql_scripts/insert_libraries.sql";
            BufferedReader reader = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                jdbcTemplate.update(line);
            }
            reader.close();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all libraries"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-insert-books-script")
    ResponseEntity<?> insertAllBooks(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "insert all books", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\insert_books.sql";
//            String fullPath = currentDir + "/src/main/java/com/example/restapi/sql_scripts/insert_books.sql";
            BufferedReader reader = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                jdbcTemplate.update(line);
            }
            reader.close();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all books"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you inserted the libraries first)"));
        }
    }

    @PostMapping("/run-insert-readers-script")
    ResponseEntity<?> insertAllReaders(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "insert all readers", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\insert_readers.sql";
//            String fullPath = currentDir + "/src/main/java/com/example/restapi/sql_scripts/insert_readers.sql";
            BufferedReader reader = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                jdbcTemplate.update(line);
            }
            reader.close();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all readers"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong"));
        }
    }

    @PostMapping("/run-insert-memberships-script")
    ResponseEntity<?> insertAllMemberships(@RequestHeader("Authorization") String token) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(token);
            User user = this.userService.getUserByUsername(username);

            boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                    role.getName() == ERole.ROLE_ADMIN
            );

            if (!isAdmin) {
                throw new UserDoesNotHavePermissionException(String.format("%s does not have permission to " +
                        "insert all memberships", user.getUsername()));
            }

            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + "\\src\\main\\java\\com\\example\\restapi\\sql_scripts\\insert_memberships.sql";
//            String fullPath = currentDir + "/src/main/java/com/example/restapi/sql_scripts/insert_memberships.sql";
            BufferedReader reader = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                jdbcTemplate.update(line);
            }
            reader.close();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Successfully inserted all memberships"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SQLRunResponseDTO("Error: something went wrong (make sure you inserted libraries and readers first)"));
        }
    }
}
