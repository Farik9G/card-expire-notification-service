package ru.meeral.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meeral.client.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
