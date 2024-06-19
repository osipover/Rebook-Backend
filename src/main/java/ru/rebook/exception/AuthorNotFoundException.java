package ru.rebook.exception;

import java.util.NoSuchElementException;

public class AuthorNotFoundException extends NoSuchElementException {

    public AuthorNotFoundException(Long authorId) {
        super("Автор с id \"%d\" не найден".formatted(authorId));
    }
}
