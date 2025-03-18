package ru.meeral.clientservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.meeral.cardservice.model.Card;
import ru.meeral.cardservice.repository.CardRepository;
import ru.meeral.clientservice.dto.ClientDTO;
import ru.meeral.clientservice.dto.NotificationDTO;
import ru.meeral.clientservice.exception.ClientNotFoundException;
import ru.meeral.clientservice.kafka.KafkaMessageProducer;
import ru.meeral.clientservice.model.Client;
import ru.meeral.clientservice.repository.ClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final KafkaMessageProducer kafkaMessageProducer;

    public Client createOrGetClient(ClientDTO dto) {
        Optional<Client> existingClient = clientRepository.findByEmail(dto.getEmail());
        return existingClient.orElseGet(() -> clientRepository.save(Client.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .build()));
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + id));
    }

    @Scheduled(cron = "0 0 12 * * ?") // Запуск каждый день в 12:00
    public void checkAllClientsForExpiry() {
        List<Client> clients = clientRepository.findAll(); // Получаем всех клиентов
        for (Client client : clients) {
            checkAndNotifyExpiry(client); // Проверяем карты каждого клиента
        }
    }

    public void checkAndNotifyExpiry(Client client) {
        List<Card> cards = cardRepository.findByClient(client);
        LocalDate now = LocalDate.now();

        for (Card card : cards) {
            LocalDate expiryDate = card.getExpiryDate();
            System.out.println("Проверяем карту: " + card.getCardNumber() + ", expiryDate: " + expiryDate);

            if (expiryDate != null && expiryDate.minusMonths(1).isBefore(now)) {
                System.out.println("Отправка уведомления клиенту: " + client.getEmail());

                NotificationDTO notification = new NotificationDTO(
                        client.getEmail(),
                        "Ваша карта скоро истекает! Срок действия до: " + expiryDate,
                        expiryDate
                );
                kafkaMessageProducer.sendNotification(notification);
            } else {
                System.out.println("Карта ещё действительна, уведомление не отправляется.");
            }
        }
    }
//    public void checkAndNotifyExpiry(Client client) {
//        List<Card> cards = cardRepository.findByClient(client);
//        LocalDate now = LocalDate.now();
//
//        for (Card card : cards) {
//            LocalDate expiryDate = card.getExpiryDate();
//            if (expiryDate != null && expiryDate.minusMonths(1).isBefore(now)) {
//                NotificationDTO notification = new NotificationDTO(
//                        client.getEmail(),
//                        "Ваша карта скоро истекает! Срок действия до: " + expiryDate,
//                        expiryDate
//                );
//                kafkaMessageProducer.sendNotification(notification);
//            }
//        }
//    }
}
