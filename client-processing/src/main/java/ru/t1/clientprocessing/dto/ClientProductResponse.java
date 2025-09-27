package ru.t1.clientprocessing.dto;

import ru.t1.clientprocessing.model.Status;

import java.time.LocalDate;

public record ClientProductResponse(
        Long clientProductId,
        Long clientId,
        Long productId,
        LocalDate openDate,
        LocalDate closeDate,
        Status status
) {
}
