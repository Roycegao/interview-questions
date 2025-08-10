import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productsReducer from '../../store/slices/productsSlice';
import cartReducer from '../../store/slices/cartSlice';
import Home from '../Home';

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

describe('Home Page', () => {
  test('renders welcome message', () => {
    render(
      <TestWrapper>
        <Home />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Welcome to Basic Shop/i)).toBeInTheDocument();
  });

  test('renders navigation cards', () => {
    render(
      <TestWrapper>
        <Home />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Admin Panel/i)).toBeInTheDocument();
    expect(screen.getByText(/Shop Cart/i)).toBeInTheDocument();
  });

  test('renders feature descriptions', () => {
    render(
      <TestWrapper>
        <Home />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Manage products/i)).toBeInTheDocument();
    expect(screen.getByText(/View shopping cart/i)).toBeInTheDocument();
  });
}); 