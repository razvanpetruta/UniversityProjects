package com.example.restapi.exceptions;

public class LibraryNotFoundException extends RuntimeException {
    public LibraryNotFoundException(Long id) {
        super("Could not find library " + id);
    }
}
