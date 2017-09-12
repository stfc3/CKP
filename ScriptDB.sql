DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers
(
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_code VARCHAR(50),
    customer_name VARCHAR(200),
    customer_address VARCHAR(200),
    customer_phone VARCHAR(20),
    tax_code VARCHAR(20),
    account_number VARCHAR(50),
    bank_name VARCHAR(200),
    status INT DEFAULT 1,
    create_date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(customer_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;