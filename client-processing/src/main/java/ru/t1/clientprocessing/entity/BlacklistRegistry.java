package ru.t1.clientprocessing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.t1.clientprocessing.model.DocumentType;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "blacklist_registry")
public class BlacklistRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 20)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false, length = 50)
    private String documentId;

    @Column(name = "blacklisted_at", nullable = false)
    private OffsetDateTime blacklistedAt;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "blacklist_expiration_date")
    private OffsetDateTime blacklistExpirationDate;
}