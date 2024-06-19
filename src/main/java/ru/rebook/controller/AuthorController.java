package ru.rebook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.rebook.model.request.AuthorAddRequest;
import ru.rebook.service.AuthorService;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("api/v1/author")
@RequiredArgsConstructor
@Tag(name = "author", description = "API для работы с авторами")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(
            summary = "Добавление автора",
            description = "Позволяет добавить нового автора в базу"
    )
    @SecurityRequirement(name = "JWT")
    @PostMapping("add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addAuthor(
            @Valid @RequestBody AuthorAddRequest author,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long authorId = authorService.addAuthor(author);
        return ResponseEntity.created(uriComponentsBuilder
                    .path("/author/{authorId}")
                    .build(Map.of("authorId", authorId)))
                .build();
    }

    @Operation(
            summary = "Информация об авторе",
            description = "Позволяет получить подробную информацию об авторе"
    )
    @GetMapping("{authorId}")
    public ResponseEntity<?> getAuthorFullInformationDto(@PathVariable("authorId") Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorFullInformationDto(authorId));
    }

}
