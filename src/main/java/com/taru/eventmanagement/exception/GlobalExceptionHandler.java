package com.taru.eventmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, WebRequest request, Model model) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value() + " - Bad request!");
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        model.addAttribute("errorObject", errorObject);

        return "error";
    }

    @ExceptionHandler(MyNotFoundException.class)
    public String handleUserNotFoundException(MyNotFoundException ex, WebRequest request, Model model) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value() + " - Not found!");
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        model.addAttribute("errorObject", errorObject);

        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleUserAccessDeniedException(AccessDeniedException ex, WebRequest request, Model model) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value() + " - Not found!");
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        model.addAttribute("errorObject", errorObject);

        return "error";
    }
}