CREATE TABLE error_log (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    microservice_name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);
