-- Test Database Initialization Script (H2 Database Compatible)

-- Create Products Table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT 'Product Name',
    description TEXT COMMENT 'Product Description',
    price DECIMAL(10,2) NOT NULL COMMENT 'Product Price',
    stock INT NOT NULL DEFAULT 0 COMMENT 'Stock Quantity',
    visible BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Is Visible',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time'
);

-- Create Shopping Cart Table
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT 'User ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    UNIQUE KEY uk_user_id (user_id)
);

-- Create Cart Items Table
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL COMMENT 'Cart ID',
    product_id BIGINT NOT NULL COMMENT 'Product ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT 'Product Quantity',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created Time',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated Time',
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uk_cart_product (cart_id, product_id)
);

-- Insert Test Data
INSERT INTO products (name, description, price, stock, visible) VALUES
('Test Product 1', 'This is a description for test product 1', 99.99, 100, TRUE),
('Test Product 2', 'This is a description for test product 2', 199.99, 50, TRUE),
('Test Product 3', 'This is a description for test product 3', 299.99, 20, FALSE),
('Test Product 4', 'This is a description for test product 4', 399.99, 0, TRUE); 