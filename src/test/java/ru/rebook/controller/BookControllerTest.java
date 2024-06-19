package ru.rebook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rebook.config.SecurityConfiguration;
import ru.rebook.controller.exception.ApplicationExceptionControllerAdvice;
import ru.rebook.exception.BookNotFoundException;
import ru.rebook.model.dto.BookFullInformationDto;
import ru.rebook.model.request.BookAddRequest;
import ru.rebook.model.request.RatingRequest;
import ru.rebook.security.JwtService;
import ru.rebook.service.BookService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @MockBean
    BookService bookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/book?authorId=1: получение книг автора")
    void getsBookByAuthorId_RequestIsValid_Returns200() throws Exception{
        String authorId = "1";
        mockMvc.perform(get("/api/v1/book")
                .param("authorId", authorId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/book?authorId=1: автор не найден")
    void getsBookByAuthorId_NonExistentAuthor_Returns404() throws Exception{
        String authorId = "1";
        String page = "0";
        String size = "10";
        when(bookService.getBooksByAuthorId(1L,0, 10)).thenThrow(BookNotFoundException.class);
        mockMvc.perform(get("/api/v1/book")
                        .param("authorId", authorId)
                        .param("page", page)
                        .param("size", size))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/book/{bookId}: получение подробной информации о книге")
    void getBookFullInformationDto_RequestIsValid_Returns200() throws Exception {
        String bookId = "1";
        BookFullInformationDto response = new BookFullInformationDto();
        when(bookService.getBookFullInformationDto(1L)).thenReturn(response);
        mockMvc.perform(get("/api/v1/book/" + bookId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/book/{bookId}: книга не найдена")
    void giveRating_NonExistentBook_Returns404() throws Exception {
        String bookId = "1";
        when(bookService.getBookFullInformationDto(1L)).thenThrow(BookNotFoundException.class);
        mockMvc.perform(get("/api/v1/book/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/book/rating: выставление книги оценки")
    @WithMockUser
    void giveRating_UserIsAuthorized_Returns200() throws Exception {
        String request = objectMapper.writeValueAsString(new RatingRequest(1L, 8.0));
        mockMvc.perform(post("/api/v1/book/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/book/rating: пользователь не авторизован")
    void giveRating_UserIsUnauthorized_Returns403() throws Exception {
        String request = objectMapper.writeValueAsString(new RatingRequest(1L, 8.0));
        mockMvc.perform(post("/api/v1/book/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/book/add: пользователь не является админом")
    @WithMockUser(roles = "USER")
    void addBook_UserIsNotAdmin_Returns403() throws Exception {
        String request = objectMapper.writeValueAsString(new BookAddRequest(
                "Назввание",
                "Описание",
                List.of(1L),
                List.of(1L),
                List.of(1L, 2L)
                ));
        mockMvc.perform(post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/v1/book/add: некорректное тело запроса")
    @WithMockUser(roles = "ADMIN")
    void addBook_RequestIsInvalid_Returns400() throws Exception {
        String request = objectMapper.writeValueAsString(new BookAddRequest(
                null,
                "Описание",
                List.of(1L),
                List.of(1L),
                List.of(1L, 2L)
        ));
        mockMvc.perform(post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/book/add: добавление новой книги")
    @WithMockUser(roles = "ADMIN")
    void addBook_RequestIsValid_Returns200() throws Exception {
        String request = objectMapper.writeValueAsString(new BookAddRequest(
                "Название",
                "Описание",
                List.of(1L),
                List.of(1L),
                List.of(1L, 2L)
        ));
        mockMvc.perform(post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}