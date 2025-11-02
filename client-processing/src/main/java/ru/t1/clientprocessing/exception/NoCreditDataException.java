package ru.t1.clientprocessing.exception;

public class NoCreditDataException extends RuntimeException {

    public NoCreditDataException(String message) {
        super(message);
    }
}
