import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import productsReducer from '../../store/slices/productsSlice';
import cartReducer from '../../store/slices/cartSlice';
import MainLayout from '../layout/MainLayout';

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

describe('MainLayout Component', () => {
  test('renders header', () => {
    render(
      <TestWrapper>
        <Routes>
          <Route path="/" element={<MainLayout />}>
            <Route index element={<div data-testid="test-content">Test Content</div>} />
          </Route>
        </Routes>
      </TestWrapper>
    );
    
    expect(screen.getByText(/Basic Shop/i)).toBeInTheDocument();
  });

  test('renders content area', () => {
    render(
      <TestWrapper>
        <Routes>
          <Route path="/" element={<MainLayout />}>
            <Route index element={<div data-testid="test-content">Test Content</div>} />
          </Route>
        </Routes>
      </TestWrapper>
    );
    
    expect(screen.getByTestId('test-content')).toBeInTheDocument();
  });
}); 