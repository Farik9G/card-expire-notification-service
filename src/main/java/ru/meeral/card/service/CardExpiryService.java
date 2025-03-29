package ru.meeral.card.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.meeral.card.model.Card;
import ru.meeral.card.model.CardStatus;
import ru.meeral.card.repository.CardRepository;
import ru.meeral.client.model.Client;
import ru.meeral.notification.dto.NotificationDTO;
import ru.meeral.notification.producer.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardExpiryService {
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final KafkaMessageProducer kafkaMessageProducer;

    @Transactional
    @Scheduled(cron = "0 0 2 * * ?") // Запуск каждый день в 12:00
    public void checkAllClientsForExpiry() {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthLater = now.plusMonths(1);

        List<Card> cards = cardRepository.findActiveExpiringCards(CardStatus.ACTIVE, now, oneMonthLater);

        for (Card card : cards) {
            if (card.getExpiryDate().isBefore(now) || card.getExpiryDate().isEqual(now)) {
                log.info("Карта с номером {} истекла и будет заменена", card.getCardNumber());
                replaceExpiredCard(card);
            } else {
                sendExpiryNotification(card);
            }
        }
    }

    private void sendExpiryNotification(Card card) {
        Client client = card.getClient();
        NotificationDTO notification = new NotificationDTO(
                client.getEmail(),
                String.format(
                        "Ваша карта c номером %s скоро истекает!\nСрок действия до: %s",
                        card.getCardNumber(), card.getExpiryDate()
                ),
                card.getExpiryDate()
        );
        kafkaMessageProducer.sendNotification(notification);
        log.info("Уведомление отправлено клиенту {} ({}) о скором истечении карты: {}",
                client.getId(), client.getEmail(), card.getExpiryDate());
    }

    private void replaceExpiredCard(Card expiredCard) {
        Client client = expiredCard.getClient();
        Card newCard = cardService.createCard(client.getId());
        cardService.expireCard(expiredCard.getId());

        NotificationDTO notification = new NotificationDTO(
                client.getEmail(),
                String.format(
                        "Ваша старая карта %s была заменена.\nВаша новая карта: %s.\nСрок действия до: %s",
                        expiredCard.getCardNumber(), newCard.getCardNumber(), newCard.getExpiryDate()
                ),
                newCard.getExpiryDate()
        );
        kafkaMessageProducer.sendNotification(notification);

        log.info("Клиенту {} ({}) отправлено уведомление о замене карты. Старая карта: {}, новая карта: {}",
                client.getId(), client.getEmail(), expiredCard.getCardNumber(), newCard.getCardNumber());
    }
}
