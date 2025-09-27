package ru.t1.clientprocessing.dto;

import ru.t1.clientprocessing.model.Status;

import java.math.BigDecimal;

public record ClientCreditProductDto(
        Long clientId,
        Long productId,
        Status status,
        BigDecimal amount
) {
}
