package ru.t1.clientprocessing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.clientprocessing.entity.BlacklistRegistry;
import ru.t1.clientprocessing.model.DocumentType;

import java.util.Optional;

public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    Optional<BlacklistRegistry> findByDocumentTypeAndDocumentId(DocumentType documentType, String documentId);
}