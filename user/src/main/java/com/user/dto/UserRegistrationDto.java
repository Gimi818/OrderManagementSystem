package com.user.dto;

public record UserRegistrationDto(
        String email,
        String fullName,
        String password,
        String repeatedPassword
) {
}
