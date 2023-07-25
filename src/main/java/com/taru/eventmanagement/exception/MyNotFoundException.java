package com.taru.eventmanagement.exception;

public class MyNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 2;

    public MyNotFoundException(String message){
        super(message);
    }
}