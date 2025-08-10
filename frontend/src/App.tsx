import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Provider } from 'react-redux';
import { ConfigProvider, message } from 'antd';
import enUS from 'antd/lib/locale/en_US';
import { store } from './store';
import MainLayout from './components/layout/MainLayout';
import HomePage from './pages/Home';
import AdminPage from './pages/Admin';
import ShopCartPage from './pages/ShopCart';
import GlobalErrorHandler from './components/GlobalErrorHandler';

// Import global styles
import './styles/global.less';

// Configure global message
message.config({
  top: 100, // Distance from top
  duration: 3, // Display duration (seconds)
  maxCount: 3, // Maximum display count
  rtl: false, // Right to left
});

const App: React.FC = () => {
  return (
    <Provider store={store}>
      <ConfigProvider 
        locale={enUS}
        theme={{
          token: {
            // Custom theme color
            colorPrimary: '#1890ff',
            borderRadius: 6,
            // Responsive font and spacing
            fontSize: window.innerWidth <= 768 ? 12 : 14,
            fontSizeSM: window.innerWidth <= 768 ? 10 : 12,
            fontSizeLG: window.innerWidth <= 768 ? 14 : 16,
            // Responsive spacing
            padding: window.innerWidth <= 768 ? 8 : 12,
            paddingSM: window.innerWidth <= 768 ? 6 : 8,
            paddingLG: window.innerWidth <= 768 ? 12 : 16,
            margin: window.innerWidth <= 768 ? 8 : 12,
            marginSM: window.innerWidth <= 768 ? 6 : 8,
            marginLG: window.innerWidth <= 768 ? 12 : 16,
            // Responsive control sizes
            controlHeight: window.innerWidth <= 768 ? 28 : 32,
            controlHeightSM: window.innerWidth <= 768 ? 20 : 24,
            controlHeightLG: window.innerWidth <= 768 ? 36 : 40,
          },
          components: {
            // Responsive button
            Button: {
              controlHeight: window.innerWidth <= 768 ? 28 : 32,
              controlHeightSM: window.innerWidth <= 768 ? 20 : 24,
              controlHeightLG: window.innerWidth <= 768 ? 36 : 40,
              paddingInline: window.innerWidth <= 768 ? 12 : 16,
              paddingInlineSM: window.innerWidth <= 768 ? 8 : 12,
              paddingInlineLG: window.innerWidth <= 768 ? 16 : 20,
            },
            // Responsive input
            Input: {
              controlHeight: window.innerWidth <= 768 ? 28 : 32,
              controlHeightSM: window.innerWidth <= 768 ? 20 : 24,
              controlHeightLG: window.innerWidth <= 768 ? 36 : 40,
            },
            // Responsive table
            Table: {
              headerBg: '#fafafa',
              headerColor: '#262626',
              fontSize: window.innerWidth <= 768 ? 12 : 14,
              // Mobile table optimization
              cellPaddingBlock: window.innerWidth <= 768 ? 8 : 12,
              cellPaddingInline: window.innerWidth <= 768 ? 8 : 16,
            },
            // Responsive card
            Card: {
              paddingLG: window.innerWidth <= 768 ? 12 : 16,
              paddingMD: window.innerWidth <= 768 ? 8 : 12,
              paddingSM: window.innerWidth <= 768 ? 6 : 8,
            },
            // Mobile layout optimization
            Layout: {
              headerHeight: window.innerWidth <= 768 ? 48 : 64,
              headerPadding: window.innerWidth <= 768 ? '0 12px' : '0 24px',
            },
          },
        }}
      >
        <Router>
          <Routes>
            <Route path="/" element={<MainLayout />}>
              <Route index element={<HomePage />} />
              <Route path="admin" element={<AdminPage />} />
              <Route path="shop-cart" element={<ShopCartPage />} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Route>
          </Routes>
          <GlobalErrorHandler />
        </Router>
      </ConfigProvider>
    </Provider>
  );
};

export default App;
