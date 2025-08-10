import React from 'react';
import { Layout } from 'antd';
import { Outlet } from 'react-router-dom';
import Header from './Header';
import styles from './MainLayout.module.css';

const { Content } = Layout;

interface MainLayoutProps {
  className?: string;
}

const MainLayout: React.FC<MainLayoutProps> = ({ className }) => {
  // Get current window width
  const isMobile = typeof window !== 'undefined' && window.innerWidth <= 768;
  const isSmallMobile = typeof window !== 'undefined' && window.innerWidth <= 480;
  
  return (
    <Layout className={`${styles.mainLayout} ${className || ''}`}>
      {/* Header Navigation */}
      <Header />
      
      {/* Content Area */}
      <Content className={`${styles.content} ${isSmallMobile ? styles.contentSmallMobile : isMobile ? styles.contentMobile : ''}`}>
        <div className={`${styles.contentWrapper} ${isSmallMobile ? styles.contentWrapperSmallMobile : isMobile ? styles.contentWrapperMobile : ''}`}>
          <Outlet />
        </div>
      </Content>
    </Layout>
  );
};

export default MainLayout; 