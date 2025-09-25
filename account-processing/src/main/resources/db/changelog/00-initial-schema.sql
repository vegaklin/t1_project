CREATE TABLE account (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    balance DECIMAL(20,2) NOT NULL DEFAULT 0,
    interest_rate DECIMAL(20,2) NOT NULL DEFAULT 0,
    is_recalc BOOLEAN NOT NULL DEFAULT FALSE,
    card_exist BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE card (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id BIGINT NOT NULL,
    card_id VARCHAR(100) NOT NULL UNIQUE,
    payment_system VARCHAR(100) NOT NULL,
    status VARCHAR(100) NOT NULL,

    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE payment (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id BIGINT NOT NULL,
    payment_date TIMESTAMP WITH TIME ZONE NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    is_credit BOOLEAN NOT NULL,
    payed_at TIMESTAMP,
    type VARCHAR(100) NOT NULL,

    CONSTRAINT fk_payment_account FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE transaction (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id BIGINT NOT NULL,
    card_id BIGINT,
    type VARCHAR(100) NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ALLOWED', 'PROCESSING', 'COMPLETE', 'BLOCKED', 'CANCELLED')),
    timestamp TIMESTAMP NOT NULL,

    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT fk_transaction_card FOREIGN KEY (card_id) REFERENCES card(id)
);
