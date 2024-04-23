CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role_id    BIGINT       NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
