package ru.rebook.model.mapper;

import org.springframework.stereotype.Component;
import ru.rebook.model.dto.AuthorFullInformationDto;
import ru.rebook.model.entity.Author;

import java.util.function.Function;

@Component
public class AuthorFullInformationDtoMapper implements Function<Author, AuthorFullInformationDto> {
    @Override
    public AuthorFullInformationDto apply(Author author) {
        return AuthorFullInformationDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .middleName(author.getMiddleName())
                .dateOfBirth(author.getDateOfBirth())
                .build();
    }
}
