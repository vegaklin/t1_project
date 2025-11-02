package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.t1.clientprocessing.model.ProductKey;

import java.time.LocalDate;

public record ProductRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull ProductKey key,
        @NotNull LocalDate createDate
) {
}
