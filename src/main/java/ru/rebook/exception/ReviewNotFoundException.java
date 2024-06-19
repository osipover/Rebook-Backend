package ru.rebook.exception;

import java.util.NoSuchElementException;

public class ReviewNotFoundException extends NoSuchElementException {

    public ReviewNotFoundException(Long reviewId) {
        super("Отзыв с id \"%d\" не найден".formatted(reviewId));
    }
}
