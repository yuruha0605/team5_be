package com.example.team5_be.common.exception;

public class DuplicateUserIdException extends RuntimeException {
    
    public DuplicateUserIdException(String message) {
        super(message);
    }
}
