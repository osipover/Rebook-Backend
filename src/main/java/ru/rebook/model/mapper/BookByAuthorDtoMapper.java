package ru.rebook.model.mapper;

import org.springframework.stereotype.Component;
import ru.rebook.model.dto.BookByAuthorDto;
import ru.rebook.model.entity.Book;

import java.util.function.Function;

@Component
public class BookByAuthorDtoMapper implements Function<Book, BookByAuthorDto> {
    @Override
    public BookByAuthorDto apply(Book book) {
        return new BookByAuthorDto(
                book.getId(),
                book.getTitle(),
                book.getRating()
        );
    }
}
