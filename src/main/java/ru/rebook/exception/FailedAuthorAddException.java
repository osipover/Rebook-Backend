package ru.rebook.exception;

public class FailedAuthorAddException extends ImpossibleAddException {

    public FailedAuthorAddException() {
        super("Такой автор уже существует");
    }
}
