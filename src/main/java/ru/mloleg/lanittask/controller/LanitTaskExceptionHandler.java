package ru.mloleg.lanittask.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mloleg.lanittask.dto.common.CommonResponse;
import ru.mloleg.lanittask.dto.common.ValidationError;
import ru.mloleg.lanittask.exception.IdAlreadyInUseException;
import ru.mloleg.lanittask.exception.PersonNotFoundException;
import ru.mloleg.lanittask.exception.UnderageOwnerException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class LanitTaskExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ValidationError> validationErrors = fieldErrors
                .stream()
                .map(validationError ->
                        ValidationError.builder()
                                .field(validationError.getField())
                                .message(validationError.getDefaultMessage())
                                .build())
                .toList();

        log.error("Ошибка валидации ответа: {}", validationErrors, e);

        return CommonResponse.builder()
                .errorMessage("Ошибка валидации")
                .validationErrorList(validationErrors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IdAlreadyInUseException.class, UnderageOwnerException.class})
    public CommonResponse<?> handleLanitTaskException(Exception e) {
        log.error("Произошла ошибка: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .errorMessage("Произошла ошибка: {%s}".formatted(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PersonNotFoundException.class)
    public CommonResponse<?> handlePersonNotFoundException(Exception e) {
        log.error("Произошла ошибка: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .errorMessage("Произошла ошибка: {%s}".formatted(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Произошла ошибка: {}", e.getMessage(), e);

        return CommonResponse.builder()
                .errorMessage("Произошла ошибка: {%s}".formatted(e.getMessage()))
                .build();
    }
}
