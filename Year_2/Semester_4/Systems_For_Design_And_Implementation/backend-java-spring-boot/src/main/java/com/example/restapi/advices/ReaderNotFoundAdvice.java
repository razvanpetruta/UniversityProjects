package com.example.restapi.advices;

import com.example.restapi.exceptions.ReaderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReaderNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ReaderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String libraryNotFoundHandler(ReaderNotFoundException rx) {
        return rx.getMessage();
    }
}
