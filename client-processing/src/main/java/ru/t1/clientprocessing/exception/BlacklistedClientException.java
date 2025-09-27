package ru.t1.clientprocessing.exception;

public class BlacklistedClientException extends RuntimeException {

    public BlacklistedClientException(String message) {
        super(message);
    }
}
