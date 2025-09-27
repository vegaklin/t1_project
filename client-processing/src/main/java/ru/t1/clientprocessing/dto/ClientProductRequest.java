package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.clientprocessing.model.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClientProductRequest(
        @NotNull Long clientId,
        @NotNull Long productId,
        @NotNull LocalDate openDate,
        LocalDate closeDate,
        @NotNull Status status,
        BigDecimal amount
) {
}
