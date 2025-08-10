import productsReducer, {
  fetchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  toggleProductVisibility,
  setFilter,
  setPagination
} from '../slices/productsSlice';

// Mock initial state based on the actual slice
const mockInitialState = {
  products: [],
  loading: false,
  error: null,
  pagination: {
    pageNum: 1,
    pageSize: 10,
    total: 0,
    totalPages: 0,
  },
  filter: {},
  shouldRefresh: false,
};

describe('Products Slice', () => {
  test('should return the initial state', () => {
    expect(productsReducer(undefined, { type: 'unknown' })).toEqual(mockInitialState);
  });

  test('should handle fetchProducts.pending', () => {
    const action = { type: fetchProducts.pending.type };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(true);
    expect(state.error).toBe(null);
  });

  test('should handle fetchProducts.fulfilled', () => {
    const mockProducts = [
      { id: 1, name: 'Test Product', price: 10.99, category: 'Test', visible: true }
    ];
    const action = { 
      type: fetchProducts.fulfilled.type, 
      payload: { 
        list: mockProducts, 
        pagination: { pageNum: 1, pageSize: 10, total: 1, totalPages: 1 } 
      } 
    };
    const state = productsReducer(mockInitialState, action);
    expect(state.products).toEqual(mockProducts);
    expect(state.pagination.total).toBe(1);
    expect(state.loading).toBe(false);
    expect(state.error).toBe(null);
  });

  test('should handle fetchProducts.rejected', () => {
    const action = { 
      type: fetchProducts.rejected.type, 
      payload: 'Error message' 
    };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.error).toBe('Error message');
  });

  test('should handle createProduct.fulfilled', () => {
    const action = { type: createProduct.fulfilled.type, payload: {} };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.shouldRefresh).toBe(true);
  });

  test('should handle updateProduct.fulfilled', () => {
    const action = { type: updateProduct.fulfilled.type, payload: {} };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.shouldRefresh).toBe(true);
  });

  test('should handle deleteProduct.fulfilled', () => {
    const action = { type: deleteProduct.fulfilled.type, payload: 1 };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.shouldRefresh).toBe(true);
  });

  test('should handle toggleProductVisibility.fulfilled', () => {
    const action = { type: toggleProductVisibility.fulfilled.type, payload: { id: 1, visible: false } };
    const state = productsReducer(mockInitialState, action);
    expect(state.loading).toBe(false);
    expect(state.shouldRefresh).toBe(true);
  });

  test('should handle setFilter', () => {
    const filter = { name: 'Electronics' };
    const action = setFilter(filter);
    const state = productsReducer(mockInitialState, action);
    expect(state.filter).toEqual(filter);
    expect(state.pagination.pageNum).toBe(1);
  });

  test('should handle setPagination', () => {
    const pagination = { pageNum: 2, pageSize: 20 };
    const action = setPagination(pagination);
    const state = productsReducer(mockInitialState, action);
    expect(state.pagination.pageNum).toBe(2);
    expect(state.pagination.pageSize).toBe(20);
  });
}); 