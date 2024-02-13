-- liquibase formatted sql

-- changeset gordey_dovydenko:1
CREATE
    EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Client
(
    client_id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(60)              NOT NULL,
    email        VARCHAR(60)              NOT NULL UNIQUE,
    password     VARCHAR(300)             NOT NULL,
    gender       VARCHAR(30)              NOT NULL CHECK ( gender IN ('MALE', 'FEMALE', 'UNSPECIFIED') ),
    created_date timestamp with time zone NOT NULL
);

-- rollback DROP TABLE Cart;

-- changeset gordey_dovydenko:2
CREATE TABLE Role
(
    client_id UUID PRIMARY KEY,
    role      VARCHAR(30) PRIMARY KEY NOT NULL CHECK ( role IN ('STUDENT', 'TEACHER', 'DEANERY', 'ADMIN') ),
    FOREIGN KEY (client_id) REFERENCES Client(client_id)
);
-- rollback DROP TABLE Role;

