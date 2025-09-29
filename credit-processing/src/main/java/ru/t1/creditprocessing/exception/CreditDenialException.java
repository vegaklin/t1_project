package ru.t1.creditprocessing.exception;

public class CreditDenialException extends RuntimeException {

    public CreditDenialException(String message) {
        super(message);
    }
}
