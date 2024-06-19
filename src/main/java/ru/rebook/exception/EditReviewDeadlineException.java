package ru.rebook.exception;

public class EditReviewDeadlineException extends ImpossibleEditException {

    public EditReviewDeadlineException() {
        super("Редактировать отзыв можно только в первые 5 минут после его создания");
    }
}
