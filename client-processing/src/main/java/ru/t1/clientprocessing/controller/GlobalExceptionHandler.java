package ru.t1.clientprocessing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.t1.clientprocessing.dto.ClientErrorResponse;
import ru.t1.clientprocessing.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, NoCreditAmountException.class})
    public ResponseEntity<ClientErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ClientErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({NoClientException.class, NoClientProductException.class, NoProductException.class})
    public ResponseEntity<ClientErrorResponse> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClientErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(BlacklistedClientException.class)
    public ResponseEntity<ClientErrorResponse> handleForbiddenExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ClientErrorResponse(ex.getMessage()));
    }
}