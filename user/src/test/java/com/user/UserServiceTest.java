package com.user;

import com.user.dto.CreatedUserDto;
import com.user.dto.UserRegistrationDto;
import com.user.dto.UserResponseDto;
import com.user.encoder.PasswordEncoderService;
import com.user.exception.exceptions.AlreadyExistException;
import com.user.exception.exceptions.PasswordConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRegistrationDto userRegistrationDto;
    @Mock
    private UserResponseDto userResponseDto;
    @Mock
    private PasswordEncoderService passwordEncoderService;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private User user;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto("Gimi@gmail.com", "Adam Nowak", "qwerty", "qwerty");
        user = new User(1L, "Adam", "ab@o.com", "qwerty");
        userResponseDto = new UserResponseDto("Gimi@gmail.com");
    }

    @Test
    @DisplayName("Should save user")
    void should_save_user() {

        given(userRepository.save(user))
                .willReturn(user);

        assertThat(userService.registration(userRegistrationDto))
                .isEqualTo(userMapper.createdEntityToDto(user));
    }


    @Test
    @DisplayName("Should find user by ID")
    void should_find_user_by_id() {
        Long id = 1L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userMapper.entityToDto(user))
                .willReturn(userResponseDto);

        assertThat(userService.findUserById(id)).isEqualTo(userResponseDto);
    }

    @Test
    @DisplayName("Should throw NotSamePasswordException")
    void Should_throw_NotSamePasswordException() {
        // Given
        UserRegistrationDto requestDto = new UserRegistrationDto("john@example.com", "Ferdo", "password1", "password2");

        // When & Then
        assertThrows(PasswordConflictException.class, () -> userService.passwordValidation(requestDto));
    }


    @Test
    @DisplayName("Should throw EmailAlreadyExistsException")
    void Should_throw_EmailAlreadyExistsException() {
        // Given
        UserRegistrationDto requestDto = new UserRegistrationDto("john@example.com", "Ferdo", "password1", "password1");
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);

        // When & Then
        assertThrows(AlreadyExistException.class, () -> userService.existByMail(requestDto));
    }


}
