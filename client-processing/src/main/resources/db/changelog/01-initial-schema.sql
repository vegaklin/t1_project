CREATE TABLE blacklist_registry (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    document_type VARCHAR(20) NOT NULL CHECK (document_type IN ('PASSPORT', 'INT_PASSPORT', 'BIRTH_CERT')),
    document_id VARCHAR(50) NOT NULL,
    blacklisted_at DATE NOT NULL,
    reason VARCHAR(255) NOT NULL,
    blacklist_expiration_date DATE
);
