import React from 'react';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import GlobalErrorHandler from '../GlobalErrorHandler';

// Create a simple store for testing
const createTestStore = (initialState = {}) => {
  return configureStore({
    reducer: {
      // Add a simple reducer to simulate error state
      error: (state = initialState, action: any) => {
        if (action.type === 'SET_ERROR') {
          return { message: action.payload };
        }
        return state;
      },
    },
    preloadedState: initialState,
  });
};

describe('GlobalErrorHandler', () => {
  it('should render without crashing', () => {
    const store = createTestStore();
    
    render(
      <Provider store={store}>
        <GlobalErrorHandler />
      </Provider>
    );
    
    // Component should render normally even without errors
    expect(document.body).toBeInTheDocument();
  });

  it('should display error message when error exists', () => {
    const store = createTestStore({ error: { message: 'Test error message' } });
    
    render(
      <Provider store={store}>
        <GlobalErrorHandler />
      </Provider>
    );
    
    // Should display error message
    expect(screen.getByText('Test error message')).toBeInTheDocument();
  });

  it('should not display anything when no error', () => {
    const store = createTestStore();
    
    const { container } = render(
      <Provider store={store}>
        <GlobalErrorHandler />
      </Provider>
    );
    
    // When there's no error, component should not display anything
    expect(container.firstChild).toBeNull();
  });
}); 