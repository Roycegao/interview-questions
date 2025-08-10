import React from 'react';
import { Card, Row, Col, Button, Typography } from 'antd';
import { ShoppingCartOutlined, UserOutlined, HomeOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import styles from './index.module.css';


const { Title } = Typography;

const HomePage: React.FC = () => {
  const navigate = useNavigate();

  // Feature cards configuration
  const featureCards = [
    {
      title: 'Product Management',
      icon: <UserOutlined className={`${styles.featureIcon} ${styles.adminIcon}`} />,
      action: () => navigate('/admin'),
      buttonText: 'Manage',
      cardClass: styles.adminCard,
      buttonClass: styles.adminButton,
    },
    {
      title: 'Cart',
      icon: <ShoppingCartOutlined className={`${styles.featureIcon} ${styles.shopIcon}`} />,
      action: () => navigate('/shop-cart'),
      buttonText: 'Shop Now',
      cardClass: styles.shopCard,
      buttonClass: styles.shopButton,
    },
  ];

  return (
    <div className={styles.homePage}>
      {/* Welcome section */}
      <div className={styles.welcomeSection}>
        <Title level={1} className={styles.welcomeTitle}>
          <HomeOutlined className={styles.welcomeIcon} />
          Welcome to our Basic Shop
        </Title>
      </div>

      {/* Feature cards */}
      <Row gutter={[24, 24]} className={styles.featureCardsRow}>
        {featureCards.map((card, index) => (
          <Col xs={24} sm={12} lg={8} key={index}>
            <Card
              hoverable
              className={`${styles.featureCard} ${card.cardClass}`}
              styles={{ body: { padding: '32px 24px' } }}
            >
              <div className={styles.iconContainer}>
                {card.icon}
              </div>
              <Title level={3} className={styles.featureTitle}>
                {card.title}
              </Title>
              
              <Button 
                type="primary" 
                size="large"
                onClick={card.action}
                className={`${styles.featureButton} ${card.buttonClass}`}
              >
                {card.buttonText}
              </Button>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default HomePage; 