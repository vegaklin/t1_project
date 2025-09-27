CREATE TABLE "user" (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE client (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id VARCHAR(12) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    document_type VARCHAR(100) NOT NULL CHECK (document_type IN ('PASSPORT', 'INT_PASSPORT', 'BIRTH_CERT')),
    document_id VARCHAR(50) NOT NULL,
    document_prefix VARCHAR(10),
    document_suffix VARCHAR(10),

    CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE product (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    key VARCHAR(10) NOT NULL CHECK (key IN ('DC', 'CC', 'AC', 'IPO', 'PC', 'PENS', 'NS', 'INS', 'BS')),
    create_date DATE NOT NULL,
    product_id VARCHAR(100) UNIQUE
);

CREATE TABLE client_product (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    open_date DATE NOT NULL,
    close_date DATE,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'CLOSED', 'BLOCKED', 'ARRESTED')),

    CONSTRAINT fk_cp_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT fk_cp_product FOREIGN KEY (product_id) REFERENCES product(id)
);
