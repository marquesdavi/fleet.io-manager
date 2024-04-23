CREATE TABLE vehicles
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    model     VARCHAR(255) NOT NULL,
    brand     VARCHAR(255) NOT NULL,
    plate     VARCHAR(255) NOT NULL UNIQUE,
    year      INT          NOT NULL,
    client_id BIGINT       NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients (id)
);