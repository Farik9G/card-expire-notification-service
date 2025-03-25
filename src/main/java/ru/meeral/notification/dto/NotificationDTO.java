package ru.meeral.notification.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class NotificationDTO {
    private String email;
    private String message;
    private LocalDate expiryDate;
}