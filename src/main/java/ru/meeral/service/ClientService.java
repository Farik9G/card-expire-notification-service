package ru.meeral.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.meeral.dto.ClientDTO;
import ru.meeral.exception.ClientNotFoundException;
import ru.meeral.model.Client;
import ru.meeral.repository.ClientRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
