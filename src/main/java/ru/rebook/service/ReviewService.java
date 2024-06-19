package ru.rebook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rebook.exception.BookNotFoundException;
import ru.rebook.exception.EditReviewDeadlineException;
import ru.rebook.exception.OtherReviewOwnerException;
import ru.rebook.exception.ReviewNotFoundException;
import ru.rebook.model.dto.PageDto;
import ru.rebook.model.dto.ReviewDto;
import ru.rebook.model.entity.Book;
import ru.rebook.model.entity.Review;
import ru.rebook.model.entity.User;
import ru.rebook.model.mapper.ReviewDtoMapper;
import ru.rebook.model.request.ReviewAddRequest;
import ru.rebook.model.request.ReviewEditRequest;
import ru.rebook.repository.BookRepository;
import ru.rebook.repository.ReviewRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final BookRepository bookRepository;

    private final ReviewDtoMapper reviewDtoMapper;

    public PageDto<ReviewDto> getReviewsByBookId(Long bookId, int page, int size) {
        boolean isBookExists = bookRepository.existsById(bookId);
        if (!isBookExists) {
            throw new BookNotFoundException(bookId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateOfCreation").ascending());
        Page<Review> reviesPage = reviewRepository.findAllByBookId(pageable, bookId);
        List<ReviewDto> reviews = reviesPage.getContent().stream()
                .map(reviewDtoMapper)
                .toList();
        PageDto<ReviewDto> response = new PageDto<>(
                reviews,
                reviesPage.getTotalElements(),
                reviesPage.getTotalPages()
        );

        return response;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Long addReview(ReviewAddRequest request, User user) {
        Book book = bookRepository.findById(request.bookId()).orElseThrow(
                () -> new BookNotFoundException(request.bookId())
        );
        Review review = Review.builder()
                .title(request.title())
                .description(request.description())
                .book(book)
                .user(user)
                .build();
        reviewRepository.save(review);

        return review.getId();
    }

    public void deleteReviewById(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );
        if (!user.hasRole("ROLE_ADMIN")) {
            checkReviewOwner(review, user);
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public void editReview(Long reviewId, ReviewEditRequest request, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException(reviewId)
        );
        checkReviewOwner(review, user);
        checkEditDeadlineExpiration(review);
        review.setTitle(request.title());
        review.setDescription(request.description());
        reviewRepository.save(review);
    }

    private void checkEditDeadlineExpiration(Review review) {
        LocalDateTime dateOfCreation = review.getDateOfCreation();
        LocalDateTime now = LocalDateTime.now();
        long durationInMinutes = Duration.between(dateOfCreation, now).toMinutes();
        if (durationInMinutes > 5) {
            throw new EditReviewDeadlineException();
        }
    }

    private void checkReviewOwner(Review review, User user) {
        Long ownerId = review.getUser().getId();
        if (!ownerId.equals(user.getId())) {
            throw new OtherReviewOwnerException();
        }
    }
}
