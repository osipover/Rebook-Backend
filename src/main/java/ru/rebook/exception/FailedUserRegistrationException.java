package ru.rebook.exception;


public class FailedUserRegistrationException extends ImpossibleAddException {

    public FailedUserRegistrationException(String email) {
        super("Пользователь с почтой \"%s\" уже зарегистрирован".formatted(email));
    }
}
