package ru.meeral.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long cardId) {
        super("Карта с ID " + cardId + " не найдена");
    }
}