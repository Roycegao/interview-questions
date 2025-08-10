# Shopping Frontend Application

## Project Overview

This is a React-based e-commerce frontend application built with TypeScript, featuring a modern UI powered by Ant Design and state management with Redux Toolkit. The application provides a responsive shopping experience with product management, shopping cart functionality, and administrative capabilities.

## Technology Stack

- **Framework**: React 19.1.1 with TypeScript 4.9.5
- **UI Library**: Ant Design 5.26.7
- **State Management**: Redux Toolkit 2.8.2 + React Redux 9.2.0
- **Routing**: React Router DOM 7.7.1
- **HTTP Client**: Axios 1.11.0
- **Styling**: Less 4.4.0 with responsive design
- **Build Tool**: Create React App 5.0.1

## Project Structure

```
src/
├── components/          # Reusable UI components
│   └── layout/         # Layout components (MainLayout)
├── pages/              # Page components
│   ├── Home.tsx        # Home page with product listing
│   ├── Admin.tsx       # Admin panel for product management
│   └── ShopCart.tsx    # Shopping cart page
├── services/           # API service layer
│   ├── api.ts          # Core API configuration and interceptors
│   ├── products.ts     # Product-related API calls
│   └── cart.ts         # Cart-related API calls
├── store/              # Redux store configuration
│   ├── index.ts        # Store setup
│   └── slices/         # Redux slices
│       ├── productsSlice.ts  # Product state management
│       └── cartSlice.ts      # Cart state management
├── types/              # TypeScript type definitions
│   └── index.ts        # Core type interfaces
├── styles/             # Global styles and themes
└── App.tsx             # Main application component
```

## Key Features

### 1. Responsive Design
- Mobile-first approach with breakpoint at 768px
- Dynamic theme configuration based on screen size
- Optimized component sizing for mobile and desktop

### 2. State Management
- **Products Slice**: Manages product listing, filtering, and CRUD operations
- **Cart Slice**: Handles shopping cart state, item management, and calculations
- Persistent cart state with localStorage backup

### 3. API Integration
- Centralized API configuration with axios interceptors
- Automatic user ID management and session handling
- Comprehensive error handling with user-friendly messages
- Support for multiple environment configurations

### 4. User Experience
- Global error handling with toast notifications
- Loading states and optimistic updates
- Form validation and error display
- Responsive navigation and layout

## API Contract & Backend Integration

### Base Configuration
The application expects a RESTful API with the following base URL configuration:
- **Development**: `http://localhost:8080/api`
- **Test**: `http://124.220.68.114:8080/api`
- **Production**: `https://codeok.cn/api`

### Authentication & Session Management
- **User ID Header**: `X-User-ID` sent with every request
- **Session Cookies**: Enabled for persistent authentication
- **Auto-generated User IDs**: Fallback to timestamp-based IDs if not provided

### API Response Format
All API responses should follow this structure:
```typescript
interface ApiResponse<T> {
  code: number;        // HTTP status code
  success: boolean;    // Operation success flag
  data: T;            // Response payload
  message?: string;    // Success message
  error?: string;      // Error message
}
```

### Required Endpoints

#### Products API
- `GET /products` - List products with pagination and filtering
- `POST /products` - Create new product
- `PUT /products/:id` - Update existing product
- `DELETE /products/:id` - Delete product
- `GET /products/:id` - Get product details

#### Cart API
- `GET /cart` - Get user's shopping cart
- `POST /cart/items` - Add item to cart
- `PUT /cart/items/:id` - Update cart item quantity
- `DELETE /cart/items/:id` - Remove item from cart
- `POST /cart/checkout` - Process checkout

### Error Handling Expectations
- **400**: Bad Request - Invalid parameters or business logic errors
- **401**: Unauthorized - Authentication required
- **403**: Forbidden - Insufficient permissions
- **404**: Not Found - Resource doesn't exist
- **500**: Internal Server Error - Server-side issues

## Development Setup

### Prerequisites
- Node.js 16.18+ 
- npm or yarn package manager

### Installation
```bash
# Install dependencies
npm install

# Start development server
npm start

# Build for different environments
npm run build:dev      # Development build
npm run build:test     # Test environment build
npm run build:prod     # Production build
```

### Environment Variables
```bash
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## Build & Deployment

### Build Scripts
- `npm run build`: Standard production build
- `npm run build:dev`: Development environment build
- `npm run build:test`: Test environment build  
- `npm run build:prod`: Production environment build

### Output
Build artifacts are generated in the `build/` directory, ready for deployment to any static hosting service.

## Performance & Optimization

### Bundle Optimization
- Tree shaking for unused code elimination
- Code splitting for route-based lazy loading
- Optimized imports for Ant Design components

### Responsive Performance
- Dynamic theme configuration based on screen size
- Optimized component rendering for mobile devices
- Efficient state updates with Redux Toolkit

## Testing

### Test Scripts
```bash
npm test              # Run test suite
npm run test:coverage # Run tests with coverage report
```

### Testing Framework
- Jest for unit testing
- React Testing Library for component testing
- DOM testing utilities for integration tests

## Browser Support

### Production
- Modern browsers (>0.2% market share)
- Excludes IE and Opera Mini

### Development
- Latest Chrome, Firefox, and Safari versions

## SLA Requirements for Backend Services

### Performance Targets
- **API Response Time**: < 200ms for 95th percentile
- **Availability**: 99.9% uptime
- **Error Rate**: < 0.1% for 4xx/5xx responses

### Scalability Requirements
- Support for concurrent user sessions
- Efficient pagination for large product catalogs
- Optimized database queries for cart operations

### Security Requirements
- CORS configuration for frontend domain
- Input validation and sanitization
- Rate limiting for API endpoints
- Secure session management

### Monitoring & Logging
- Comprehensive error logging with context
- Performance metrics collection
- User activity tracking for analytics
- Health check endpoints for system monitoring

## Troubleshooting

### Common Issues
1. **CORS Errors**: Ensure backend allows frontend domain
2. **User ID Mismatch**: Check localStorage and header synchronization
3. **Build Failures**: Verify Node.js version compatibility
4. **API Timeouts**: Adjust timeout values in api.ts configuration

### Debug Mode
Enable browser developer tools and check:
- Network tab for API request/response details
- Console for error messages and warnings
- Redux DevTools for state management debugging

## Contributing

### Code Standards
- TypeScript strict mode enabled
- ESLint configuration for code quality
- Consistent component structure and naming
- Comprehensive type definitions

### Development Workflow
1. Feature branch creation
2. TypeScript compilation check
3. ESLint validation
4. Component testing
5. Pull request review

## License

Private project - All rights reserved.

---

**Note**: This frontend application is designed to work seamlessly with a compatible backend API. Ensure all API endpoints and response formats match the specifications outlined in this document for optimal functionality. 