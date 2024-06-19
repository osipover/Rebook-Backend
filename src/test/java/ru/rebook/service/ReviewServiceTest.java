package ru.rebook.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.rebook.exception.BookNotFoundException;
import ru.rebook.exception.EditReviewDeadlineException;
import ru.rebook.exception.OtherReviewOwnerException;
import ru.rebook.exception.ReviewNotFoundException;
import ru.rebook.model.dto.PageDto;
import ru.rebook.model.dto.ReviewDto;
import ru.rebook.model.entity.Book;
import ru.rebook.model.entity.Review;
import ru.rebook.model.entity.Role;
import ru.rebook.model.entity.User;
import ru.rebook.model.mapper.ReviewDtoMapper;
import ru.rebook.model.request.ReviewAddRequest;
import ru.rebook.model.request.ReviewEditRequest;
import ru.rebook.repository.BookRepository;
import ru.rebook.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    ReviewDtoMapper reviewDtoMapper;

    @InjectMocks
    ReviewService reviewService;

    List<Review> reviews;

    @BeforeEach
    public void setUp() {
        Book book = new Book();
        Review r1 = Review.builder()
                .user(new User())
                .book(book)
                .title("Заголовок 1")
                .description("Описание 1")
                .build();
        Review r2 = Review.builder()
                .user(new User())
                .book(book)
                .title("Заголовок 2")
                .description("Описание 2")
                .build();
        reviews = List.of(r1, r2);
    }

    @Test
    void getReviewsByBookId_NonExistentBook_ThrowsBookNotFoundException() {
        Long bookId = 1L;
        int page = 0;
        int size = 1;
        when(bookRepository.existsById(bookId)).thenReturn(false);
        Assertions.assertThrows(
                BookNotFoundException.class,
                () -> reviewService.getReviewsByBookId(bookId, page, size)
        );
    }

    @Test
    void getReviewsByBookId_ValidRequest_ReturnsPageDtoWithReviews() {
        Long bookId = 1L;
        int page = 0;
        int size = 1;
        Page<Review> pageReviews = new PageImpl<>(reviews, PageRequest.of(0, 1), 2);
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(reviewRepository.findAllByBookId(any(), any())).thenReturn(pageReviews);
        when(reviewDtoMapper.apply(reviews.get(0))).thenReturn(new ReviewDto());
        when(reviewDtoMapper.apply(reviews.get(1))).thenReturn(new ReviewDto());

        PageDto<ReviewDto> reviews = reviewService.getReviewsByBookId(bookId, page, size);

        Assertions.assertDoesNotThrow(() -> reviewService.getReviewsByBookId(bookId, page, size));
        Assertions.assertEquals(2, reviews.getTotalElements());
        Assertions.assertEquals(2, reviews.getTotalPages());
    }

    @Test
    void addReview_NonExistentBook_ThrowsBookNotFoundException() {
        User user = new User();
        ReviewAddRequest request = new ReviewAddRequest("Заголовок", "Описание", 1L);
        when(bookRepository.findById(request.bookId())).thenReturn(Optional.empty());
        Assertions.assertThrows(
                BookNotFoundException.class,
                () -> reviewService.addReview(request, user)
        );
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void addReview_ValidRequest_ReturnsReviewId() {
        User user = new User();
        ReviewAddRequest request = new ReviewAddRequest("Заголовок", "Описание", 1L);
        when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(new Book()));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());
        Assertions.assertDoesNotThrow(
                () -> reviewService.addReview(request, user)
        );
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void deleteReview_NonExistentReview_ThrowsReviewNotFoundException() {
        long reviewId = 1L;
        User user = new User();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.deleteReviewById(reviewId, user)
        );
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_UserIsNotOwnerOfReview_ThrowsOtherReviewOwnerException() {
        long reviewId = 1L;
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        User owner = User.builder()
                .id(20L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .user(owner)
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        Assertions.assertThrows(
                OtherReviewOwnerException.class,
                () -> reviewService.deleteReviewById(reviewId, user)
        );
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_UserIsAdmin() {
        long reviewId = 1L;
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_ADMIN")))
                .build();
        User owner = User.builder()
                .id(20L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .user(owner)
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        Assertions.assertDoesNotThrow(
                () -> reviewService.deleteReviewById(reviewId, user)
        );
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    void deleteReview_UserIsOwner() {
        long reviewId = 1L;
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        User owner = User.builder()
                .id(10L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .user(owner)
                .build();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        Assertions.assertDoesNotThrow(
                () -> reviewService.deleteReviewById(reviewId, user)
        );
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    void editReview_UserIsNotOwner_ThrowsOtherReviewOwnerException() {
        long reviewId = 1L;
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        User owner = User.builder()
                .id(20L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .user(owner)
                .build();
        ReviewEditRequest request = new ReviewEditRequest("Заголовок 2", "Описание 2");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Assertions.assertThrows(
                OtherReviewOwnerException.class,
                () -> reviewService.editReview(reviewId, request, user)
        );
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void editReview_EditDeadlineExpired_ThrowsEditReviewDeadlineException() {
        long reviewId = 1L;
        LocalDateTime creationTime = LocalDateTime.of(2024, 6, 19, 12, 30);
        LocalDateTime currentTime = LocalDateTime.of(2024, 6, 19, 12, 36);
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        User owner = User.builder()
                .id(10L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .dateOfCreation(creationTime)
                .user(owner)
                .build();
        ReviewEditRequest request = new ReviewEditRequest("Заголовок 2", "Описание 2");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        try (MockedStatic<LocalDateTime> utilities = mockStatic(LocalDateTime.class)) {
            utilities.when(LocalDateTime::now).thenReturn(currentTime);
            utilities.when(() -> LocalDateTime.from(currentTime)).thenReturn(currentTime);
            Assertions.assertThrows(EditReviewDeadlineException.class,
                    () -> reviewService.editReview(reviewId, request, user));
            verify(reviewRepository, never()).save(any(Review.class));
        }
    }

    @Test
    void editReview_NonExistentReview_ThrowsReviewNotFoundException() {
        long reviewId = 1L;
        User user = new User();
        ReviewEditRequest request = new ReviewEditRequest("Заголовок 2", "Описание 2");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ReviewNotFoundException.class,
                () -> reviewService.editReview(reviewId, request, user));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void editReview_ValidRequest() {
        long reviewId = 1L;
        LocalDateTime creationTime = LocalDateTime.of(2024, 6, 19, 12, 30);
        LocalDateTime currentTime = LocalDateTime.of(2024, 6, 19, 12, 33);
        User user = User.builder()
                .id(10L)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        User owner = User.builder()
                .id(10L)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .title("Заголовок 1")
                .description("Описание 1")
                .dateOfCreation(creationTime)
                .user(owner)
                .build();
        ReviewEditRequest request = new ReviewEditRequest("Заголовок 2", "Описание 2");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);
        try (MockedStatic<LocalDateTime> utilities = mockStatic(LocalDateTime.class)) {
            utilities.when(LocalDateTime::now).thenReturn(currentTime);
            utilities.when(() -> LocalDateTime.from(currentTime)).thenReturn(currentTime);

            Assertions.assertDoesNotThrow(
                    () -> reviewService.editReview(reviewId, request, user)
            );
            Assertions.assertEquals("Заголовок 2", review.getTitle());
            Assertions.assertEquals("Описание 2", review.getDescription());
            verify(reviewRepository, times(1)).save(review);
        }
    }
}