package ru.meeral.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meeral.clientservice.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
