package ru.rebook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.rebook.model.entity.User;
import ru.rebook.model.request.BookAddRequest;
import ru.rebook.model.request.RatingRequest;
import ru.rebook.service.BookService;

import java.util.Map;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
@Tag(name = "book", description = "API для работы с книгами")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Добавление книги",
            description = "Позволяет добавить новую книгу в базу"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addBook(
            @Valid @RequestBody BookAddRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long bookId = bookService.addBook(request);
        return ResponseEntity.created(uriComponentsBuilder
                        .path("/book/{bookId}")
                        .build(Map.of("bookId", bookId)))
                .build();
    }

    @Operation(
            summary = "Книги автора",
            description = "Позволяет получить все книги, написанные данным автором. Для поиска используется id автора"
    )
    @GetMapping
    public ResponseEntity<?> getBooksByAuthorId(
            @RequestParam("authorId") Long authorId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(bookService.getBooksByAuthorId(authorId, page, size));
    }

    @Operation(
            summary = "Информация о книге",
            description = "Позволяет получить подробную информацию о книге, включая отзывы и подборки, в которых эта книга есть"
    )
    @GetMapping("{bookId}")
    public ResponseEntity<?> getBookFullInformationDto(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(bookService.getBookFullInformationDto(bookId));
    }

    @Operation(
            summary = "Оценить книгу",
            description = "Позволяет авторизованным пользователям поставить оценку книге (от 1 до 10)"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("rating")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> giveRating(
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal User user
    ) {
        bookService.giveRatingToBook(request, user);
        return ResponseEntity.ok().build();
    }
}

