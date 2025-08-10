# Shopping Frontend Application

## Overview
React-based frontend application for shopping system with TypeScript, Redux, and Ant Design.

## Technology Stack
- React 19.1.1 + TypeScript 4.9.5
- Redux Toolkit + React Redux
- Ant Design 5.26.7
- React Router DOM 7.7.1
- Axios 1.11.0
- Less 4.4.0

## Project Structure
```
src/
├── components/          # Reusable UI components
├── pages/              # Page components (Home, Admin, ShopCart)
├── services/           # API service layer
├── store/              # Redux store configuration
├── types/              # TypeScript definitions
└── styles/             # Global styles
```

## Working Principles

### Architecture
- Component-based React structure
- Redux state management
- Service layer for API communication
- TypeScript for type safety

### State Management
- Redux Toolkit manages global state
- Products and cart slices
- Local storage for persistence

### API Communication
- Axios interceptors for request/response
- Automatic user ID management
- Unified error handling
- 10-second timeout

### Responsive Design
- Mobile-first approach
- Dynamic theme configuration
- Adaptive component sizing

## Service Level Agreement (SLA) for Backend

### API Requirements
- **Base URL**: `http://localhost:8080/api`
- **Headers**: `X-User-ID` (Long type)
- **Timeout**: 10 seconds
- **Format**: JSON with standard response structure

### Response Format
```typescript
interface ApiResponse<T> {
  code: number;
  success: boolean;
  data: T;
  message?: string;
  error?: string;
}
```

### Required Endpoints

#### Products
```
GET    /api/products          # List products
POST   /api/products          # Create product
PUT    /api/products/:id      # Update product
DELETE /api/products/:id      # Delete product
```

#### Cart
```
GET    /api/cart              # Get cart
POST   /api/cart/items        # Add item
PUT    /api/cart/items/:id    # Update item
DELETE /api/cart/items/:id    # Remove item
```

### Data Models

#### Product
```typescript
interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
  visible: boolean;
  createdAt?: string;
  updatedAt?: string;
}
```

#### Cart
```typescript
interface Cart {
  id: number;
  items: CartItem[];
  totalAmount: number;
  itemCount: number;
}
```

### Error Handling
- **400**: Bad Request
- **401**: Unauthorized
- **403**: Forbidden
- **404**: Not Found
- **409**: Conflict
- **422**: Validation Error
- **500**: Server Error

### Performance Requirements
- Product listing: < 500ms
- Cart operations: < 300ms
- CRUD operations: < 1000ms

## Development Setup

### Install
```bash
cd frontend
npm install
```

### Run
```bash
npm start          # Development
npm run build      # Production build
npm test           # Run tests
```

### Environment
Create `.env.local`:
```env
REACT_APP_API_BASE_URL=http://localhost:8080/api
```

## Build & Deploy
- Output: `build/` directory
- Static files for any web server
- Optimized for production

## Browser Support
- Chrome 90+, Firefox 88+, Safari 14+
- Mobile browsers supported
- No IE support

## Team Responsibilities

### Frontend Team
- UI/UX implementation
- State management
- Performance optimization
- Cross-browser compatibility

### Backend Team
- API implementation
- Data validation
- Security
- Performance optimization

### Communication
- API changes: 2 weeks notice
- Breaking changes: Major version bump
- Critical issues: 24-hour response
