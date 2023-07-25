package com.taru.eventmanagement.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {

    private String statusCode;
    private String message;
    private Date timestamp;
}