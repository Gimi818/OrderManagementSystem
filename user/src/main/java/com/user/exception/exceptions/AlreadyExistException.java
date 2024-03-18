package com.user.exception.exceptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}

