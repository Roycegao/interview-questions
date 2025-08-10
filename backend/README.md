# Shop Admin Backend Service

## Overview

This is a Spring Boot-based e-commerce backend service that provides RESTful APIs for product management and shopping cart operations. The service is designed with a clean architecture pattern and includes comprehensive API documentation.

## Technology Stack

- **Framework**: Spring Boot 2.7.9
- **Database**: MySQL 5.7+ with Druid connection pool
- **ORM**: MyBatis with PageHelper for pagination
- **API Documentation**: Knife4j (Enhanced Swagger UI)
- **Security**: Jasypt for property encryption
- **Build Tool**: Maven
- **Java Version**: 1.8+

## Architecture

```
src/main/java/com/example/
├── ShopApplication.java          # Main application class
├── common/                       # Common utilities and response models
│   ├── config/                   # Configuration classes
│   ├── exception/                # Global exception handlers
│   ├── req/                      # Common request models
│   └── resp/                     # Common response models
├── shop/
│   ├── controller/               # REST API controllers
│   ├── service/                  # Business logic layer
│   ├── dao/                      # Data access layer
│   └── model/                    # Data models
│       ├── entity/               # Database entities
│       └── req/                  # Request DTOs
src/main/resources/
├── application.yml               # Application configuration
├── mapper/                       # MyBatis XML mapper files
└── templates/                    # Template files
src/test/                         # Test source code
sql/
└── shop.sql                      # Database initialization script
pom.xml                          # Maven dependencies and build configuration
```

## Project Startup Process

### Prerequisites
- **Java**: JDK 1.8 or higher
- **Maven**: 3.6 or higher
- **MySQL**: 8.0 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Step 1: Database Setup
1. **Install MySQL Server**
   ```bash
   # For Windows: Download and install MySQL Installer
   # For macOS: brew install mysql
   # For Ubuntu: sudo apt-get install mysql-server
   ```

2. **Start MySQL Service**
   ```bash
   # Windows: Start MySQL service from Services
   # macOS: brew services start mysql
   # Ubuntu: sudo systemctl start mysql
   ```

3. **Create Database and Import Schema**
   ```bash
   # Connect to MySQL
   mysql -u root -p
   
   # Import the database schema
   mysql -u root -p < sql/shop.sql
   
   # Or execute manually:
   # CREATE DATABASE IF NOT EXISTS basic_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   # USE basic_shop;
   # Then copy and paste the contents of sql/shop.sql
   ```

### Step 2: Configuration Setup
1. **Update Database Connection**
   - Open `src/main/resources/application.yml`
   - Update the database URL, username, and password:
   ```yaml
   spring:
     datasource:
       druid:
         url: jdbc:mysql://localhost:3306/basic_shop?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false
         username: your_username
         password: ENC(your_encrypted_password)
   ```

2. **Encrypt Database Password (Optional)**
   ```bash
   # If you want to encrypt your password using Jasypt:
   # The salt is already configured as: USDT_JIU_BU_GEI_NI
   # You can use online Jasypt encryptors or implement a simple encryption utility
   ```

### Step 3: Build and Run
1. **Clean and Compile**
   ```bash
   # Navigate to project directory
   cd backend
   
   # Clean previous builds
   mvn clean
   
   # Compile the project
   mvn compile
   ```

2. **Run Tests (Optional but Recommended)**
   ```bash
   # Run all tests
   mvn test
   
   # Generate test coverage report
   mvn jacoco:report
   ```

3. **Start the Application**
   ```bash
   # Method 1: Using Maven Spring Boot plugin
   mvn spring-boot:run
   
   # Method 2: Build JAR and run
   mvn clean package
   java -jar target/shop-admin-0.0.1-SNAPSHOT.jar
   
   # Method 3: Run from IDE
   # Open ShopApplication.java and click the Run button
   ```

### Step 4: Verify Installation
1. **Check Application Status**
   - Application should start on port 8080
   - Check console logs for successful startup message
   - Verify database connection is established

2. **Access API Documentation**
   - Open browser and navigate to: `http://localhost:8080/doc.html`
   - You should see the Knife4j enhanced Swagger UI
   - Test the health check endpoint: `http://localhost:8080/api/products`

3. **Verify Database Tables**
   ```sql
   USE basic_shop;
   SHOW TABLES;
   -- Should show: products, carts, cart_items
   
   SELECT * FROM products;
   -- Should show sample products
   ```

### Troubleshooting Common Issues

1. **Port Already in Use**
   ```bash
   # Change port in application.yml
   server:
     port: 8081
   ```

2. **Database Connection Failed**
   - Verify MySQL service is running
   - Check database credentials
   - Ensure database `basic_shop` exists
   - Verify MySQL user has proper permissions

