package ru.meeral.cardservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meeral.cardservice.model.Card;
import ru.meeral.cardservice.model.CardStatus;
import ru.meeral.clientservice.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByClientId(Long clientId);
    List<Card> findByClient(Client client);
}
