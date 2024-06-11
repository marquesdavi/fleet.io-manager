# CREATE TABLE IF NOT EXISTS customers
# (
#     id          BIGINT PRIMARY KEY AUTO_INCREMENT,
#     name        VARCHAR(255) NOT NULL,
#     document_id BIGINT,
#     address_id  BIGINT,
#     phone       VARCHAR(255) NOT NULL,
#     created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
#     updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
#     CONSTRAINT fk_customer_document FOREIGN KEY (document_id) REFERENCES documents (id),
#     CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES address (id)
# );

CREATE TABLE IF NOT EXISTS customers
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(80),
    cnpj        VARCHAR(20),
    phone       VARCHAR(25) NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE(cnpj)
);