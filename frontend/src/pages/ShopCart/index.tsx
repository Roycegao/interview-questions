import React, { useEffect, useState } from 'react';
import { 
  Row, 
  Col, 
  Card, 
  Table, 
  Button, 
  Input, 
  InputNumber, 
  Space, 
  Typography, 
  message,
  Popconfirm,
  Empty,
  Divider
} from 'antd';
import { 
  ShoppingCartOutlined, 
  PlusOutlined, 
  MinusOutlined, 
  DeleteOutlined,
  SearchOutlined
} from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { fetchVisibleProducts, fetchVisibleProductsWithSearch } from '../../store/slices/productsSlice';
import { fetchCart, addToCart, updateCartItem, removeFromCart, clearCart } from '../../store/slices/cartSlice';
import { Product, CartItem } from '../../types';
import styles from './index.module.css';

const { Text } = Typography;
const { Search } = Input;

const ShopCartPage: React.FC = () => {
  const dispatch = useDispatch();
  const { products, loading: productsLoading, pagination } = useSelector((state: RootState) => state.products);
  const { cart, loading: cartLoading } = useSelector((state: RootState) => state.cart);
  
  const [searchText, setSearchText] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [cartCurrentPage, setCartCurrentPage] = useState(1);
  const [cartPageSize, setCartPageSize] = useState(5);

  // Fetch visible products and cart - only once on component mount
  useEffect(() => {
    dispatch(fetchVisibleProductsWithSearch({ 
      name: undefined, 
      pageNum: 1, 
      pageSize: pageSize 
    }) as any);
    dispatch(fetchCart() as any);
  }, [dispatch, pageSize]);

  // Handle search
  const handleSearch = async (value: string) => {
    setSearchText(value);
    setCurrentPage(1);
    
    try {
      
      dispatch(fetchVisibleProductsWithSearch({ 
        name: value.trim() || undefined, 
        pageNum: 1, 
        pageSize: pageSize 
      }) as any);
    } catch (error) {
      console.error('Search failed:', error);
      message.error('Search failed, please try again');
    }
  };

  // Handle pagination change
  const handlePaginationChange = (page: number, size?: number) => {
    setCurrentPage(page);
    if (size && size !== pageSize) {
      setPageSize(size);
    }
    
    // Refetch data with new pagination
    try {
      // Always use fetchVisibleProductsWithSearch to support pagination
      dispatch(fetchVisibleProductsWithSearch({ 
        name: searchText.trim() || undefined, 
        pageNum: page, 
        pageSize: size || pageSize 
      }) as any);
    } catch (error) {
      console.error('Pagination failed:', error);
      message.error('Failed to load page, please try again');
    }
  };

  // Handle cart pagination change
  const handleCartPaginationChange = (page: number, size?: number) => {
    setCartCurrentPage(page);
    if (size && size !== cartPageSize) {
      setCartPageSize(size);
    }
  };

  // Get paginated cart items
  const getPaginatedCartItems = () => {
    if (!cart?.items) return [];
    
    const startIndex = (cartCurrentPage - 1) * cartPageSize;
    const endIndex = startIndex + cartPageSize;
    return cart.items.slice(startIndex, endIndex);
  };

  // Add to cart
  const handleAddToCart = async (product: Product) => {
    try {
      const resultAction = await dispatch(addToCart({ productId: product.id, quantity: 1 }) as any);
      if (addToCart.fulfilled.match(resultAction)) {
        // Refetch both cart data and products data to get updated stock information
        dispatch(fetchCart() as any);
        dispatch(fetchVisibleProducts() as any);
        // Reset cart pagination to first page when adding new item
        setCartCurrentPage(1);
        message.success('Added to cart');
      }
      // Error handled by global interceptor, no additional handling needed here
    } catch (error: any) {
      // Global interceptor already shows error message, no need to repeat here
      console.error('Failed to add to cart:', error);
    }
  };

  // Update cart item quantity
  const handleUpdateQuantity = async (itemId: number, quantity: number) => {
    if (quantity <= 0) {
      await handleRemoveFromCart(itemId);
      return;
    }
    
    // Validate quantity doesn't exceed available stock
    const cartItem = cart?.items?.find(item => item.id === itemId);
    if (cartItem) {
      const product = products?.find(p => p.id === cartItem.productId);
      const availableStock = (product?.quantity || 0) + cartItem.quantity;
      
      if (quantity > availableStock) {
        message.error(`Quantity cannot exceed available stock: ${availableStock}`);
        return;
      }
    }
    
    try {
      const resultAction = await dispatch(updateCartItem({ itemId, quantity }) as any);
      if (updateCartItem.fulfilled.match(resultAction)) {
        // Refetch both cart data and products data to get updated stock information
        dispatch(fetchCart() as any);
        dispatch(fetchVisibleProducts() as any);
        // Reset cart pagination to first page when updating quantity
        setCartCurrentPage(1);
        message.success('Quantity updated successfully');
      }
      // Error handled by global interceptor
    } catch (error: any) {
      // Global interceptor already shows error message
      console.error('Failed to update quantity:', error);
    }
  };

  // Remove from cart
  const handleRemoveFromCart = async (itemId: number) => {
    try {
      const resultAction = await dispatch(removeFromCart(itemId) as any);
      if (removeFromCart.fulfilled.match(resultAction)) {
        // Refetch both cart data and products data to get updated stock information
        dispatch(fetchCart() as any);
        dispatch(fetchVisibleProducts() as any);
        // Reset cart pagination to first page when removing item
        setCartCurrentPage(1);
        message.success('Removed from cart');
      }
      // Error handled by global interceptor
    } catch (error: any) {
      // Global interceptor already shows error message
      console.error('Failed to remove item:', error);
    }
  };

  // Clear cart
  const handleClearCart = async () => {
    try {
      const resultAction = await dispatch(clearCart() as any);
      if (clearCart.fulfilled.match(resultAction)) {
        // Refetch both cart data and products data to get updated stock information
        dispatch(fetchCart() as any);
        dispatch(fetchVisibleProducts() as any);
        // Reset cart pagination to first page when clearing cart
        setCartCurrentPage(1);
        message.success('Cart cleared');
      }
      // Error handled by global interceptor
    } catch (error: any) {
      // Global interceptor already shows error message
      console.error('Failed to clear cart:', error);
    }
  };

  // Product table columns configuration
  const productColumns = [
    {
      title: 'Product Name',
      dataIndex: 'name',
      key: 'name',
      width: '30%',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      width: '20%',
      render: (price: number) => `¥${price.toFixed(2)}`,
    },
    {
      title: 'Stock',
      dataIndex: 'quantity',
      key: 'quantity',
      width: '20%',
      render: (quantity: number) => {
        return (
          <span className={`${styles.stockStatus} ${quantity <= 0 ? styles.stockStatusOutOfStock : ''}`}>
            {quantity > 0 ? quantity : 'Out of Stock'}
          </span>
        );
      },
    },
    {
      title: 'Actions',
      key: 'action',
      width: '30%',
      render: (_: unknown, record: Product) => {
        const isOutOfStock = record.quantity <= 0;
        
        return (
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => handleAddToCart(record)}
            size="small"
            disabled={isOutOfStock}
            title={
              isOutOfStock 
                ? 'Insufficient stock' 
                : 'Add to Cart'
            }
          >
            {isOutOfStock ? 'Out of Stock' : 'Add to Cart'}
          </Button>
        );
      },
    },
  ];

  // Cart table columns configuration
  const cartColumns = [
    {
      title: 'Item',
      dataIndex: 'productName',
      key: 'productName',
      width: '35%',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      width: '20%',
      render: (price: number) => `¥${price.toFixed(2)}`,
    },
    {
      title: 'Quantity',
      key: 'quantity',
      width: '25%',
      render: (_: unknown, record: CartItem) => {
        const product = products?.find(p => p.id === record.productId);
        // Available stock = current stock + quantity already in cart
        const availableStock = (product?.quantity || 0) + record.quantity;
        
        return (
          <Space>
            <Button
              size="small"
              icon={<MinusOutlined />}
              onClick={() => handleUpdateQuantity(record.id, record.quantity - 1)}
              disabled={record.quantity <= 1}
            />
            <InputNumber
              size="small"
              value={record.quantity}
              min={1}
              max={availableStock}
              onChange={(value) => handleUpdateQuantity(record.id, value || 1)}
              className={styles.quantityInput}
              title={`Max quantity: ${availableStock}`}
            />
            <Button
              size="small"
              icon={<PlusOutlined />}
              onClick={() => handleUpdateQuantity(record.id, record.quantity + 1)}
              disabled={record.quantity >= availableStock}
              title={record.quantity >= availableStock ? `Reached max available quantity: ${availableStock}` : 'Increase quantity'}
            />
          </Space>
        );
      },
    },
    {
      title: 'Actions',
      key: 'action',
      width: '20%',
      render: (_: unknown, record: CartItem) => (
        <Button
          type="link"
          danger
          icon={<DeleteOutlined />}
          onClick={() => handleRemoveFromCart(record.id)}
          size="small"
        >
          Delete
        </Button>
      ),
    },
  ];

  return (
    <div className={styles.shopCartPage}>
      <Row gutter={[24, 24]}>
        {/* Product Display Area */}
        <Col xs={24} lg={12}>
          <Card
            title={
              <Space>
                <ShoppingCartOutlined />
                <span>Products</span>
              </Space>
            }
            extra={
              <Search
                placeholder="Search products"
                allowClear
                enterButton={<SearchOutlined />}
                onSearch={handleSearch}
                                  onChange={(e) => {
                    // Update search text when user manually types or clears search box
                    const value = e.target.value;
                    setSearchText(value);
                  }}
                className={styles.searchInput}
              />
            }
          >
                        <Table
              columns={productColumns}
              dataSource={products}
              rowKey="id"
              loading={productsLoading}
              pagination={{
                current: currentPage,
                pageSize: pageSize,
                total: pagination?.total || products.length,
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: (total, range) => `Page ${range[0]}-${range[1]} of ${total}`,
                pageSizeOptions: ['5', '10', '20', '50'],
                onChange: handlePaginationChange,
                onShowSizeChange: handlePaginationChange,
              }}
              size="small"
              locale={{
                emptyText: <Empty description="No products available" />
              }}
            />
          </Card>
        </Col>

        {/* Cart Area */}
        <Col xs={24} lg={12}>
          <Card
            title={
              <Space>
                <ShoppingCartOutlined />
                <span>Cart</span>
                {cart && cart.itemCount > 0 && (
                  <Text type="secondary">({cart.itemCount} items)</Text>
                )}
              </Space>
            }
            extra={
              cart && cart.itemCount > 0 && (
                <Popconfirm
                  title="Are you sure you want to clear the cart?"
                  onConfirm={handleClearCart}
                  okText="Yes"
                  cancelText="No"
                >
                  <Button type="link" danger size="small">
                    Clear Cart
                  </Button>
                </Popconfirm>
              )
            }
          >
            {cart && cart.items && cart.items.length > 0 ? (
              <>
                <Table
                  columns={cartColumns}
                  dataSource={getPaginatedCartItems()}
                  rowKey="id"
                  loading={cartLoading}
                  pagination={{
                    current: cartCurrentPage,
                    pageSize: cartPageSize,
                    total: cart.items.length,
                    showSizeChanger: true,
                    showQuickJumper: true,
                    showTotal: (total, range) => `Page ${range[0]}-${range[1]} of ${total}`,
                    pageSizeOptions: ['3', '5', '10', '20'],
                    onChange: handleCartPaginationChange,
                    onShowSizeChange: handleCartPaginationChange,
                    size: 'small',
                  }}
                  size="small"
                />
                <Divider />
                <Row justify="space-between" align="middle">
                  <Col>
                    <Text strong>Total:</Text>
                  </Col>
                  <Col>
                    <Text strong className={styles.totalAmount}>
                      ¥{cart.totalAmount.toFixed(2)}
                    </Text>
                  </Col>
                </Row>
              </>
            ) : (
              <Empty description="Cart is empty" />
            )}
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default ShopCartPage; 