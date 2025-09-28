package ru.t1.accountprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.accountprocessing.model.TransactionPaymentType;

import java.math.BigDecimal;

public record ClientTransactionDto(
        @NotNull BigDecimal amount,
        @NotNull TransactionPaymentType type
) {
}
