package ru.meeral.exception;

public class CardServiceException extends RuntimeException {
    public CardServiceException(String message) {
        super(message);
    }
}