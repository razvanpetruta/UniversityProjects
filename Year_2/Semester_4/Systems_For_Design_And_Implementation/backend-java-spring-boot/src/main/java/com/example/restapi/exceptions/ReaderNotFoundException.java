package com.example.restapi.exceptions;

public class ReaderNotFoundException extends RuntimeException {
    public ReaderNotFoundException(Long id) {
        super("Could not find reader " + id);
    }
}
