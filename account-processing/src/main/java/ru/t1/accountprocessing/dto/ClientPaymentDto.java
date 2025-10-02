package ru.t1.accountprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.accountprocessing.model.TransactionPaymentType;

import java.math.BigDecimal;

public record ClientPaymentDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull BigDecimal amount,
        @NotNull TransactionPaymentType type
) {
}
