package ru.t1.accountprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.accountprocessing.model.ClientStatus;

public record ClientProductDto(
        @NotNull String clientId,
        @NotNull String productId,
        @NotNull ClientStatus status,
        @NotNull Boolean isRecalc
) {
}
