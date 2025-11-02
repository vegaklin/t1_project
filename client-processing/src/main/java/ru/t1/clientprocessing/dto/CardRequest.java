package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.clientprocessing.model.PaymentSystem;

public record CardRequest(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull PaymentSystem paymentSystem
) {
}
