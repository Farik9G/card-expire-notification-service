package ru.meeral.clientservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.meeral.clientservice.dto.NotificationDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;
    private static final String TOPIC = "card-expiry-notifications";

    public void sendNotification(NotificationDTO notification) {
        log.info("Отправка уведомления в Kafka: {}", notification);
        kafkaTemplate.send(TOPIC, notification);
    }
}