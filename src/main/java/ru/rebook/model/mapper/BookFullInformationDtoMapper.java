package ru.rebook.model.mapper;

import org.springframework.stereotype.Component;
import ru.rebook.model.dto.*;
import ru.rebook.model.entity.Book;

import java.util.function.Function;

@Component
public class BookFullInformationDtoMapper implements Function<Book, BookFullInformationDto> {
    @Override
    public BookFullInformationDto apply(Book book) {
        return BookFullInformationDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .authors(book.getAuthors().stream()
                        .map(author -> new AuthorFullNameDto(
                                author.getId(),
                                author.getFirstName(),
                                author.getLastName(),
                                author.getMiddleName()))
                        .toList())
                .genres(book.getGenres().stream()
                        .map(genre -> new GenreDto(
                                genre.getTitle(),
                                genre.getDescription()))
                        .toList())
                .collections(book.getCollections().stream()
                        .map(collection -> new BookCollectionDto(
                                collection.getId(),
                                collection.getTitle()))
                        .toList())
                .build();
    }
}
