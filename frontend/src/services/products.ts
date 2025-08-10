import api, { handleApiResponse } from './api';
import { Product, ProductFilter, ProductFormData, PaginatedResponse } from '../types';

// Product API service class
export class ProductService {
  // Get product list
  static async getProducts(pageNum: number = 1, pageSize: number = 10, filter?: ProductFilter): Promise<PaginatedResponse<Product>> {
    const params = new URLSearchParams({
      pageNum: pageNum.toString(),
      pageSize: pageSize.toString(),
      ...(filter?.name && { name: filter.name }),
      ...(filter?.minPrice && { minPrice: filter.minPrice.toString() }),
      ...(filter?.maxPrice && { maxPrice: filter.maxPrice.toString() }),
      ...(filter?.visible !== undefined && { visible: filter.visible.toString() }),
    });

    const response = await api.get(`/products?${params.toString()}`);
    return handleApiResponse(response);
  }



  // Create product
  static async createProduct(productData: ProductFormData): Promise<void> {
    await api.post('/products', productData);
  }

  // Update product
  static async updateProduct(id: number, productData: Partial<ProductFormData>): Promise<void> {
    await api.put(`/products/${id}`, productData);
  }

  // Delete product
  static async deleteProduct(id: number): Promise<void> {
    await api.delete(`/products/${id}`);
  }

  // Toggle product visibility
  static async toggleProductVisibility(id: number, visible: boolean): Promise<void> {
    await api.patch(`/products/${id}/visibility`, { visible });
  }

  // Get visible product list (for cart page)
  static async getVisibleProducts(): Promise<Product[]> {
    const response = await api.get('/products/visible/simple');
    return handleApiResponse(response);
  }

  // Get visible products with search (for cart page)
  static async getVisibleProductsWithSearch(name?: string, pageNum: number = 1, pageSize: number = 10): Promise<PaginatedResponse<Product>> {
    const params = new URLSearchParams();
    if (name && name.trim()) {
      params.append('name', name.trim());
    }
    params.append('pageNum', pageNum.toString());
    params.append('pageSize', pageSize.toString());
    
    const response = await api.get(`/products/visible?${params.toString()}`);
    return handleApiResponse(response);
  }
}

export default ProductService; 