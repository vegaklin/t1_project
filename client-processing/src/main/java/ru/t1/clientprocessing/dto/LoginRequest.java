package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest (
        @NotNull String login,
        @NotNull String password
) {
}
