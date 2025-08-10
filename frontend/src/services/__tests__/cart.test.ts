import { CartService } from '../cart';
import api from '../api';
import { Cart } from '../../types';

// Mock the api module
jest.mock('../api');
const mockedApi = api as jest.Mocked<typeof api>;

describe('CartService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getCart', () => {
    it('should fetch cart successfully', async () => {
      const mockCart: Cart = {
        id: 1,
        // userId: 'user123',
        items: [
          {
            id: 1,
            productId: 1,
            productName: 'Product 1',
            price: 100,
            quantity: 2,
            totalPrice: 200,
          },
        ],
        totalAmount: 200,
        itemCount: 2,
      };

      const mockResponse = {
        data: {
          success: true,
          data: mockCart,
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await CartService.getCart();

      expect(mockedApi.get).toHaveBeenCalledWith('/cart');
      expect(result).toEqual(mockCart);
    });
  });

  describe('addToCart', () => {
    it('should add product to cart with default quantity', async () => {
      const productId = 1;
      const quantity = 1;

      mockedApi.post.mockResolvedValue({} as any);

      await CartService.addToCart(productId);

      expect(mockedApi.post).toHaveBeenCalledWith('/cart/items', {
        productId,
        quantity,
      });
    });

    it('should add product to cart with custom quantity', async () => {
      const productId = 2;
      const quantity = 3;

      mockedApi.post.mockResolvedValue({} as any);

      await CartService.addToCart(productId, quantity);

      expect(mockedApi.post).toHaveBeenCalledWith('/cart/items', {
        productId,
        quantity,
      });
    });
  });

  describe('updateCartItem', () => {
    it('should update cart item quantity', async () => {
      const itemId = 1;
      const quantity = 5;

      mockedApi.put.mockResolvedValue({} as any);

      await CartService.updateCartItem(itemId, quantity);

      expect(mockedApi.put).toHaveBeenCalledWith(`/cart/items/${itemId}`, {
        quantity,
      });
    });
  });

  describe('removeFromCart', () => {
    it('should remove item from cart', async () => {
      const itemId = 1;

      mockedApi.delete.mockResolvedValue({} as any);

      await CartService.removeFromCart(itemId);

      expect(mockedApi.delete).toHaveBeenCalledWith(`/cart/items/${itemId}`);
    });
  });

  describe('clearCart', () => {
    it('should clear entire cart', async () => {
      mockedApi.delete.mockResolvedValue({} as any);

      await CartService.clearCart();

      expect(mockedApi.delete).toHaveBeenCalledWith('/cart');
    });
  });

  describe('Error Handling', () => {
    it('should handle API errors gracefully', async () => {
      const error = new Error('API Error');
      mockedApi.get.mockRejectedValue(error);

      await expect(CartService.getCart()).rejects.toThrow('API Error');
    });

    it('should handle network errors', async () => {
      const error = new Error('Network Error');
      mockedApi.post.mockRejectedValue(error);

      await expect(CartService.addToCart(1)).rejects.toThrow('Network Error');
    });

    it('should handle cart not found error', async () => {
      const error = new Error('Cart not found');
      mockedApi.get.mockRejectedValue(error);

      await expect(CartService.getCart()).rejects.toThrow('Cart not found');
    });
  });

  describe('Edge Cases', () => {
    it('should handle empty cart response', async () => {
      const mockEmptyCart: Cart = {
        id: 1,
        // userId: 'user123',
        items: [],
        totalAmount: 0,
        itemCount: 0,
      };

      const mockResponse = {
        data: {
          success: true,
          data: mockEmptyCart,
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await CartService.getCart();

      expect(result).toEqual(mockEmptyCart);
      expect(result.items).toHaveLength(0);
      expect(result.totalAmount).toBe(0);
      expect(result.itemCount).toBe(0);
    });

    it('should handle zero quantity in update', async () => {
      const itemId = 1;
      const quantity = 0;

      mockedApi.put.mockResolvedValue({} as any);

      await CartService.updateCartItem(itemId, quantity);

      expect(mockedApi.put).toHaveBeenCalledWith(`/cart/items/${itemId}`, {
        quantity,
      });
    });

    it('should handle large quantity values', async () => {
      const itemId = 1;
      const quantity = 999;

      mockedApi.put.mockResolvedValue({} as any);

      await CartService.updateCartItem(itemId, quantity);

      expect(mockedApi.put).toHaveBeenCalledWith(`/cart/items/${itemId}`, {
        quantity,
      });
    });
  });
}); 