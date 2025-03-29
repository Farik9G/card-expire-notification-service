package ru.meeral.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.meeral.card.model.Card;
import ru.meeral.card.model.CardStatus;

import java.time.LocalDate;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByClientId(Long clientId);
    boolean existsByCardNumber(String cardNumber);
    @Query("""
    SELECT c FROM Card c
    JOIN FETCH c.client
    WHERE c.status = :status AND c.expiryDate BETWEEN :startDate AND :endDate
    """)
    List<Card> findActiveExpiringCards(
            @Param("status") CardStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
