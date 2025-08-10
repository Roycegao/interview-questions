import React from 'react';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import GlobalErrorHandler from '../GlobalErrorHandler';

// 创建一个简单的 store 用于测试
const createTestStore = (initialState = {}) => {
  return configureStore({
    reducer: {
      // 添加一个简单的 reducer 来模拟错误状态
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
    
    // 组件应该正常渲染，即使没有错误
    expect(document.body).toBeInTheDocument();
  });

  it('should display error message when error exists', () => {
    const store = createTestStore({ error: { message: 'Test error message' } });
    
    render(
      <Provider store={store}>
        <GlobalErrorHandler />
      </Provider>
    );
    
    // 应该显示错误消息
    expect(screen.getByText('Test error message')).toBeInTheDocument();
  });

  it('should not display anything when no error', () => {
    const store = createTestStore();
    
    const { container } = render(
      <Provider store={store}>
        <GlobalErrorHandler />
      </Provider>
    );
    
    // 没有错误时，组件应该不显示任何内容
    expect(container.firstChild).toBeNull();
  });
}); 