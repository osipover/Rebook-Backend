package ru.rebook.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rebook.exception.AuthorNotFoundException;
import ru.rebook.model.entity.Author;
import ru.rebook.repository.AuthorRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorConverter {

    private final AuthorRepository authorRepository;

    public List<Author> convertAuthorIdListToAuthors(List<Long> authorsIds) {
        return authorsIds.stream()
                .map(id -> authorRepository.findById(id).orElseThrow(
                        () -> new AuthorNotFoundException(id)))
                .toList();
    }
}
