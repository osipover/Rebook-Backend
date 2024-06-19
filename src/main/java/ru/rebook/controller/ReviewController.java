package ru.rebook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rebook.model.entity.User;
import ru.rebook.model.request.ReviewAddRequest;
import ru.rebook.model.request.ReviewEditRequest;
import ru.rebook.service.ReviewService;

import java.util.Map;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
@Tag(name = "review", description = "API для работы с отзывами")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Добавление книги",
            description = "Позволяет добавить новую книгу в базу"
    )
    @GetMapping("book/{bookId}")
    public ResponseEntity<?> getReviewsByBookId(
            @PathVariable("bookId")  @Parameter(description = "Идентификатор книги") Long bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId, page, size));
    }

    @PostMapping("add")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> addReview(
            @Valid @RequestBody ReviewAddRequest request,
            @AuthenticationPrincipal User user
    ) {
        Long reviewId = reviewService.addReview(request, user);
        return ResponseEntity.ok(Map.of("review_id", reviewId));
    }

    @DeleteMapping("delete/{reviewId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId")  @Parameter(description = "Идентификатор отзыва") Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        reviewService.deleteReviewById(reviewId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("edit/{reviewId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> editReview(
            @PathVariable("reviewId")  @Parameter(description = "Идентификатор отзыва") Long reviewId,
            @RequestBody ReviewEditRequest request,
            @AuthenticationPrincipal User user
    ) {
        reviewService.editReview(reviewId, request, user);
        return ResponseEntity.ok().build();
    }

}