3. **Maven Build Errors**
   ```bash
   # Clear Maven cache
   mvn clean
   rm -rf ~/.m2/repository
   mvn dependency:resolve
   ```

4. **Java Version Issues**
   ```bash
   # Check Java version
   java -version
   
   # Ensure JAVA_HOME is set correctly
   echo $JAVA_HOME
   ```

### Development Workflow

1. **Code Changes**
   - Make changes to Java source files
   - Spring Boot DevTools will auto-reload (if enabled)
   - Or restart the application manually

2. **Database Changes**
   - Update `sql/shop.sql` for schema changes
   - Consider using Flyway or Liquibase for production

3. **Testing**
   ```bash
   # Run specific test class
   mvn test -Dtest=ProductServiceTest
   
   # Run tests with coverage
   mvn clean test jacoco:report
   ```

4. **Building for Production**
   ```bash
   # Create production JAR
   mvn clean package -DskipTests
   
   # Run production build
   java -jar target/shop-admin-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

## Database Schema

### Core Tables

1. **products** - Product catalog management
2. **carts** - User shopping cart containers
3. **cart_items** - Individual items in shopping carts

### Key Features
- UTF8MB4 character encoding support
- Automatic timestamp management
- Proper indexing for performance
- Foreign key constraints for data integrity

## API Endpoints

### Base URL
```
http://localhost:8080
```

### Product Management APIs

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/api/products` | Get paginated product list with filters | No |
| `GET` | `/api/products/{id}` | Get product details by ID | No |
| `POST` | `/api/products` | Create new product | No |
| `PUT` | `/api/products/{id}` | Update existing product | No |
| `DELETE` | `/api/products/{id}` | Delete product | No |
| `PATCH` | `/api/products/{id}/visibility` | Toggle product visibility | No |
| `GET` | `/api/products/visible` | Get paginated visible products | No |
| `GET` | `/api/products/visible/simple` | Get simple list of visible products | No |

### Shopping Cart APIs

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| `GET` | `/api/cart` | Get user's shopping cart | No (uses X-User-ID header) |
| `POST` | `/api/cart/items` | Add product to cart | No (uses X-User-ID header) |
| `PUT` | `/api/cart/items/{itemId}` | Update cart item quantity | No (uses X-User-ID header) |
| `DELETE` | `/api/cart/items/{itemId}` | Remove item from cart | No (uses X-User-ID header) |
| `DELETE` | `/api/cart` | Clear entire cart | No (uses X-User-ID header) |
| `GET` | `/api/cart/count` | Get cart item count | No (uses X-User-ID header) |
| `GET` | `/api/cart/total` | Get cart total amount | No (uses X-User-ID header) |

## Authentication & User Management

### User ID Strategy
The service uses a header-based user identification system:

- **Header**: `X-User-ID`
- **Strategy**: 
  1. Frontend provides existing user ID in header
  2. If no valid ID provided, backend generates new timestamp-based ID
  3. Response includes `X-User-ID` header for frontend to store

### Frontend Implementation
```javascript
// Store user ID in localStorage
const userId = localStorage.getItem('userId');

// Include in all cart-related requests
const headers = {
  'Content-Type': 'application/json',
  'X-User-ID': userId
};

// Update stored ID from response headers
fetch('/api/cart', { headers })
  .then(response => {
    const newUserId = response.headers.get('X-User-ID');
    if (newUserId) {
      localStorage.setItem('userId', newUserId);
    }
  });
```

## Response Format

### Standard Response Structure
```json
{
  "code": 200,
  "message": "Success",
  "data": {},
  "timestamp": "2024-01-01T00:00:00.000Z"
}
```

### Error Response Structure
```json
{
  "code": 1001,
  "message": "Parameter validation failed",
  "data": null,
  "timestamp": "2024-01-01T00:00:00.000Z"
}
```

### Response Codes
- `200`: Success
- `1000`: System error
- `1001`: Parameter validation failed
- `2001`: Resource not found
- `2002`: Resource already exists
- `3001`: Insufficient stock
- `3002`: Cart item not found

## Pagination

### Request Parameters
```json
{
  "page": 1,
  "size": 10,
  "name": "optional search term"
}
```

### Response Structure
```json
{
  "code": 200,
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "size": 10,
    "pages": 10
  }
}
```

## Data Models

### Product Entity
```json
{
  "id": 1,
  "name": "Product Name",
  "price": 99.99,
  "quantity": 100,
  "visible": true,
  "createdAt": "2024-01-01T00:00:00.000Z",
  "updatedAt": "2024-01-01T00:00:00.000Z"
}
```

### Cart Entity
```json
{
  "id": 1,
  "userId": 123456789,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Product Name",
      "price": 99.99,
      "quantity": 2,
      "totalPrice": 199.98
    }
  ],
  "createdAt": "2024-01-01T00:00:00.000Z",
  "updatedAt": "2024-01-01T00:00:00.000Z"
}
```

