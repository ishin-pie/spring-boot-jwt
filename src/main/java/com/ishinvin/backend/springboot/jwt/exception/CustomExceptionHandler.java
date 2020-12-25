package com.ishinvin.backend.springboot.jwt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidArgumentsException.class})
    public ResponseEntity<Object> handleInvalidArgumentsException(InvalidArgumentsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidCredentialException.class})
    public ResponseEntity<Object> handleInvalidCredentialException(InvalidCredentialException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error(ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", "Something went wrong.");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
