package com.taru.eventmanagement.exception;

import java.io.Serial;

public class MyNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 2;

    public MyNotFoundException(String message){
        super(message);
    }
}