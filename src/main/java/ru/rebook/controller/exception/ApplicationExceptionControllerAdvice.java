package ru.rebook.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rebook.controller.exception.response.ExceptionResponse;
import ru.rebook.controller.exception.response.ValidationExceptionResponse;
import ru.rebook.controller.exception.response.Violation;
import ru.rebook.exception.ImpossibleAddException;
import ru.rebook.exception.ImpossibleEditException;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationExceptionControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {ImpossibleAddException.class, ImpossibleEditException.class})
    public ResponseEntity<ExceptionResponse> handleImpossibleException(RuntimeException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ValidationExceptionResponse(violations);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


}
