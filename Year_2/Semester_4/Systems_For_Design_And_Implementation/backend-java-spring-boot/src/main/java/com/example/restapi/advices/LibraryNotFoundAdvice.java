package com.example.restapi.advices;

import com.example.restapi.exceptions.LibraryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LibraryNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(LibraryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String libraryNotFoundHandler(LibraryNotFoundException lx) {
        return lx.getMessage();
    }
}
