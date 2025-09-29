CREATE TABLE product_registry (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id VARCHAR(12) NOT NULL,
    account_id VARCHAR(100) NOT NULL,
    product_id BIGINT NOT NULL,
    interest_rate DECIMAL(20,2) NOT NULL DEFAULT 0,
    open_date DATE NOT NULL
);

CREATE TABLE payment_registry (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_registry_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    interest_rate_amount DECIMAL(20,2) NOT NULL DEFAULT 0,
    debt_amount DECIMAL(20,2) NOT NULL DEFAULT 0,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    payment_expiration_date DATE NOT NULL,

    CONSTRAINT fk_pr_payment FOREIGN KEY (product_registry_id) REFERENCES product_registry(id)
);
