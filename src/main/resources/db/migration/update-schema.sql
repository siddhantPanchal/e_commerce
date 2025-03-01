CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users
(
    id       BIGINT       NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id  BIGINT       NOT NULL,
    username VARCHAR(45)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_role UNIQUE (role_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);


CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE role
(
    id   BIGINT      NOT NULL,
    name VARCHAR(45) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);

DROP SEQUENCE users_seq CASCADE;