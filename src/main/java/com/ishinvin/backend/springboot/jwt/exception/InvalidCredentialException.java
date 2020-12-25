package com.ishinvin.backend.springboot.jwt.exception;

public class InvalidCredentialException extends RuntimeException {

    public InvalidCredentialException(String msg) {
        super(msg);
    }

}
