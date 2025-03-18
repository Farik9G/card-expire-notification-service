package ru.meeral.cardservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long clientId;
    private String cardNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
}
