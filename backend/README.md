# 🛍️ Basic Shop - 基础商城系统后端

## 📋 项目概述

Basic Shop 是一个基于 Spring Boot 的轻量级商城系统后端，提供商品管理和购物车功能。系统采用 RESTful API 设计，支持商品 CRUD 操作、库存管理、购物车管理等核心功能。

## 🏗️ 技术架构

### 核心技术栈
- **Java 8** - 基础开发语言
- **Spring Boot 2.7.9** - 主框架
- **MyBatis 1.3.2** - ORM 框架
- **MySQL 5.1.49** - 数据库
- **Druid 1.1.9** - 数据库连接池
- **Knife4j 3.0.3** - API 文档（增强版 Swagger UI）

### 项目结构
```
backend/
├── src/main/java/com/example/
│   ├── ShopApplication.java          # 主启动类
│   ├── shop/                        # 商城业务模块
│   │   ├── controller/              # 控制器层
│   │   │   ├── ProductController.java    # 商品管理控制器
│   │   │   └── CartController.java       # 购物车管理控制器
│   │   ├── service/                 # 业务逻辑层
│   │   ├── dao/                     # 数据访问层
│   │   └── model/                   # 数据模型
│   │       ├── entity/              # 实体类
│   │       ├── req/                 # 请求对象
│   └── common/                      # 公共模块
│       ├── config/                  # 配置类
│       ├── exception/               # 异常处理
│       ├── req/                     # 通用请求对象
│       └── resp/                    # 通用响应对象
├── src/main/resources/
│   ├── application.yml              # 应用配置文件
│   ├── mapper/                      # MyBatis 映射文件
│   └── templates/                   # 模板文件
└── sql/                            # 数据库脚本
    └── shop.sql                    # 数据库初始化脚本
```

## 🚀 核心功能模块

### 1. 商品管理模块 (`ProductController`)
- **商品查询**: 支持分页查询、按名称筛选、可见性筛选
- **商品 CRUD**: 创建、读取、更新、删除商品
- **库存管理**: 实时库存数量管理
- **可见性控制**: 快速切换商品显示/隐藏状态
- **批量操作**: 支持批量查询和操作

**主要接口:**
- `GET /api/products` - 分页商品列表查询
- `GET /api/products/{id}` - 商品详情查询
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品
- `PATCH /api/products/{id}/visibility` - 更新商品可见性
- `GET /api/products/visible` - 可见商品分页查询
- `GET /api/products/visible/simple` - 可见商品简单列表

### 2. 购物车管理模块 (`CartController`)
- **购物车操作**: 添加、修改、删除购物车商品
- **用户会话管理**: 基于 HTTP Header 的用户识别机制
- **库存验证**: 添加商品时自动验证库存
- **价格计算**: 自动计算商品总价和购物车总金额
- **购物车统计**: 商品数量统计和总金额统计

**主要接口:**
- `GET /api/cart` - 获取购物车详情
- `POST /api/cart/items` - 添加商品到购物车
- `PUT /api/cart/items/{itemId}` - 更新购物车商品数量
- `DELETE /api/cart/items/{itemId}` - 从购物车移除商品
- `DELETE /api/cart` - 清空购物车
- `GET /api/cart/count` - 获取购物车商品数量
- `GET /api/cart/total` - 获取购物车总金额

### 3. 用户会话管理
系统采用基于 HTTP Header 的用户识别机制：
- 客户端通过 `X-User-ID` Header 传递用户 ID
- 如果客户端未提供有效用户 ID，系统自动生成新的用户 ID
- 响应头中返回用户 ID，确保前后端用户状态同步
- 支持无状态部署，无需服务器端 Session 存储

## 🗄️ 数据库设计

### 核心表结构

#### 1. 商品表 (`products`)
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '产品ID',
    name VARCHAR(255) NOT NULL COMMENT '产品名称',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '产品价格',
    quantity INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    visible BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否可见',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);
```

#### 2. 购物车表 (`carts`)
```sql
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);
```

#### 3. 购物车项目表 (`cart_items`)
```sql
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项目ID',
    cart_id BIGINT NOT NULL COMMENT '购物车ID',
    product_id BIGINT NOT NULL COMMENT '产品ID',
    product_name VARCHAR(255) NOT NULL COMMENT '产品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '产品价格',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    total_price DECIMAL(10,2) NOT NULL COMMENT '总价',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);
```

## ⚙️ 配置说明

### 应用配置 (`application.yml`)
- **服务端口**: 8080
- **数据库**: MySQL 连接配置（支持 Jasypt 加密）
- **MyBatis**: 映射文件位置和类型别名配置
- **分页插件**: PageHelper 配置
- **API 文档**: Knife4j 配置
- **CORS**: 跨域配置

### 安全配置
- **Jasypt 加密**: 数据库密码等敏感信息加密
- **CORS 支持**: 跨域资源共享配置
- **API 文档安全**: 生产环境自动隐藏敏感信息

## 🚀 快速开始

### 环境要求
- Java 8+
- Maven 3.6+
- MySQL 5.7+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd backend
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p < sql/shop.sql
```

3. **修改配置**
编辑 `src/main/resources/application.yml`，配置数据库连接信息：
```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/basic_shop?useUnicode=true&characterEncoding=UTF-8
      username: your_username
      password: ENC(your_encrypted_password)
```

4. **启动应用**
```bash
mvn spring-boot:run
```

5. **访问 API 文档**
```
http://localhost:8080/doc.html
```

## 📚 API 文档

系统集成了 Knife4j（增强版 Swagger UI），提供完整的 API 文档：

- **访问地址**: `http://localhost:8080/doc.html`
- **功能特性**: 
  - 在线调试
  - 接口文档导出
  - 请求/响应示例
  - 错误码说明

## 🧪 测试

### 单元测试
```bash
mvn test
```

### 代码覆盖率
项目集成了 JaCoCo 代码覆盖率工具：
- **行覆盖率**: 最低 60%
- **分支覆盖率**: 最低 50%
- **测试报告**: `target/site/jacoco/index.html`

### 测试工具
- **JUnit 5** - 单元测试框架
- **Mockito** - Mock 框架
- **TestContainers** - 集成测试容器
- **H2 Database** - 内存数据库（测试用）

## 🔧 开发指南

### 添加新功能
1. 在 `model/entity` 中定义实体类
2. 在 `dao` 中创建数据访问接口
3. 在 `service` 中实现业务逻辑
4. 在 `controller` 中暴露 REST API
5. 在 `mapper` 中编写 SQL 映射

### 代码规范
- 遵循 Java 命名规范
- 使用 Lombok 简化代码
- 统一异常处理和响应格式
- 完整的 API 文档注释

## 📦 部署

### 打包
```bash
mvn clean package
```

### 运行
```bash
java -jar target/shop-admin-0.0.1-SNAPSHOT.jar
```

### Docker 部署（可选）
```dockerfile
FROM openjdk:8-jre-alpine
COPY target/shop-admin-0.0.1-SNAPSHOT.jar shop-admin.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/shop-admin.jar"]
```

## 📄 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

