package com.user.dto;

public record UserRegistrationDto(
        String email,
        String fullName,
        String address,
        String password,
        String repeatedPassword
) {
}
