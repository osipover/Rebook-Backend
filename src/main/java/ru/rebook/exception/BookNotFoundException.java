package ru.rebook.exception;

import java.util.NoSuchElementException;

public class BookNotFoundException extends NoSuchElementException {

    public BookNotFoundException(Long bookId) {
        super("Книга с id '%d' не найдена".formatted(bookId));
    }
}
