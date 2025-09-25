CREATE TABLE GENERATED ALWAYS AS IDENTITY product_registry (
    id BIGINT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    interest_rate DECIMAL(20,2) NOT NULL DEFAULT 0,
    open_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE GENERATED ALWAYS AS IDENTITY payment_registry (
    id BIGINT PRIMARY KEY,
    product_registry_id BIGINT NOT NULL,
    payment_date TIMESTAMP WITH TIME ZONE NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    interest_rate_amount DECIMAL(20,2) NOT NULL DEFAULT 0,
    debt_amount DECIMAL(20,2) NOT NULL DEFAULT 0,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    payment_expiration_date TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_pr_payment FOREIGN KEY (product_registry_id) REFERENCES product_registry(id)
);
