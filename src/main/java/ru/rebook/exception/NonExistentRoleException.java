package ru.rebook.exception;

import java.util.NoSuchElementException;

public class NonExistentRoleException extends NoSuchElementException {

    public NonExistentRoleException(String roleName) {
        super("Роли \"%s\" не существует".formatted(roleName));
    }
}
