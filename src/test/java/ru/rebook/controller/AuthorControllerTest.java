package ru.rebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.rebook.model.request.AuthorAddRequest;
import ru.rebook.service.AuthorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @MockBean
    AuthorService bookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    AuthorAddRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AuthorAddRequest(
                "Александр",
                "Пушкин",
                "Сергеевич",
                "06.06.1799"
        );
    }

    @Test
    @DisplayName("POST /api/v1/author/add: пользователь не является админом")
    @WithMockUser(roles = "USER")
    void addAuthor_UserIsNotAdmin_Returns403() throws Exception {
        String request = objectMapper.writeValueAsString(validRequest);
        mockMvc.perform(post("/api/v1/author/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/author/add: некорректное тело запроса")
    @WithMockUser(roles = "ADMIN")
    void addAuthor_RequestIsInvalid_Returns400() throws Exception {
        AuthorAddRequest invalidRequest = new AuthorAddRequest(
                "",
                "Пушкин",
                "Сергеевич",
                "06/06/1799"
        );
        String request = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/api/v1/author/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/author/add: добавление нового автора")
    @WithMockUser(roles = "ADMIN")
    void addAuthor_RequestIsValid_Returns201() throws Exception {
        String request = objectMapper.writeValueAsString(validRequest);
        mockMvc.perform(post("/api/v1/author/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}