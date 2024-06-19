package ru.rebook.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rebook.exception.AuthorNotFoundException;
import ru.rebook.exception.FailedAuthorAddException;
import ru.rebook.model.entity.Author;
import ru.rebook.model.mapper.AuthorFullInformationDtoMapper;
import ru.rebook.model.request.AuthorAddRequest;
import ru.rebook.repository.AuthorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorFullInformationDtoMapper authorFullInformationDtoMapper;

    @InjectMocks
    AuthorService authorService;

    @Test
    void addAuthor_AuthorAlreadyExisted_ThrowsFailedAuthorAddException() {
        AuthorAddRequest request = new AuthorAddRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "10.10.1960"
        );
        when(authorRepository.exists(any())).thenReturn(true);
        Assertions.assertThrows(
                FailedAuthorAddException.class,
                () -> authorService.addAuthor(request)
        );
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void addAuthor_ValidRequest_ReturnsAuthorId() {
        AuthorAddRequest request = new AuthorAddRequest(
                "Иван",
                "Иванов",
                "Иванович",
                "10.10.1960"
        );
        when(authorRepository.exists(any())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(new Author());
        Assertions.assertDoesNotThrow(
                () -> authorService.addAuthor(request)
        );
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void getAuthorFullInformationDto_InvalidRequest_ThrowsAuthorNotFoundException() {
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.getAuthorFullInformationDto(authorId)
        );
    }
}