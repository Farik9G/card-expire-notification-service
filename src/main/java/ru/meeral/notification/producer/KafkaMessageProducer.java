package ru.meeral.notification.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.meeral.exception.NotificationSendingException;
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
            kafkaTemplate.send(topic, notification).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Ошибка при отправке уведомления в Kafka", ex);
                    throw new NotificationSendingException("Ошибка при отправке уведомления в Kafka", ex);
                } else {
                    log.info("Уведомление успешно отправлено в Kafka: {}", result.getRecordMetadata());
                }
            });
    }
}