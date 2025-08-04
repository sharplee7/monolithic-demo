package com.monolithicbank.common.exception;

import org.springframework.http.HttpStatus;

public class SystemException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus;

    public SystemException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public SystemException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}