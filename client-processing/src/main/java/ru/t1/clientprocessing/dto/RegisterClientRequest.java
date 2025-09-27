package ru.t1.clientprocessing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.t1.clientprocessing.model.DocumentType;

import java.time.LocalDate;

public record RegisterClientRequest(
        @NotBlank @Size(max = 12) String clientId,
        @NotBlank @Size(max = 100) String firstName,
        @Size(max = 100) String middleName,
        @NotBlank @Size(max = 100) String lastName,
        @NotNull LocalDate dateOfBirth,
        @NotNull DocumentType documentType,
        @NotBlank @Size(max = 50) String documentId,
        @Size(max = 10) String documentPrefix,
        @Size(max = 10) String documentSuffix,

        @NotBlank @Size(max = 100) String login,
        @NotBlank @Size(max = 100) String password,
        @NotBlank @Email @Size(max = 100) String email
) {
}
