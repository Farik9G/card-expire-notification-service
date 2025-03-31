package ru.meeral.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long clientId) {
        super("Не удалось найти клиента с ID: " + (clientId != null ? clientId : "неизвестный ID"));
    }

    public ClientNotFoundException(String cardNumber) {
        super("Не удалось найти клиента для карты с номером " + cardNumber);
    }
}
