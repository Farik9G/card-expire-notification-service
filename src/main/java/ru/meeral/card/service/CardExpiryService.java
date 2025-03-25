package ru.meeral.card.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.meeral.card.model.Card;
import ru.meeral.card.repository.CardRepository;
import ru.meeral.client.model.Client;
import ru.meeral.client.repository.ClientRepository;
import ru.meeral.notification.dto.NotificationDTO;
import ru.meeral.notification.producer.KafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardExpiryService {
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final KafkaMessageProducer kafkaMessageProducer;

    @Scheduled(cron = "0 0 12 * * ?") // Запуск каждый день в 12:00
    public void checkAllClientsForExpiry() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            checkAndNotifyExpiry(client);
        }
    }

    public void checkAndNotifyExpiry(Client client) {
        List<Card> cards = cardRepository.findByClient(client);
        LocalDate now = LocalDate.now();

        for (Card card : cards) {
            LocalDate expiryDate = card.getExpiryDate();
            if (expiryDate != null && expiryDate.minusMonths(1).isBefore(now)) {
                NotificationDTO notification = new NotificationDTO(
                        client.getEmail(),
                        "Ваша карта скоро истекает! Срок действия до: " + expiryDate,
                        expiryDate
                );
                kafkaMessageProducer.sendNotification(notification);
                log.info("Уведомление отправлено клиенту {} ({}) о карте с истечением срока: {}",
                        client.getId(), client.getEmail(), expiryDate);
            }
        }
    }
}
