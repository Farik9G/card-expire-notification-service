package ru.meeral.exception;

public class CardReplacementException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Ошибка при создании новой карты для клиента с ID ";

    public CardReplacementException(Long clientId) {
        super(MESSAGE_TEMPLATE + clientId);
    }
}