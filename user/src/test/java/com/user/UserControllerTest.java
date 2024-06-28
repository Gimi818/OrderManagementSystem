package com.user;

import com.user.dto.CreatedUserDto;
import com.user.dto.UserRegistrationDto;
import com.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {
    @MockBean
    private UserService service;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private static UserRegistrationDto userRegistrationDto;
    private static String useRequestDtoJson;
    private static CreatedUserDto createdUserDto;
    private static UserResponseDto userResponseDto;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        userRegistrationDto = new UserRegistrationDto("email@gmail.com", "Adam Nowak", "popo", "popo");
        userResponseDto = new UserResponseDto("email@gmail.com");

        useRequestDtoJson = objectMapper.writeValueAsString(userRegistrationDto);

    }


    @Test
    @DisplayName("Should save user account")
    void should_save_user() throws Exception {
        given(service.registration(userRegistrationDto)).willReturn(createdUserDto);

        mockMvc.perform(post("/api/v1/users/registration")
                        .content(useRequestDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should find user by ID")
    void should_find_user_by_id() throws Exception {
        Long id = 1L;
        given(service.findUserById(id)).willReturn(userResponseDto);

        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@gmail.com"));
    }
}
