import axios, { AxiosInstance, AxiosResponse, AxiosError } from 'axios';
import { message } from 'antd';
import { ApiResponse } from '../types';

// Configure global settings for message component to ensure prompt messages are displayed on top
message.config({
  top: 100,
  maxCount: 3,
  duration: 4,
});

// User ID storage key name
const USER_ID_KEY = 'shop_user_id';

// Get or generate user ID
const getUserId = (): string => {
  let userId = localStorage.getItem(USER_ID_KEY);
  if (!userId) {
    // Generate pure numeric user ID (ensure it can be parsed as Long by backend)
    userId = Date.now().toString();
    localStorage.setItem(USER_ID_KEY, userId);
  }
  return userId;
};

// Create axios instance
const api: AxiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Enable cookies support for Session management
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    // Add user ID header to each request
    const userId = getUserId();
    config.headers['X-User-ID'] = userId;
    
    return config;
  },
  (error: AxiosError) => {
    console.error('API Request Error:', error);
    message.error('Request failed, please check network connection');
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response: AxiosResponse) => {
    // Check if there's a new user ID in response headers
    const newUserId = response.headers['x-user-id'] || response.headers['X-User-ID'];
    const currentUserId = localStorage.getItem(USER_ID_KEY);
    
    if (newUserId) {
      if (currentUserId !== newUserId) {
        localStorage.setItem(USER_ID_KEY, newUserId);
      }
    }
    
    return response;
  },
  (error: AxiosError) => {
    
    // Unified error handling and display
    handleApiError(error);
    
    return Promise.reject(error);
  }
);

/**
 * Unified API error handling and display
 */
const handleApiError = (error: AxiosError) => {
  const response = error.response;
  
  if (!response) {
    // Network error or request timeout
    message.error('Network connection error, please check network settings');
    return;
  }
  
  const { status, data } = response;
  
  switch (status) {
    case 400:
      // Parameter error
      const errorMessage = (data as any)?.message || (data as any)?.error || 'Request parameter error';
      message.error(errorMessage);
      break;
      
    case 401:
      // Unauthorized
      message.error('Unauthorized access, please login again');
      break;
      
    case 403:
      // Forbidden
      message.error('Access denied, insufficient permissions');
      break;
      
    case 404:
      // Resource not found
      message.error('Requested resource not found');
      break;
      
    case 409:
      // Conflict
      const conflictMessage = (data as any)?.message || (data as any)?.error || 'Data conflict';
      message.error(conflictMessage);
      break;
      
    case 422:
      // Data validation failed
      const validationMessage = (data as any)?.message || (data as any)?.error || 'Data validation failed';
      message.error(validationMessage);
      break;
      
    case 429:
      // Too many requests
      message.warning('Too many requests, please try again later');
      break;
      
    case 500:
      // Internal server error
      message.error('Internal server error, please try again later');
      break;
      
    case 502:
      // Bad gateway
      message.error('Service temporarily unavailable, please try again later');
      break;
      
    case 503:
      // Service unavailable
      message.error('Service under maintenance, please try again later');
      break;
      
    default:
      // Other errors
      const defaultMessage = (data as any)?.message || (data as any)?.error || `Request failed (${status})`;
      message.error(defaultMessage);
      break;
  }
};

/**
 * Handle API response data
 */
export const handleApiResponse = <T>(response: AxiosResponse<ApiResponse<T>>): T => {
  const { data } = response;
  if (data.success) {
    return data.data;
  } else {
    // When success is false, display business error message and throw exception
    const errorMessage = data.message || 'API request failed';
    message.error(errorMessage);
    throw new Error(errorMessage);
  }
};

export default api; 