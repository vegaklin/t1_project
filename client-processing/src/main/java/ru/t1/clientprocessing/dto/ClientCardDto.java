package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.clientprocessing.model.PaymentSystem;

public record ClientCardDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull PaymentSystem paymentSystem
) {
}
