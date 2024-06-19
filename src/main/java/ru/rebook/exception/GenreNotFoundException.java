package ru.rebook.exception;

import java.util.NoSuchElementException;

public class GenreNotFoundException extends NoSuchElementException {

    private Long genreId;

    public GenreNotFoundException(Long genreId) {
        super("Жанр с id \"%d\" не найден".formatted(genreId));
        this.genreId = genreId;
    }
}
