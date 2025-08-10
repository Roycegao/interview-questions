import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productsReducer from '../store/slices/productsSlice';
import cartReducer from '../store/slices/cartSlice';
import App from '../App';

// 创建测试用的store
const createTestStore = () => {
  return configureStore({
    reducer: {
      products: productsReducer,
      cart: cartReducer,
    },
  });
};

// 测试用的包装器组件
const TestWrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const store = createTestStore();
  return (
    <Provider store={store}>
      <BrowserRouter>
        {children}
      </BrowserRouter>
    </Provider>
  );
};

describe('App Component', () => {
  test('renders without crashing', () => {
    render(
      <TestWrapper>
        <App />
      </TestWrapper>
    );
    expect(screen.getByText(/Basic Shop/i)).toBeInTheDocument();
  });

  test('renders navigation links', () => {
    render(
      <TestWrapper>
        <App />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Admin/i)).toBeInTheDocument();
    expect(screen.getByText(/Shop Cart/i)).toBeInTheDocument();
  });

  test('renders welcome message', () => {
    render(
      <TestWrapper>
        <App />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Welcome to Basic Shop/i)).toBeInTheDocument();
  });
}); 