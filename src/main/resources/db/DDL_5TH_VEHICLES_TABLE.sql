CREATE TABLE vehicles
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    model     VARCHAR(255) NOT NULL,
    brand     VARCHAR(255) NOT NULL,
    plate     VARCHAR(255) NOT NULL UNIQUE,
    year      INT          NOT NULL,
    customer_id BIGINT       NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);