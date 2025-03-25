package ru.meeral.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.meeral.card.dto.CardDTO;
import ru.meeral.card.model.Card;
import ru.meeral.card.model.CardStatus;
import ru.meeral.card.repository.CardRepository;
import ru.meeral.client.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;

    public Card createCard(CardDTO dto) {
        var client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        var card = Card.builder()
                .client(client)
                .cardNumber(dto.getCardNumber())
                .issueDate(dto.getIssueDate())
                .expiryDate(dto.getExpiryDate())
                .status(CardStatus.ACTIVE)
                .build();

        return cardRepository.save(card);
    }

    public void cancelCard(Long cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Карта не найдена"));

        card.setStatus(CardStatus.CANCELLED);
        cardRepository.save(card);
    }

    public List<Card> getClientCards(Long clientId) {
        return cardRepository.findByClientId(clientId);
    }
}
