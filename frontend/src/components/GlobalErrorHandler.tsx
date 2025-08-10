import React, { useEffect } from 'react';
import { Alert } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store';
import { clearError as clearProductsError } from '../store/slices/productsSlice';
import { clearError as clearCartError } from '../store/slices/cartSlice';
import styles from './GlobalErrorHandler.module.css';

/**
 * Global Error Handler Component
 * Used to display error messages from different modules
 */
const GlobalErrorHandler: React.FC = () => {
  const dispatch = useDispatch();
  const { error: productsError } = useSelector((state: RootState) => state.products);
  const { error: cartError } = useSelector((state: RootState) => state.cart);

  // Auto-clear error messages (after 5 seconds)
  useEffect(() => {
    if (productsError) {
      const timer = setTimeout(() => {
        dispatch(clearProductsError());
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [productsError, dispatch]);

  useEffect(() => {
    if (cartError) {
      const timer = setTimeout(() => {
        dispatch(clearCartError());
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [cartError, dispatch]);

  // If no errors, don't render anything
  if (!productsError && !cartError) {
    return null;
  }

  return (
    <div className={styles.errorContainer}>
      {productsError && (
        <Alert
          message="Product Operation Failed"
          description={productsError}
          type="error"
          showIcon
          closable
          style={{ marginBottom: '8px' }}
          onClose={() => dispatch(clearProductsError())}
        />
      )}
      {cartError && (
        <Alert
          message="Cart Operation Failed"
          description={cartError}
          type="error"
          showIcon
          closable
          style={{ marginBottom: '8px' }}
          onClose={() => dispatch(clearCartError())}
        />
      )}
    </div>
  );
};

export default GlobalErrorHandler;
