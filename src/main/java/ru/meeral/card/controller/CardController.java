package ru.meeral.card.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.meeral.card.dto.CardDTO;
import ru.meeral.card.model.Card;
import ru.meeral.card.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardDTO dto) {
        return ResponseEntity.ok(cardService.createCard(dto));
    }

    @PutMapping("/{cardId}/cancel")
    public ResponseEntity<Void> cancelCard(@PathVariable Long cardId) {
        cardService.cancelCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Card>> getClientCards(@PathVariable Long clientId) {
        return ResponseEntity.ok(cardService.getClientCards(clientId));
    }
}
