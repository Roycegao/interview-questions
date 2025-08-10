// Product type definition
export interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
  visible: boolean;
  createdAt?: string;
  updatedAt?: string;
}

// Cart item type definition
export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  price: number;
  quantity: number;
  totalPrice: number;
}

// Cart type definition
export interface Cart {
  id: number;
  items: CartItem[];
  totalAmount: number;
  itemCount: number;
}

// API response type definition
export interface ApiResponse<T> {
  code: number;
  success: boolean;
  data: T;
  message?: string;
  error?: string;
}

// Pagination type definition
export interface Pagination {
  pageNum: number;
  pageSize: number;
  total: number;
  totalPages: number;
}

// Paginated response type definition
export interface PaginatedResponse<T> {
  list: T[];
  pagination: Pagination;
}

// Search and filter type definition
export interface ProductFilter {
  name?: string;
  minPrice?: number;
  maxPrice?: number;
  visible?: boolean;
}

// Form type definition
export interface ProductFormData {
  name: string;
  price: number;
  quantity: number;
  visible: boolean;
}

 