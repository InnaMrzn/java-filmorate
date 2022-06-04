package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class FilmorateExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {FilmorateValidationException.class} )
    public ResponseEntity<Object> handleValidationException (FilmorateValidationException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class} )
    public ResponseEntity<Object> handleExceptions (ConstraintViolationException ex){
        log.error(ex.getMessage());
       return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
