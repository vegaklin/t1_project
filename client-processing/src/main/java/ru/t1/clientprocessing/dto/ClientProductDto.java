package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.clientprocessing.model.Status;

public record ClientProductDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull Status status,
        @NotNull Boolean isRecalc
) {
}
