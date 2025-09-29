package ru.t1.accountprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.accountprocessing.model.PaymentSystem;

public record ClientCardDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull PaymentSystem paymentSystem
) {
}
