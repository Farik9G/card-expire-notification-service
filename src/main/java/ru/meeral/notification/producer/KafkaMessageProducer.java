package ru.meeral.notification.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.meeral.notification.dto.NotificationDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;
    @Value("${spring.kafka.topic.card-expiry-notifications}")
    private String topic;

    public void sendNotification(NotificationDTO notification) {
        log.info("Отправка уведомления в Kafka: {}", notification);
        kafkaTemplate.send(topic, notification);
    }
}