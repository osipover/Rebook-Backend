package ru.rebook.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.rebook.exception.AuthorNotFoundException;
import ru.rebook.exception.BookNotFoundException;
import ru.rebook.model.converter.AuthorConverter;
import ru.rebook.model.converter.BookCollectionConverter;
import ru.rebook.model.converter.GenreConverter;
import ru.rebook.model.dto.BookByAuthorDto;
import ru.rebook.model.dto.PageDto;
import ru.rebook.model.entity.Book;
import ru.rebook.model.entity.Rating;
import ru.rebook.model.entity.User;
import ru.rebook.model.mapper.BookByAuthorDtoMapper;
import ru.rebook.model.mapper.BookFullInformationDtoMapper;
import ru.rebook.model.request.BookAddRequest;
import ru.rebook.model.request.RatingRequest;
import ru.rebook.repository.AuthorRepository;
import ru.rebook.repository.BookRepository;
import ru.rebook.repository.RatingRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    AuthorConverter authorConverter;

    @Mock
    GenreConverter genreConverter;

    @Mock
    BookCollectionConverter bookCollectionConverter;

    @Mock
    BookByAuthorDtoMapper bookByAuthorDtoMapper;

    @Mock
    BookFullInformationDtoMapper bookFullInformationDtoMapper;

    @InjectMocks
    BookService bookService;

    List<Book> books;

    @BeforeEach
    public void setUp() {
        Book b1 = Book.builder()
                .id(1L)
                .title("Назввание 1")
                .description("Описание 1")
                .build();
        Book b2 = Book.builder()
                .id(2L)
                .title("Назввание 2")
                .description("Описание 2")
                .build();
        books = List.of(b1, b2);
    }

    @Test
    void getBooksByAuthorId_NonExistedAuthor_ThrowsAuthorNotFoundException() {
        Long authorId = 1L;
        int page = 0;
        int size = 1;
        when(authorRepository.existsById(authorId)).thenReturn(false);
        Assertions.assertThrows(
                AuthorNotFoundException.class,
                () -> bookService.getBooksByAuthorId(authorId, page, size)
        );
    }

    @Test
    void getBooksByAuthorId_RequestIsValid_ReturnsPageDtoWithBooks() {
        long authorId = 1L;
        int page = 0;
        int size = 1;
        Page<Book> pageBooks = new PageImpl<>(books, PageRequest.of(0, 1), 2);
        when(authorRepository.existsById(authorId)).thenReturn(true);
        when(bookRepository.findAllByAuthorId(any(), any())).thenReturn(pageBooks);
        when(bookByAuthorDtoMapper.apply(books.get(0))).thenReturn(new BookByAuthorDto(1L, "Название 1", 8D));
        when(bookByAuthorDtoMapper.apply(books.get(1))).thenReturn(new BookByAuthorDto(2L, "Название 2", 6D));

        PageDto<BookByAuthorDto> result = bookService.getBooksByAuthorId(authorId, page, size);

        Assertions.assertDoesNotThrow(() -> bookService.getBooksByAuthorId(authorId, page, size));
        Assertions.assertEquals(2, result.getTotalElements());
        Assertions.assertEquals(2, result.getTotalPages());
    }

    @Test
    void getBookFullInformationDto_NonExistedBook_ThrowsBookNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                BookNotFoundException.class,
                () -> bookService.getBookFullInformationDto(bookId)
        );
    }

    @Test
    void giveRatingToBook_NonExistedBook_ThrowsBookNotFoundException() {
        long bookId = 1L;
        double ratingValue = 8D;
        User user = new User();
        RatingRequest request = new RatingRequest(bookId, ratingValue);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                BookNotFoundException.class,
                () -> bookService.giveRatingToBook(request, user)
        );
    }

    @Test
    void giveRatingToBook_ValidRequest() {
        long bookId = 1L;
        double ratingValue = 10d;
        User user = new User();
        Book book = new Book();
        book.setId(bookId);
        book.setRating(6d);
        Rating rating = Rating.builder()
                .value(ratingValue)
                .book(book)
                .user(user)
                .build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(ratingRepository.averageByBookId(bookId)).thenReturn(8d);

        RatingRequest request = new RatingRequest(bookId, ratingValue);
        bookService.giveRatingToBook(request, user);

        verify(bookRepository, times(1)).findById(bookId);
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(ratingRepository, times(1)).averageByBookId(bookId);
        assertEquals(8d, book.getRating(), 0.01);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addBook_ValidAuthorized_ReturnsBookId() {
        BookAddRequest request = new BookAddRequest(
                "Название",
                "Описание",
                List.of(1L),
                List.of(2L, 3L),
                List.of(2L, 5L)
        );
        when(authorConverter.convertAuthorIdListToAuthors(anyList())).thenReturn(Collections.emptyList());
        when(genreConverter.convertGenreIdListToGenres(anyList())).thenReturn(Collections.emptyList());
        when(bookCollectionConverter.convertCollectionIdListToBookCollection(anyList())).thenReturn(Collections.emptyList());

        Long bookId = bookService.addBook(request);

        verify(bookRepository).save(any(Book.class));
    }
}