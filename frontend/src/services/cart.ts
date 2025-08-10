import api, { handleApiResponse } from './api';
import { Cart } from '../types';

// Cart API service class
export class CartService {
  // Get cart
  static async getCart(): Promise<Cart> {
    const response = await api.get('/cart');
    return handleApiResponse(response);
  }

  // Add product to cart
  static async addToCart(productId: number, quantity: number = 1): Promise<void> {
    await api.post('/cart/items', {
      productId,
      quantity,
    });
  }

  // Update cart item quantity
  static async updateCartItem(itemId: number, quantity: number): Promise<void> {
    await api.put(`/cart/items/${itemId}`, {
      quantity,
    });
  }

  // Remove item from cart
  static async removeFromCart(itemId: number): Promise<void> {
    await api.delete(`/cart/items/${itemId}`);
  }

  // Clear cart
  static async clearCart(): Promise<void> {
    await api.delete('/cart');
  }


}

export default CartService; 