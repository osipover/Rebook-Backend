package ru.rebook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rebook.exception.AuthorNotFoundException;
import ru.rebook.exception.FailedAuthorAddException;
import ru.rebook.model.dto.AuthorFullInformationDto;
import ru.rebook.model.entity.Author;
import ru.rebook.model.mapper.AuthorFullInformationDtoMapper;
import ru.rebook.model.request.AuthorAddRequest;
import ru.rebook.repository.AuthorRepository;
import ru.rebook.repository.BookRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final AuthorFullInformationDtoMapper authorFullInformationDtoMapper;

    @Transactional
    public Long addAuthor(AuthorAddRequest newAuthorRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Author author = Author.builder()
                .firstName(newAuthorRequest.firstName())
                .lastName(newAuthorRequest.lastName())
                .middleName(newAuthorRequest.middleName())
                .dateOfBirth(LocalDate.parse(newAuthorRequest.dateOfBirth(), formatter))
                .build();
        boolean isAuthorExists = authorRepository.exists(Example.of(author));
        if (isAuthorExists) {
            throw new FailedAuthorAddException();
        }
        authorRepository.save(author);
        return author.getId();
    }

    public AuthorFullInformationDto getAuthorFullInformationDto(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new AuthorNotFoundException(authorId)
        );
        AuthorFullInformationDto authorFullInformationDto = authorFullInformationDtoMapper.apply(author);
        int numBooksByAuthor = bookRepository.countBooksByAuthorId(authorId);
        authorFullInformationDto.setNumWrittenBooks(numBooksByAuthor);
        return authorFullInformationDto;
    }
}
