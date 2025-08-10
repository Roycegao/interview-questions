import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Product, ProductFilter, ProductFormData } from '../../types';
import ProductService from '../../services/products';

// Product state interface
interface ProductsState {
  products: Product[];
  loading: boolean;
  error: string | null;
  pagination: {
    pageNum: number;
    pageSize: number;
    total: number;
    totalPages: number;
  };
  filter: ProductFilter;
  shouldRefresh: boolean; // Flag to indicate if list needs refresh
}

// Initial state
const initialState: ProductsState = {
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

// Async action: fetch product list
export const fetchProducts = createAsyncThunk(
  'products/fetchProducts',
  async ({ pageNum, pageSize, filter }: { pageNum?: number; pageSize?: number; filter?: ProductFilter }, { rejectWithValue }) => {
    try {
      const response = await ProductService.getProducts(pageNum, pageSize, filter);
      return response;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to fetch products');
    }
  }
);

// Async action: create product
export const createProduct = createAsyncThunk(
  'products/createProduct',
  async (productData: ProductFormData, { rejectWithValue }) => {
    try {
      await ProductService.createProduct(productData);
      return productData;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to create product');
    }
  }
);

// Async action: update product
export const updateProduct = createAsyncThunk(
  'products/updateProduct',
  async ({ id, productData }: { id: number; productData: Partial<ProductFormData> }, { rejectWithValue }) => {
    try {
      await ProductService.updateProduct(id, productData);
      return { id, productData };
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to update product');
    }
  }
);

// Async action: delete product
export const deleteProduct = createAsyncThunk(
  'products/deleteProduct',
  async (id: number, { rejectWithValue }) => {
    try {
      await ProductService.deleteProduct(id);
      return id;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to delete product');
    }
  }
);

// Async action: toggle product visibility
export const toggleProductVisibility = createAsyncThunk(
  'products/toggleProductVisibility',
  async ({ id, visible }: { id: number; visible: boolean }, { rejectWithValue }) => {
    try {
      await ProductService.toggleProductVisibility(id, visible);
      return { id, visible };
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to toggle product visibility');
    }
  }
);

// Async action: fetch visible products
export const fetchVisibleProducts = createAsyncThunk(
  'products/fetchVisibleProducts',
  async (_, { rejectWithValue }) => {
    try {
      const response = await ProductService.getVisibleProducts();
      return response;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to fetch visible products');
    }
  }
);

// Async action: fetch visible products with search
export const fetchVisibleProductsWithSearch = createAsyncThunk(
  'products/fetchVisibleProductsWithSearch',
  async ({ name, pageNum, pageSize }: { name?: string; pageNum?: number; pageSize?: number }, { rejectWithValue }) => {
    try {
      const response = await ProductService.getVisibleProductsWithSearch(name, pageNum, pageSize);
      return response;
    } catch (error) {
      return rejectWithValue(error instanceof Error ? error.message : 'Failed to fetch visible products with search');
    }
  }
);



// Product slice
const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    // Set filter
    setFilter: (state, action: PayloadAction<ProductFilter>) => {
      state.filter = { ...state.filter, ...action.payload };
      // Reset pagination to first page when filter changes
      state.pagination.pageNum = 1;
    },

    // Set pagination
    setPagination: (state, action: PayloadAction<{ pageNum: number; pageSize: number }>) => {
      state.pagination = { ...state.pagination, ...action.payload };
    },
    // Clear error
    clearError: (state) => {
      state.error = null;
    },
    // Reset refresh flag
    resetRefreshFlag: (state) => {
      state.shouldRefresh = false;
    },
  },
  extraReducers: (builder) => {
    // fetchProducts
    builder
      .addCase(fetchProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.products = action.payload.list;
        state.pagination = action.payload.pagination;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // createProduct
    builder
      .addCase(createProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createProduct.fulfilled, (state, action) => {
        state.loading = false;
        // After creating product, set flag to trigger data refresh
        state.shouldRefresh = true;
      })
      .addCase(createProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // updateProduct
    builder
      .addCase(updateProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateProduct.fulfilled, (state, action) => {
        state.loading = false;
        // After updating product, set flag to trigger data refresh
        state.shouldRefresh = true;
      })
      .addCase(updateProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // deleteProduct
    builder
      .addCase(deleteProduct.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteProduct.fulfilled, (state, action) => {
        state.loading = false;
        // After deleting product, set flag to trigger data refresh
        state.shouldRefresh = true;
      })
      .addCase(deleteProduct.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // toggleProductVisibility
    builder
      .addCase(toggleProductVisibility.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(toggleProductVisibility.fulfilled, (state, action) => {
        state.loading = false;
        // After toggling visibility, set flag to trigger data refresh
        state.shouldRefresh = true;
      })
      .addCase(toggleProductVisibility.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // fetchVisibleProducts
    builder
      .addCase(fetchVisibleProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchVisibleProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.products = Array.isArray(action.payload) ? action.payload : [];
      })
      .addCase(fetchVisibleProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

    // fetchVisibleProductsWithSearch
    builder
      .addCase(fetchVisibleProductsWithSearch.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchVisibleProductsWithSearch.fulfilled, (state, action) => {
        state.loading = false;
        // Handle both paginated and non-paginated responses
        if (action.payload && typeof action.payload === 'object' && 'list' in action.payload) {
          // Paginated response
          state.products = action.payload.list;
          state.pagination = action.payload.pagination;
        } else {
          // Non-paginated response (fallback)
          state.products = Array.isArray(action.payload) ? action.payload : [];
        }
      })
      .addCase(fetchVisibleProductsWithSearch.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });

  },
});

export const { setFilter, setPagination, clearError, resetRefreshFlag } = productsSlice.actions;
export default productsSlice.reducer; 