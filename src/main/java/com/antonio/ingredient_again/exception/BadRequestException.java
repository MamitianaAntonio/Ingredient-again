package com.antonio.ingredient_again.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super();
    }
}
