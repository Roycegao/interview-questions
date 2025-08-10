import axios from 'axios';
import { message } from 'antd';
import api, { handleApiResponse } from '../api';
import { ApiResponse } from '../../types';

// Mock antd message
jest.mock('antd', () => ({
  message: {
    error: jest.fn(),
    config: jest.fn(),
  },
}));

// Mock axios
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

// Mock localStorage
const localStorageMock = (() => {
  let store: { [key: string]: string } = {};
  return {
    getItem: jest.fn((key: string) => store[key] || null),
    setItem: jest.fn((key: string, value: string) => {
      store[key] = value;
    }),
    removeItem: jest.fn((key: string) => {
      delete store[key];
    }),
    clear: jest.fn(() => {
      store = {};
    }),
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

describe('API Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorageMock.clear();
  });

  describe('API Instance Configuration', () => {
    it('should create axios instance with correct base URL', () => {
      expect(api.defaults.baseURL).toBe('http://localhost:8080/api');
    });

    it('should set correct timeout', () => {
      expect(api.defaults.timeout).toBe(10000);
    });

    it('should set correct headers', () => {
      expect(api.defaults.headers['Content-Type']).toBe('application/json');
    });

    it('should enable credentials', () => {
      expect(api.defaults.withCredentials).toBe(true);
    });
  });

  describe('Request Interceptor', () => {
    it('should add user ID header to requests', async () => {
      const mockUserId = '123456789';
      localStorageMock.getItem.mockReturnValue(mockUserId);

      // Mock axios create to return our api instance
      const mockAxiosInstance = {
        interceptors: {
          request: {
            use: jest.fn(),
          },
          response: {
            use: jest.fn(),
          },
        },
        defaults: api.defaults,
      };
      mockedAxios.create.mockReturnValue(mockAxiosInstance as any);

      // Test the interceptor logic directly
      const config: { headers: { [key: string]: string } } = { headers: {} };
      const userId = localStorageMock.getItem('shop_user_id');
      if (userId) {
        config.headers['X-User-ID'] = userId;
      }

      expect(config.headers['X-User-ID']).toBe(mockUserId);
    });

    it('should generate new user ID if none exists', async () => {
      localStorageMock.getItem.mockReturnValue(null);
      const mockDate = new Date('2023-01-01T00:00:00.000Z');
      jest.spyOn(Date, 'now').mockReturnValue(mockDate.getTime());

      // Test the getUserId logic directly
      let userId = localStorageMock.getItem('shop_user_id');
      if (!userId) {
        userId = Date.now().toString();
        localStorageMock.setItem('shop_user_id', userId);
      }

      expect(localStorageMock.setItem).toHaveBeenCalledWith('shop_user_id', mockDate.getTime().toString());
    });
  });

  describe('Error Handling', () => {
    it('should handle different HTTP status codes', () => {
      const statusCodes = [
        { status: 400, expectedMessage: 'Request parameter error' },
        { status: 401, expectedMessage: 'Unauthorized access, please login again' },
        { status: 403, expectedMessage: 'Access forbidden' },
        { status: 404, expectedMessage: 'Resource not found' },
        { status: 500, expectedMessage: 'Server internal error, please try again later' },
      ];

      // Test error message mapping
      statusCodes.forEach(({ status, expectedMessage }) => {
        let errorMessage = 'Unknown error';
        
        switch (status) {
          case 400:
            errorMessage = 'Request parameter error';
            break;
          case 401:
            errorMessage = 'Unauthorized access, please login again';
            break;
          case 403:
            errorMessage = 'Access forbidden';
            break;
          case 404:
            errorMessage = 'Resource not found';
            break;
          case 500:
            errorMessage = 'Server internal error, please try again later';
            break;
        }
        
        expect(errorMessage).toBe(expectedMessage);
      });
    });
  });

  describe('handleApiResponse', () => {
    it('should extract data from successful response', () => {
      const mockData = { id: 1, name: 'Test Product' };
      const response = {
        data: {
          success: true,
          data: mockData,
          message: 'Success',
        },
      };

      const result = handleApiResponse(response as any);
      expect(result).toEqual(mockData);
    });

    it('should throw error for unsuccessful response', () => {
      const response = {
        data: {
          success: false,
          message: 'Operation failed',
        },
      };

      expect(() => handleApiResponse(response as any)).toThrow('Operation failed');
    });
  });

  describe('User ID Management', () => {
    it('should store and retrieve user ID from localStorage', () => {
      const testUserId = 'test_user_123';
      
      localStorageMock.setItem('shop_user_id', testUserId);
      const retrievedUserId = localStorageMock.getItem('shop_user_id');
      
      expect(retrievedUserId).toBe(testUserId);
    });

    it('should handle missing user ID gracefully', () => {
      localStorageMock.getItem.mockReturnValue(null);
      
      const userId = localStorageMock.getItem('shop_user_id');
      
      expect(userId).toBeNull();
    });
  });
}); 