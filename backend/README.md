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
├── shop/
│   ├── controller/              # REST API controllers
│   ├── service/                 # Business logic layer
│   ├── dao/                     # Data access layer
│   └── model/                   # Data models
│       ├── entity/              # Database entities
│       └── req/                 # Request DTOs
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

## Testing

### Test Coverage
- **Line Coverage**: Minimum 60%
- **Branch Coverage**: Minimum 50%
- **Tools**: JaCoCo, JUnit, Mockito, TestContainers

### Running Tests
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

## License

This project is licensed under the MIT License.

---

For technical support or questions, please refer to the API documentation at `http://localhost:8080/doc.html` or contact the development team.
