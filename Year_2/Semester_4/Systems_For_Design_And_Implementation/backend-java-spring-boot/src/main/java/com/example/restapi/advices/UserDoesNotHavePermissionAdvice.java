package com.example.restapi.advices;

import com.example.restapi.exceptions.ReaderNotFoundException;
import com.example.restapi.exceptions.UserDoesNotHavePermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserDoesNotHavePermissionAdvice {
    @ResponseBody
    @ExceptionHandler(UserDoesNotHavePermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String libraryNotFoundHandler(UserDoesNotHavePermissionException ux) {
        return ux.getMessage();
    }
}
