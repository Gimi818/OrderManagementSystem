package com.user;

import com.user.dto.CreatedUserDto;
import com.user.dto.UserRegistrationDto;
import com.user.dto.UserResponseDto;

import com.user.exception.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.user.UserService.ErrorMessages.*;


@Service
@AllArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;


    @Transactional
    public CreatedUserDto registration(UserRegistrationDto registration) {
        log.debug("Attempting to register a new user with email: {}", registration.email());
        existByMail(registration);
        passwordValidation(registration);
        User user = repository.save(mapper.dtoToEntity(registration));
        log.info("New user registered with ID: {} and Email: {}", user.getId(), user.getEmail());
        return mapper.createdEntityToDto(user);

    }

    public UserResponseDto findUserById(Long id) {
        log.debug("Searching for user by ID: {}", id);
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID, id));
        log.info("Found user with ID {}", user.getId());
        return mapper.entityToDto(user);
    }

    private void passwordValidation(UserRegistrationDto registrationDto) {
        log.debug("Validating password for user email: {}", registrationDto.email());
        if (!registrationDto.password().equals(registrationDto.repeatedPassword())) {
            log.warn("Password validation failed for user email: {}", registrationDto.email());
            throw new PasswordConflictException(PASSWORD_CONFLICT);
        }
    }

    private void existByMail(UserRegistrationDto registrationDto) {
        log.debug("Checking existence of user by email: {}", registrationDto.email());
        if (repository.existsByEmail(registrationDto.email())) {
            log.warn("User registration attempted with existing email: {}", registrationDto.email());
            throw new AlreadyExistException(EMAIL_ALREADY_TAKEN, registrationDto.email());
        }
    }

    static final class ErrorMessages {
        static final String NOT_FOUND_BY_ID = "User with id %d not found";
        static final String EMAIL_ALREADY_TAKEN = "This email %s is already taken";
        static final String PASSWORD_CONFLICT = "The passwords given aren't the same ";

    }
}