## Configuration

### Environment Variables
- `server.port`: Application port (default: 8080)
- `spring.datasource.url`: MySQL connection string
- `spring.datasource.username`: Database username
- `spring.datasource.password`: Encrypted database password

### Database Configuration
- **Connection Pool**: Druid with optimized settings
- **Initial Size**: 1
- **Max Active**: 20
- **Min Idle**: 1
- **Max Wait**: 60 seconds

## Configuration Details

### Database Configuration
The application uses MySQL with Druid connection pool for optimal performance:

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/basic_shop?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false
      username: root
      password: ENC(encrypted_password)
      initial-size: 1
      min-idle: 1
      max-active: 20
      max-wait: 60000
```

### Security Configuration
- **Jasypt Encryption**: Database passwords are encrypted using Jasypt
- **Salt Value**: `USDT_JIU_BU_GEI_NI`
- **Algorithm**: `PBEWithMD5AndDES`
- **Format**: `ENC(encrypted_value)`

### API Documentation Configuration
```yaml
knife4j:
  enable: true
  setting:
    enableSwaggerModels: true
    enableDocumentManage: true
    enableAfterScript: true
  production: false  # Set to true for production
```

### MyBatis Configuration
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.shop.model
  configuration:
    map-underscore-to-camel-case: true
```

### Pagination Configuration
```yaml
pagehelper:
  returnPageInfo: check
  params: count=countSql
  supportMethodsArguments: true
  reasonable: true
  helperDialect: mysql
```

## Development Setup

### Prerequisites
- Java 8+
- Maven 3.6+
- MySQL 8.0+

### Local Development
1. Clone the repository
2. Configure database connection in `application.yml`
3. Run database initialization script: `sql/shop.sql`
4. Execute: `mvn spring-boot:run`

### Build
```bash
mvn clean package
java -jar target/shop-admin-0.0.1-SNAPSHOT.jar
```

## API Documentation

### Swagger UI Access
- **URL**: `http://localhost:8080/doc.html`
- **Features**: 
  - Interactive API testing
  - Request/response examples
  - Schema documentation
  - API versioning

## Running Tests
```bash
mvn test
mvn jacoco:report
```

## Security Considerations

### Data Protection
- Database credentials encrypted with Jasypt
- Input validation on all endpoints
- SQL injection prevention with MyBatis

### Rate Limiting
Currently not implemented - consider adding for production use

## Monitoring & Logging

### Logging
- SLF4J with Spring Boot default configuration
- Structured logging for cart operations
- User ID tracking in logs

### Health Checks
- Spring Boot Actuator endpoints available
- Database connection monitoring

## Deployment

### Production Considerations
- Set `knife4j.production=true` for production
- Configure proper database credentials
- Enable HTTPS
- Implement proper authentication/authorization
- Add rate limiting
- Configure monitoring and alerting

### Docker Support
Dockerfile not included - consider containerization for deployment

## Support & SLA

### Response Times
- **GET requests**: < 200ms
- **POST/PUT requests**: < 500ms
- **DELETE requests**: < 300ms

### Availability
- **Target**: 99.9% uptime
- **Maintenance windows**: Scheduled during low-traffic periods

### Error Handling
- Comprehensive error codes and messages
- Graceful degradation for non-critical failures
- Detailed logging for debugging

## Contributing

### Code Standards
- Follow Spring Boot best practices
- Use Lombok for boilerplate reduction
- Implement proper exception handling
- Add comprehensive unit tests

### API Design Principles
- RESTful conventions
- Consistent response formats
- Proper HTTP status codes
- Comprehensive Swagger documentation

## Quick Start Guide

### For Developers (5-Minute Setup)
```bash
# 1. Clone and navigate to project
cd backend

# 2. Setup database (one-time)
mysql -u root -p < sql/shop.sql

# 3. Update database credentials in application.yml

# 4. Run the application
mvn spring-boot:run

# 5. Access API docs
open http://localhost:8080/doc.html
```

### For Production Deployment
```bash
# 1. Build production JAR
mvn clean package -DskipTests

# 2. Configure production database
# 3. Set environment variables
# 4. Run with production profile
java -jar target/shop-admin-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### API Testing Examples
```bash
# Test product listing
curl http://localhost:8080/api/products

# Test cart operations (replace USER_ID with actual value)
curl -H "X-User-ID: 123456789" http://localhost:8080/api/cart

# Add item to cart
curl -X POST http://localhost:8080/api/cart/items \
  -H "Content-Type: application/json" \
  -H "X-User-ID: 123456789" \
  -d '{"productId": 1, "quantity": 2}'
```

## License

This project is licensed under the MIT License.

---

For technical support or questions, please refer to the API documentation at `http://localhost:8080/doc.html` or contact the development team.
