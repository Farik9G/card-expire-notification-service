package ru.meeral.clientservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.meeral.clientservice.dto.ClientDTO;
import ru.meeral.clientservice.model.Client;
import ru.meeral.clientservice.service.ClientService;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> createOrGetClient(@RequestBody ClientDTO dto) {
        Client client = clientService.createOrGetClient(dto);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    @PostMapping("/check-expiry")
    public ResponseEntity<String> checkExpiryForAllClients() {
        clientService.checkAllClientsForExpiry();
        return ResponseEntity.ok("Expiry check triggered.");
    }


}
