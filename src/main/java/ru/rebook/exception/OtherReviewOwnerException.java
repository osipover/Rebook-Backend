package ru.rebook.exception;

import org.springframework.security.access.AccessDeniedException;

public class OtherReviewOwnerException extends AccessDeniedException {

    public OtherReviewOwnerException() {
        super("Пользователь не может удалять чужие отзывы");
    }
}
