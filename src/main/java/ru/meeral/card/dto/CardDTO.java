package ru.meeral.card.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long clientId;

    @Size(min = 16, max = 16, message = "Номер карты должен содержать ровно 16 цифр")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать только цифры")
    private String cardNumber;

    @PastOrPresent(message = "Дата выпуска карты не может быть в будущем")
    private LocalDate issueDate;

    @Future(message = "Дата окончания срока действия карты должна быть в будущем")
    private LocalDate expiryDate;
}
