package com.example.shop.service.impl;

import com.example.shop.dao.CartDao;
import com.example.shop.dao.ProductDao;
import com.example.shop.model.entity.Cart;
import com.example.shop.model.entity.CartItem;
import com.example.shop.model.entity.Product;
import com.example.shop.service.CartService;
import com.example.common.exception.BusinessException;
import com.example.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Shopping Cart Service Implementation Class
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Override
    public Cart getCart(Long userId) {
        Cart cart = cartDao.selectByUserId(userId);
        if (cart == null) {
            // If cart doesn't exist, create a new one
            cart = new Cart();
            cart.setUserId(userId);
            cartDao.insertCart(cart);
        }
        
        // Calculate total amount and product count
        calculateCartTotals(cart);
        return cart;
    }
    
    @Override
    @Transactional
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // Validate product and get product information
        Product product = validateAndGetProduct(productId);
        
        // Check if stock is sufficient
        validateStockAvailability(product, quantity);
        
        // Get or create cart
        Cart cart = getOrCreateCart(userId);
        
        // Check if product already exists in cart
        CartItem existingItem = cartDao.selectCartItemByCartIdAndProductId(cart.getId(), productId);
        
        if (existingItem != null) {
            updateExistingCartItem(existingItem, product, quantity);
        } else {
            createNewCartItem(cart, product, quantity);
        }
    }
    
    @Override
    @Transactional
    public void updateCartItem(Long userId, Long itemId, Integer quantity) {
        Cart cart = getCart(userId);
        CartItem cartItem = getAndValidateCartItem(itemId, cart.getId());
        Product product = getAndValidateProduct(cartItem.getProductId());
        
        int oldQuantity = cartItem.getQuantity();
        
        if (quantity <= 0) {
            // If quantity is 0 or negative, remove the item and restore stock
            restoreProductStock(product, oldQuantity);
            cartDao.deleteCartItemById(itemId);
        } else {
            // Check if stock is sufficient - available stock = current stock + quantity already in cart
            validateStockForUpdate(product, oldQuantity, quantity);
            
            // Update stock and cart item
            updateProductStock(product, oldQuantity, quantity);
            updateCartItemQuantity(cartItem, quantity);
        }
    }
    
    @Override
    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        Cart cart = getCart(userId);
        CartItem cartItem = getAndValidateCartItem(itemId, cart.getId());
        
        // 恢复库存
        restoreProductStock(cartItem.getProductId(), cartItem.getQuantity());
        
        cartDao.deleteCartItemById(itemId);
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        
        // Restore stock for all cart items (batch processing)
        List<CartItem> items = cartDao.selectCartItems(cart.getId());
        if (!items.isEmpty()) {
            batchRestoreProductStock(items);
        }
        
        cartDao.deleteCartItemsByCartId(cart.getId());
    }
    
    @Override
    public Integer getCartItemCount(Long userId) {
        Cart cart = getCart(userId);
        return cartDao.countCartItems(cart.getId());
    }
    
    @Override
    public BigDecimal getCartTotal(Long userId) {
        Cart cart = getCart(userId);
        return cartDao.sumCartTotal(cart.getId());
    }
    
    /**
     * Validate if product exists and is visible
     */
    private Product validateAndGetProduct(Long productId) {
        Product product = productDao.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found");
        }
        if (!product.getVisible()) {
            throw new BusinessException(ErrorCode.PRODUCT_INVISIBLE, "Product not visible");
        }
        return product;
    }
    
    /**
     * Validate if stock is sufficient
     */
    private void validateStockAvailability(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT);
        }
    }
    
    /**
     * Update existing cart item
     */
    private void updateExistingCartItem(CartItem existingItem, Product product, Integer quantity) {
        int oldQuantity = existingItem.getQuantity();
        int newTotalQuantity = oldQuantity + quantity;
        
        // Check if total quantity exceeds available stock (original stock + quantity already in cart)
        int availableStock = product.getQuantity() + oldQuantity;
        if (newTotalQuantity > availableStock) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT);
        }
        
        // Deduct new stock
        product.setQuantity(product.getQuantity() - quantity);
        productDao.update(product);
        
        // Update cart item
        existingItem.setQuantity(newTotalQuantity);
        existingItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(newTotalQuantity)));
        cartDao.updateCartItem(existingItem);
    }
    
    /**
     * Create new cart item
     */
    private void createNewCartItem(Cart cart, Product product, Integer quantity) {
        // Deduct stock
        product.setQuantity(product.getQuantity() - quantity);
        productDao.update(product);
        
        // Create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setProductId(product.getId());
        cartItem.setProductName(product.getName());
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
        cartDao.insertCartItem(cartItem);
    }
    
    /**
     * Get and validate cart item
     */
    private CartItem getAndValidateCartItem(Long itemId, Long cartId) {
        CartItem cartItem = cartDao.selectCartItemById(itemId);
        if (cartItem == null || !cartItem.getCartId().equals(cartId)) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return cartItem;
    }
    
    /**
     * Get and validate product
     */
    private Product getAndValidateProduct(Long productId) {
        Product product = productDao.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return product;
    }
    
    /**
     * Validate if stock is sufficient for update
     */
    private void validateStockForUpdate(Product product, int oldQuantity, int newQuantity) {
        int availableStock = product.getQuantity() + oldQuantity;
        if (newQuantity > availableStock) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT);
        }
    }
    
    /**
     * Update product stock
     */
    private void updateProductStock(Product product, int oldQuantity, int newQuantity) {
        int stockChange = newQuantity - oldQuantity;
        int newProductQuantity = product.getQuantity() - stockChange;
        product.setQuantity(newProductQuantity);
        productDao.update(product);
    }
    
    /**
     * Update cart item quantity
     */
    private void updateCartItemQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(quantity)));
        cartDao.updateCartItem(cartItem);
    }
    
    /**
     * Restore product stock (by product ID)
     */
    private void restoreProductStock(Long productId, int quantity) {
        Product product = productDao.selectById(productId);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            productDao.update(product);
        } else {
            log.warn("Cannot restore stock, product not found - Product ID: {}", productId);
        }
    }
    
    /**
     * Restore product stock (by product object)
     */
    private void restoreProductStock(Product product, int quantity) {
        product.setQuantity(product.getQuantity() + quantity);
        productDao.update(product);
    }
    
    /**
     * Batch restore product stock
     */
    private void batchRestoreProductStock(List<CartItem> items) {
        // Group by product ID, calculate total quantity to restore for each product
        Map<Long, Integer> productRestoreMap = items.stream()
            .collect(Collectors.groupingBy(
                CartItem::getProductId,
                Collectors.summingInt(CartItem::getQuantity)
            ));
        
        // Batch query all related products
        List<Long> productIds = new ArrayList<>(productRestoreMap.keySet());
        List<Product> products = productDao.selectByIds(productIds);
        
        // Batch update product stock
        List<Product> productsToUpdate = new ArrayList<>();
        for (Product product : products) {
            Integer restoreQuantity = productRestoreMap.get(product.getId());
            if (restoreQuantity != null) {
                product.setQuantity(product.getQuantity() + restoreQuantity);
                productsToUpdate.add(product);
            }
        }
        
        // Batch update to database
        if (!productsToUpdate.isEmpty()) {
            productDao.batchUpdate(productsToUpdate);
        }

    }
    
    /**
     * Calculate cart total amount and product count
     */
    private void calculateCartTotals(Cart cart) {
        if (cart != null && cart.getId() != null) {
            cart.setTotalAmount(cartDao.sumCartTotal(cart.getId()));
            cart.setItemCount(cartDao.countCartItems(cart.getId()));
        }
    }
    
    /**
     * Get or create cart
     */
    private Cart getOrCreateCart(Long userId) {
        Cart cart = cartDao.selectByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartDao.insertCart(cart);
        }
        return cart;
    }
} 