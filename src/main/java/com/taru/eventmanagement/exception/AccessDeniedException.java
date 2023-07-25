package com.taru.eventmanagement.exception;

import java.io.Serial;

public class AccessDeniedException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 3;

    public AccessDeniedException(String message){
        super(message);
    }
}