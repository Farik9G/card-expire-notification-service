package ru.meeral.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meeral.card.model.Card;
import ru.meeral.card.model.CardStatus;
import ru.meeral.client.model.Client;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByClientId(Long clientId);
    boolean existsByCardNumber(String cardNumber);
    List<Card> findByStatus(CardStatus status);
}
