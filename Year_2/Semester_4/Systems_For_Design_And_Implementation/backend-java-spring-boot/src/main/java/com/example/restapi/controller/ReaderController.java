package com.example.restapi.controller;

import com.example.restapi.dtos.readerdtos.ReaderDTO_forAll;
import com.example.restapi.dtos.readerdtos.ReaderDTO_forOne;
import com.example.restapi.model.reader.Reader;
import com.example.restapi.model.user.User;
import com.example.restapi.security.jwt.JwtUtils;
import com.example.restapi.service.reader_service.IReaderService;

import javax.validation.Valid;

import com.example.restapi.service.user_service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "http://localhost:4200", "https://sdi-library-management.netlify.app" }, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class ReaderController {
    private final IReaderService readerService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public ReaderController(IReaderService readerService, UserService userService, JwtUtils jwtUtils) {
        this.readerService = readerService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/readers")
    ResponseEntity<List<ReaderDTO_forAll>> allReaders(@RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(defaultValue = "25") Integer pageSize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.readerService.getAllReaders(pageNo, pageSize));
    }

    @GetMapping("/readers/count")
    ResponseEntity<Long> countReaders() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.readerService.countAllReaders());
    }

    @GetMapping("/readers/{id}")
    ResponseEntity<ReaderDTO_forOne> oneReader(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.readerService.getReaderById(id));
    }

    @PostMapping("/readers")
    ResponseEntity<Reader> newReader(@Valid @RequestBody Reader newReader,
                                     @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.readerService.addNewReader(newReader, user.getID()));
    }

    @PutMapping("/readers/{readerID}")
    ResponseEntity<Reader> replaceReader(@Valid @RequestBody Reader newReader,
                                         @PathVariable Long readerID,
                                         @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.readerService.replaceReader(newReader, readerID, user.getID()));
    }

    @DeleteMapping("/readers/{id}")
    ResponseEntity<HttpStatus> deleteReader(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        this.readerService.deleteReader(id, user.getID());
        return ResponseEntity.accepted().body(HttpStatus.OK);
    }
}
