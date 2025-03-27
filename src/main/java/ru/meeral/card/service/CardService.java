package ru.meeral.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.meeral.card.model.Card;
import ru.meeral.card.model.CardStatus;
import ru.meeral.card.repository.CardRepository;
import ru.meeral.client.repository.ClientRepository;
import java.security.SecureRandom;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public Card createCard(Long clientId) {
        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        String cardNumber;
        do {
            cardNumber = generateUniqueCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));

        var card = Card.builder()
                .client(client)
                .cardNumber(cardNumber)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(3))
                .status(CardStatus.ACTIVE)
                .build();

        return cardRepository.save(card);
    }

    @Transactional
    public void cancelCard(Long cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));

        card.setStatus(CardStatus.CANCELLED);
        cardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public List<Card> getClientCards(Long clientId) {
        return cardRepository.findByClientId(clientId);
    }

    private String generateUniqueCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }

        String cardNumberWithoutCheckDigit = sb.toString();
        int checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);

        return cardNumberWithoutCheckDigit + checkDigit;
    }

    @Transactional
    public void expireCard(Long cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));
        card.setStatus(CardStatus.EXPIRED);
        cardRepository.save(card);
    }

    private int calculateLuhnCheckDigit(String cardNumberWithoutCheckDigit) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumberWithoutCheckDigit.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
