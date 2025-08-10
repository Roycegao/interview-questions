import cartReducer, {
  addToCart,
  removeFromCart,
  updateCartItem,
  clearCart,
  fetchCart
} from '../slices/cartSlice';

// Mock initial state based on the actual slice
const mockInitialState = {
  cart: null,
  loading: false,
  error: null,
};

describe('Cart Slice', () => {
  test('should return the initial state', () => {
    expect(cartReducer(undefined, { type: 'unknown' })).toEqual(mockInitialState);
  });

  test('should handle fetchCart.pending', () => {
    const action = { type: fetchCart.pending.type };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle fetchCart.fulfilled', () => {
    const mockCart = {
      id: 1,
      items: [
        {
          id: 1,
          productId: 1,
          productName: 'Test Product',
          price: 10.99,
          quantity: 1,
          totalPrice: 10.99
        }
      ],
      totalAmount: 10.99,
      itemCount: 1
    };
    const action = { type: fetchCart.fulfilled.type, payload: mockCart };
    const state = cartReducer(mockInitialState, action);
    expect(state.cart).toEqual(mockCart);
    expect(state.loading).toBe(false);
    expect(state.error).toBe(null);
  });

  test('should handle fetchCart.rejected', () => {
    const action = { type: fetchCart.rejected.type, payload: 'Error message' };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.error).toBe('Error message');
  });

  test('should handle addToCart.pending', () => {
    const action = { type: addToCart.pending.type };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle addToCart.fulfilled', () => {
    const action = { type: addToCart.fulfilled.type, payload: {} };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
  });

  test('should handle addToCart.rejected', () => {
    const action = { type: addToCart.rejected.type, payload: 'Error message' };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.error).toBe('Error message');
  });

  test('should handle updateCartItem.pending', () => {
    const action = { type: updateCartItem.pending.type };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle updateCartItem.fulfilled', () => {
    const action = { type: updateCartItem.fulfilled.type, payload: {} };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
  });

  test('should handle removeFromCart.pending', () => {
    const action = { type: removeFromCart.pending.type };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle removeFromCart.fulfilled', () => {
    const action = { type: removeFromCart.fulfilled.type, payload: {} };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
  });

  test('should handle clearCart.pending', () => {
    const action = { type: clearCart.pending.type };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle clearCart.fulfilled', () => {
    const action = { type: clearCart.fulfilled.type, payload: null };
    const state = cartReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.cart).toBe(null);
  });
}); 