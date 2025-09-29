package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ClientCreditProductDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull BigDecimal amount,
        @NotNull Integer monthCount
) {
}
