package ru.rebook.model.mapper;

import org.springframework.stereotype.Component;
import ru.rebook.model.dto.AuthorFullNameDto;
import ru.rebook.model.entity.Author;

import java.util.function.Function;

@Component
public class AuthorFullNameDtoMapper implements Function<Author, AuthorFullNameDto> {
    @Override
    public AuthorFullNameDto apply(Author author) {
        return AuthorFullNameDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .middleName(author.getMiddleName())
                .build();
    }
}
