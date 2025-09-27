package ru.t1.clientprocessing.dto;

import ru.t1.clientprocessing.model.Status;

public record ClientProductDto(
        Long clientId,
        Long productId,
        Status status
) {
}
