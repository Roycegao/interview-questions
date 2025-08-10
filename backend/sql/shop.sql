-- Create database
CREATE DATABASE IF NOT EXISTS basic_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE basic_shop;

CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Product ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Product name',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'Product price',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT 'Stock quantity',
  `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Whether visible',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_visible` (`visible`),
  KEY `idx_price` (`price`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Products table';

CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Cart item ID',
  `cart_id` bigint(20) NOT NULL COMMENT 'Cart ID',
  `product_id` bigint(20) NOT NULL COMMENT 'Product ID',
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Product name',
  `price` decimal(10,2) NOT NULL COMMENT 'Product price',
  `quantity` int(11) NOT NULL DEFAULT '1' COMMENT 'Quantity',
  `total_price` decimal(10,2) NOT NULL COMMENT 'Total price',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_product` (`cart_id`,`product_id`),
  KEY `idx_cart_id` (`cart_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cart items table';


CREATE TABLE IF NOT EXISTS `carts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Cart ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Carts table';

-- Insert sample data
INSERT INTO products (name, price, quantity, visible) VALUES
('Cash Money Gun', 43.00, 38, TRUE),
('Giant Inflatable Unicorn', 23.50, 3, TRUE),
('Deal With It Sunglasses', 11.20, 17, TRUE),
('Breaking Bad "Lego" Set', 699.50, 0, FALSE);

-- Create default cart
INSERT INTO carts (user_id) VALUES (1);