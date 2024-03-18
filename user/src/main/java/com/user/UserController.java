package com.user;

import com.user.dto.CreatedUserDto;
import com.user.dto.UserRegistrationDto;
import com.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.user.UserController.Routes.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    @PostMapping(REGISTRATION)
    public ResponseEntity<CreatedUserDto> registration(@RequestBody UserRegistrationDto registrationDto) {
        return new ResponseEntity<>(service.registration(registrationDto), HttpStatus.CREATED);
    }

    @GetMapping(Routes.FIND_USER_BY_ID)
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        UserResponseDto userResponseDto = service.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    static final class Routes {
        static final String ROOT = "/api/v1/users";
        static final String REGISTRATION = ROOT + "/registration";
        static final String CONFIRM = ROOT + "/confirm";
        static final String FIND_USER_BY_ID = ROOT + "/{id}";

    }
}
