package com.ishinvin.backend.springboot.jwt.exception;

public class InvalidArgumentsException extends RuntimeException {

    public InvalidArgumentsException(String msg) {
        super(msg);
    }

}
