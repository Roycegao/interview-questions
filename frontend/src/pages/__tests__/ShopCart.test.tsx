import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productsReducer from '../../store/slices/productsSlice';
import cartReducer from '../../store/slices/cartSlice';
import ShopCart from '../ShopCart';

const createTestStore = () => {
  return configureStore({
    reducer: {
      products: productsReducer,
      cart: cartReducer,
    },
  });
};

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

describe('ShopCart Page', () => {
  test('renders shop cart page title', () => {
    render(
      <TestWrapper>
        <ShopCart />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Shopping Cart/i)).toBeInTheDocument();
  });

  test('renders products section', () => {
    render(
      <TestWrapper>
        <ShopCart />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Available Products/i)).toBeInTheDocument();
  });

  test('renders cart section', () => {
    render(
      <TestWrapper>
        <ShopCart />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Your Cart/i)).toBeInTheDocument();
  });

  test('renders empty cart message initially', () => {
    render(
      <TestWrapper>
        <ShopCart />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Your cart is empty/i)).toBeInTheDocument();
  });
}); 