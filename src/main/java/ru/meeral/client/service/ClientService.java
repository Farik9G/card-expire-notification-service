package ru.meeral.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.meeral.client.dto.ClientDTO;
import ru.meeral.client.exception.ClientNotFoundException;
import ru.meeral.client.model.Client;
import ru.meeral.client.repository.ClientRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client createOrGetClient(ClientDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
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
}
