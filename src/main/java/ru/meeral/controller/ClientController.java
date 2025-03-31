package ru.meeral.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.meeral.service.CardExpiryService;
import ru.meeral.dto.ClientDTO;
import ru.meeral.model.Client;
import ru.meeral.service.ClientService;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final CardExpiryService cardExpiryService;

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
        cardExpiryService.checkAllClientsForExpiry();
        return ResponseEntity.ok("Проверка срока действия карт запущена.");
    }


}
