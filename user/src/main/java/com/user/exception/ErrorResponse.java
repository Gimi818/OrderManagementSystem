package com.user.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus status) {
}
