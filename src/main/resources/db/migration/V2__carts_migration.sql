CREATE TABLE IF NOT EXISTS carts (
    id BINARY(16) DEFAULT (uuid_to_bin(uuid())),
    create_date DATE DEFAULT (curdate()),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id TINYINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category_id TINYINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BINARY(16) PRIMARY KEY DEFAULT (uuid_to_bin(uuid())),
    cart_id BINARY(16) NOT NULL,
    product_id BIGINT NOT NULL, -- Yahan 'INT' ko 'BIGINT' kiya gaya hai mismatch theek karne ke liye
    quantity INT DEFAULT 1,
    
    CONSTRAINT fk_cart 
        FOREIGN KEY (cart_id) 
        REFERENCES carts(id) 
        ON DELETE CASCADE,
        
    CONSTRAINT fk_product 
        FOREIGN KEY (product_id) 
        REFERENCES products(id) 
        ON DELETE CASCADE
);