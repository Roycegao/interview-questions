import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productsReducer from '../../store/slices/productsSlice';
import cartReducer from '../../store/slices/cartSlice';
import Admin from '../Admin';

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

describe('Admin Page', () => {
  test('renders admin page title', () => {
    render(
      <TestWrapper>
        <Admin />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Product Management/i)).toBeInTheDocument();
  });

  test('renders add product button', () => {
    render(
      <TestWrapper>
        <Admin />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Add Product/i)).toBeInTheDocument();
  });

  test('renders product table', () => {
    render(
      <TestWrapper>
        <Admin />
      </TestWrapper>
    );
    
    expect(screen.getByText(/Name/i)).toBeInTheDocument();
    expect(screen.getByText(/Price/i)).toBeInTheDocument();
    expect(screen.getByText(/Category/i)).toBeInTheDocument();
    expect(screen.getByText(/Actions/i)).toBeInTheDocument();
  });

  test('opens add product modal when clicking add button', async () => {
    render(
      <TestWrapper>
        <Admin />
      </TestWrapper>
    );
    
    const addButton = screen.getByText(/Add Product/i);
    fireEvent.click(addButton);
    
    await waitFor(() => {
      expect(screen.getByText(/Add New Product/i)).toBeInTheDocument();
    });
  });
}); 