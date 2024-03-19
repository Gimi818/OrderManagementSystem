package com.cart.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus status) {
}
