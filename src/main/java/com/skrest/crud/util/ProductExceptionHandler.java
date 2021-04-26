package com.skrest.crud.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ProductExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleInvalidInputException(IllegalArgumentException exception){
        LOGGER.error(String.format("Invalid Input Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(HttpClientErrorException.Unauthorized exception){
        LOGGER.error(String.format("Unauthorized Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception){
        LOGGER.error(String.format("Resource Not Found Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResponseStatusException exception){
        LOGGER.error(String.format("Not Found Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceAccessException.class})
    public ResponseEntity<Object> handleResourceAccessException(ResourceAccessException exception){
        LOGGER.error(String.format("Resource Access Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ServerErrorException.class})
    public ResponseEntity<Object> handleServerException(ServerErrorException exception, String msg){
        LOGGER.error(String.format("Internal Server Exception:  %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementException exception){
        LOGGER.error(String.format("No Element Found Exception: %s ", exception.getMessage()));
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
