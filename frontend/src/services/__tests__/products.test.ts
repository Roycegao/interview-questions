import { ProductService } from '../products';
import api from '../api';
import { Product, ProductFilter, ProductFormData, PaginatedResponse } from '../../types';

// Mock the api module
jest.mock('../api');
const mockedApi = api as jest.Mocked<typeof api>;

describe('ProductService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getProducts', () => {
    it('should fetch products with default pagination', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: {
            list: [
              { id: 1, name: 'Product 1', price: 100 },
              { id: 2, name: 'Product 2', price: 200 },
            ],
            total: 2,
            pageNum: 1,
            pageSize: 10,
          },
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await ProductService.getProducts();

      expect(mockedApi.get).toHaveBeenCalledWith('/products?pageNum=1&pageSize=10');
      expect(result).toEqual(mockResponse.data.data);
    });

    it('should fetch products with custom pagination', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: {
            list: [{ id: 1, name: 'Product 1', price: 100 }],
            total: 1,
            pageNum: 2,
            pageSize: 5,
          },
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await ProductService.getProducts(2, 5);

      expect(mockedApi.get).toHaveBeenCalledWith('/products?pageNum=2&pageSize=5');
      expect(result).toEqual(mockResponse.data.data);
    });

    it('should fetch products with name filter', async () => {
      const filter: ProductFilter = { name: 'test' };
      const mockResponse = {
        data: {
          success: true,
          data: {
            list: [{ id: 1, name: 'test product', price: 100 }],
            total: 1,
            pageNum: 1,
            pageSize: 10,
          },
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await ProductService.getProducts(1, 10, filter);

      expect(mockedApi.get).toHaveBeenCalledWith('/products?pageNum=1&pageSize=10&name=test');
      expect(result).toEqual(mockResponse.data.data);
    });
  });

  describe('createProduct', () => {
    it('should create a new product', async () => {
              const productData: ProductFormData = {
          name: 'New Product',
          price: 99.99,
          quantity: 100,
          visible: true,
        };

      mockedApi.post.mockResolvedValue({} as any);

      await ProductService.createProduct(productData);

      expect(mockedApi.post).toHaveBeenCalledWith('/products', productData);
    });
  });

  describe('updateProduct', () => {
    it('should update an existing product', async () => {
      const productId = 1;
      const updateData: Partial<ProductFormData> = {
        name: 'Updated Product',
        price: 149.99,
      };

      mockedApi.put.mockResolvedValue({} as any);

      await ProductService.updateProduct(productId, updateData);

      expect(mockedApi.put).toHaveBeenCalledWith(`/products/${productId}`, updateData);
    });
  });

  describe('deleteProduct', () => {
    it('should delete a product', async () => {
      const productId = 1;

      mockedApi.delete.mockResolvedValue({} as any);

      await ProductService.deleteProduct(productId);

      expect(mockedApi.delete).toHaveBeenCalledWith(`/products/${productId}`);
    });
  });

  describe('toggleProductVisibility', () => {
    it('should toggle product visibility', async () => {
      const productId = 1;
      const visible = false;

      mockedApi.patch.mockResolvedValue({} as any);

      await ProductService.toggleProductVisibility(productId, visible);

      expect(mockedApi.patch).toHaveBeenCalledWith(`/products/${productId}/visibility`, { visible });
    });
  });

  describe('getVisibleProducts', () => {
    it('should fetch visible products for cart', async () => {
      const mockProducts: Product[] = [
        { id: 1, name: 'Product 1', price: 100, quantity: 50, visible: true },
        { id: 2, name: 'Product 2', price: 200, quantity: 30, visible: true },
      ];

      const mockResponse = {
        data: {
          success: true,
          data: mockProducts,
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await ProductService.getVisibleProducts();

      expect(mockedApi.get).toHaveBeenCalledWith('/products/visible/simple');
      expect(result).toEqual(mockProducts);
    });
  });

  describe('getVisibleProductsWithSearch', () => {
    it('should fetch visible products with search and pagination', async () => {
      const searchName = 'test';
      const mockResponse = {
        data: {
          success: true,
          data: {
            list: [{ id: 1, name: 'test product', price: 100, visible: true }],
            total: 1,
            pageNum: 1,
            pageSize: 10,
          },
        },
      };

      mockedApi.get.mockResolvedValue(mockResponse as any);

      const result = await ProductService.getVisibleProductsWithSearch(searchName, 1, 10);

      expect(mockedApi.get).toHaveBeenCalledWith('/products/visible?name=test&pageNum=1&pageSize=10');
      expect(result).toEqual(mockResponse.data.data);
    });
  });
}); 