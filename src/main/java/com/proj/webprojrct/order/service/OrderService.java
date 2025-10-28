package com.proj.webprojrct.order.service;

import com.proj.webprojrct.cart.repository.CartRepository;
import com.proj.webprojrct.order.controller.OrderController;
import com.proj.webprojrct.order.entity.Order;
import com.proj.webprojrct.order.entity.OrderItem;
import com.proj.webprojrct.order.repository.OrderItemRepository;
import com.proj.webprojrct.order.repository.OrderRepository;
import com.proj.webprojrct.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;

    public void placeOrder(User user, OrderController.OrderRequest request) {
        // 1️⃣ Tìm giỏ hàng của user
        var cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng cho người dùng."));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng.");
        }

        // 2️⃣ Tạo đơn hàng mới
        var order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setPhone(request.getPhone());
        order.setQuantity(cart.getTotalQuantity());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(request.getShippingAddress());
        order.setTotalAmount(cart.getTotalPrice());

        // 3️⃣ Map từ CartItem sang OrderItem
        List<OrderItem> orderItems = cart.getItems().stream().map(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductPrice(item.getProductPrice());
            return orderItem;
        }).toList();

        order.setItems(orderItems);

        // 4️⃣ Lưu đơn hàng
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // 5️⃣ Xóa giỏ hàng sau khi đặt đơn
        cartRepository.delete(cart);
    }
}
