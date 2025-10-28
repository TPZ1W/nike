package com.proj.webprojrct.cart.service;

import com.proj.webprojrct.cart.dto.CartResponse;
import com.proj.webprojrct.cart.entity.Cart;
import com.proj.webprojrct.cart.entity.CartItem;
import com.proj.webprojrct.cart.repository.CartItemRepository;
import com.proj.webprojrct.cart.repository.CartRepository;
import com.proj.webprojrct.product.repository.ProductRepository;
import com.proj.webprojrct.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartResponse getCartByUser(User user) {
        var cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    var newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return new CartResponse(cart);
    }

    public void addToCart(User user, Long productId, Integer quantity, String size) {
        var product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        cartRepository.findByUserId(user.getId())
                .ifPresentOrElse(cart -> {
                    cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                            .ifPresentOrElse(cartItem -> {
                                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                                cartItemRepository.save(cartItem);
                            }, () -> {
                                var newCartItem = new CartItem();
                                newCartItem.setCart(cart);
                                newCartItem.setProduct(product);
                                newCartItem.setQuantity(quantity);
                                newCartItem.setProductPrice(product.getPrice());
                                newCartItem.setTotalPrice(product.getPrice() * quantity);
                                newCartItem.setSize(size);
                                cartItemRepository.save(newCartItem);
                            });
                }, () -> {
                    var newCart = cartRepository.save(new com.proj.webprojrct.cart.entity.Cart());
                    var newCartItem = new CartItem();
                    newCart.setUser(user);
                    newCartItem.setCart(newCart);
                    newCartItem.setProduct(product);
                    newCartItem.setQuantity(quantity);
                    newCartItem.setProductPrice(product.getPrice());
                    newCartItem.setTotalPrice(product.getPrice() * quantity);
                    newCartItem.setSize(size);
                    newCart.getItems().add(newCartItem);
                    cartRepository.save(newCart);
                });
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateQuantity(User user, Long productId, Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        var cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng của người dùng"));

        var cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));

        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(cartItem.getProductPrice() * newQuantity);
        cartItemRepository.save(cartItem);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeItem(User user, Long productId) {
        var cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng của người dùng"));

        var cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));

        cartItemRepository.delete(cartItem);

        if (cart.getItems().isEmpty()) {
            cart.setTotalPrice(0.0);
            cart.setTotalQuantity(0);
            cartRepository.save(cart);
        }
    }


}
