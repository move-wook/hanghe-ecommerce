CREATE TABLE `user` (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_name VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_balance (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              current_balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                              last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10, 2) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `order` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         total_price DECIMAL(10, 2) NOT NULL,
                         status VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_item (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            order_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE payment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         order_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         amount DECIMAL(10, 2) NOT NULL,
                         status VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE coupon (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        discount_type VARCHAR(255) NOT NULL,
                        discount_value DECIMAL(10, 2) NOT NULL,
                        minimum_order_amount DECIMAL(10, 2) NOT NULL,
                        limit_count INT NOT NULL,
                        issued_count INT NOT NULL DEFAULT 0,
                        valid_from TIMESTAMP NOT NULL,
                        valid_until TIMESTAMP NOT NULL
);

CREATE TABLE issued_coupon (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               coupon_id BIGINT NOT NULL,
                               used BOOLEAN DEFAULT FALSE,
                               used_at TIMESTAMP,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_inventory (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   product_id BIGINT NOT NULL,
                                   stock INT NOT NULL,
                                   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
