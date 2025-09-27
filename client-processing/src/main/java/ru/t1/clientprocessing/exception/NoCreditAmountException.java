package ru.t1.clientprocessing.exception;

public class NoCreditAmountException extends RuntimeException {

    public NoCreditAmountException(String message) {
        super(message);
    }
}
