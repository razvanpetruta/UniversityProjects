package com.example.restapi.exceptions;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(Long id) {
        super("Could not find user profile " + id);
    }
}