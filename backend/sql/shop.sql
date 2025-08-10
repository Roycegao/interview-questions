-- 创建数据库
CREATE DATABASE IF NOT EXISTS basic_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE basic_shop;

-- 产品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '产品ID',
    name VARCHAR(255) NOT NULL COMMENT '产品名称',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '产品价格',
    quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    visible BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否可见',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_visible (visible),
    INDEX idx_price (price),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

CREATE TABLE IF NOT EXISTS `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '产品价格',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT '库存数量',
  `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_visible` (`visible`),
  KEY `idx_price` (`price`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车项目ID',
  `cart_id` bigint(20) NOT NULL COMMENT '购物车ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `price` decimal(10,2) NOT NULL COMMENT '产品价格',
  `quantity` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `total_price` decimal(10,2) NOT NULL COMMENT '总价',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cart_product` (`cart_id`,`product_id`),
  KEY `idx_cart_id` (`cart_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车项目表';


CREATE TABLE IF NOT EXISTS `carts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 插入示例数据
INSERT INTO products (name, price, quantity, visible) VALUES
('Cash Money Gun', 43.00, 38, TRUE),
('Giant Inflatable Unicorn', 23.50, 3, TRUE),
('Deal With It Sunglasses', 11.20, 17, TRUE),
('Breaking Bad "Lego" Set', 699.50, 0, FALSE);

-- 创建默认购物车
INSERT INTO carts (user_id) VALUES (1);