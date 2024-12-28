package com.food.ordering.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public ErrorDTO handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected Error!")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {Exception.class})
    public ErrorDTO handleException(ValidationException ex) {
        ErrorDTO errorDTO;
        if (ex instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) ex);
            log.error(violations, ex);
            errorDTO = ErrorDTO.builder()
                    .code(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                    .message(violations)
                    .build();
        } else {
            String exceptionMessage = ex.getMessage();
            log.error(ex.getMessage(), ex);
            return ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(exceptionMessage)
                    .build();
        }
        return errorDTO;
    }

    private String extractViolationsFromException(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }
}
