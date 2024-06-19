package ru.rebook.exception;

import java.util.NoSuchElementException;

public class BookCollectionNotFoundException extends NoSuchElementException {

    public BookCollectionNotFoundException(Long collectionId) {
        super("Подборка с id \"%d\" не найдена".formatted(collectionId));
    }
}
