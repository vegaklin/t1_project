package ru.t1.accountprocessing.dto;

import jakarta.validation.constraints.NotNull;
import ru.t1.accountprocessing.model.ClientStatus;

public record ClientProductDto(
        @NotNull Long clientId,
        @NotNull Long productId,
        @NotNull ClientStatus status
) {
}
