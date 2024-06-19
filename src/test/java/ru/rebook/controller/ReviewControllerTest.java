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
import ru.rebook.model.request.ReviewAddRequest;
import ru.rebook.service.ReviewService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @MockBean
    ReviewService bookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ReviewAddRequest validReviewAddRequest;

    @BeforeEach
    void setUp() {
        validReviewAddRequest = new ReviewAddRequest(
                "Заголовок",
                "Текст отзыва",
                1L
        );
    }

    @Test
    @DisplayName("POST /api/v1/review/add: пользователь не авторизован")
    void addReview_UserIsUnauthorized_Returns403() throws Exception {
        String request = objectMapper.writeValueAsString(validReviewAddRequest);
        mockMvc.perform(post("/api/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("POST /api/v1/review/add: некорректное тело запроса")
    @WithMockUser(roles = "USER")
    void addReview_RequestIsInvalid_Returns400() throws Exception {
        ReviewAddRequest invalidRequest = new ReviewAddRequest(
                "",
                "Текст отзыва",
                1L
        );
        String request = objectMapper.writeValueAsString(invalidRequest);
        mockMvc.perform(post("/api/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}