import React, { useState, useEffect } from 'react';
import { Layout, Menu, Button, Space } from 'antd';
import { ShoppingCartOutlined, UserOutlined, HomeOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';
import styles from './Header.module.css';

const { Header: AntHeader } = Layout;

interface HeaderProps {
  className?: string;
}

const Header: React.FC<HeaderProps> = ({ className }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const cart = useSelector((state: RootState) => state.cart.cart);
  
  // Use state for responsive detection
  const [isMobile, setIsMobile] = useState(false);
  const [isSmallMobile, setIsSmallMobile] = useState(false);

  // Handle window resize
  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
      setIsSmallMobile(window.innerWidth <= 480);
    };

    // Set initial values
    handleResize();

    // Add event listener
    window.addEventListener('resize', handleResize);

    // Cleanup
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  // Calculate cart item count
  const cartItemCount = cart?.itemCount || 0;

  // Menu items configuration
  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: 'Home',
    },
    {
      key: '/admin',
      icon: <UserOutlined />,
      label: 'Admin',
    },
    {
      key: '/shop-cart',
      icon: <ShoppingCartOutlined />,
      label: `Cart ${cartItemCount > 0 ? `(${cartItemCount})` : ''}`,
    },
  ];

  // Handle menu click
  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key);
  };

  // Desktop Layout
  if (!isMobile && !isSmallMobile) {
    return (
      <AntHeader className={`${styles.header} ${className || ''}`}>
        <div className={styles.headerContainer}>
          {/* Logo */}
          <div className={styles.logo}>
            <ShoppingCartOutlined className={styles.logoIcon} />
            Basic Shop
          </div>

          {/* Navigation Menu */}
          <Menu
            mode="horizontal"
            selectedKeys={[location.pathname]}
            items={menuItems}
            onClick={handleMenuClick}
            className={styles.navigationMenu}
          />

          {/* Right Action Area */}
          <Space size="middle" className={styles.actionSpace}>
            <Button 
              type="primary" 
              icon={<ShoppingCartOutlined />}
              onClick={() => navigate('/shop-cart')}
              size="middle"
            >
              Cart {cartItemCount > 0 && `(${cartItemCount})`}
            </Button>
          </Space>
        </div>
      </AntHeader>
    );
  }

  // Mobile Layout
  return (
    <AntHeader className={`${styles.header} ${className || ''} ${isSmallMobile ? styles.headerSmallMobile : styles.headerMobile}`}>
      <div className={`${styles.headerContainer} ${isSmallMobile ? styles.headerContainerSmallMobile : styles.headerContainerMobile}`}>
        {/* First Row: Logo and Cart Button */}
        <div className={`${styles.mobileTopRow} ${isSmallMobile ? styles.mobileTopRowSmall : ''}`}>
          {/* Logo */}
          <div className={`${styles.logo} ${isSmallMobile ? styles.logoSmallMobile : styles.logoMobile}`}>
            <ShoppingCartOutlined className={`${styles.logoIcon} ${isSmallMobile ? styles.logoIconSmallMobile : styles.logoIconMobile}`} />
            Basic Shop
          </div>

          {/* Top Right Cart Button */}
          <Button 
            type="primary" 
            icon={<ShoppingCartOutlined />}
            onClick={() => navigate('/shop-cart')}
            size={isSmallMobile ? 'small' : 'small'}
            className={`${styles.mobileCartButton} ${isSmallMobile ? styles.mobileCartButtonSmall : ''}`}
          >
            {cartItemCount > 0 && `(${cartItemCount})`}
          </Button>
        </div>

        {/* Second Row: Navigation Menu */}
        <Menu
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={handleMenuClick}
          className={`${styles.navigationMenu} ${isSmallMobile ? styles.navigationMenuSmallMobile : styles.navigationMenuMobile}`}
        />
      </div>
    </AntHeader>
  );
};

export default Header; 