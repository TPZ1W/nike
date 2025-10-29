package com.proj.webprojrct.order.service;

import com.proj.webprojrct.cart.repository.CartRepository;
import com.proj.webprojrct.order.controller.OrderController;
import com.proj.webprojrct.order.entity.Order;
import com.proj.webprojrct.order.entity.OrderItem;
import com.proj.webprojrct.order.repository.OrderItemRepository;
import com.proj.webprojrct.order.repository.OrderRepository;
import com.proj.webprojrct.payment.PaymentService;
import com.proj.webprojrct.product.repository.ProductRepository;
import com.proj.webprojrct.promotion.entity.Coupon;
import com.proj.webprojrct.promotion.repository.CouponRepository;
import com.proj.webprojrct.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    public String placeOrder(User user, OrderController.OrderRequest request) {
        var cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng cho người dùng."));
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng.");
        }
        Coupon couponItem = null;
        double totalBeforeDiscount = cart.getTotalPrice();

        if (StringUtils.hasText(request.getCouponCode())) {
            couponItem = couponRepository.findByCode(request.getCouponCode()).orElseThrow(() -> new RuntimeException("Không tìm thấy mã giảm giá."));
            boolean expired = couponItem.getEndDate() != null && couponItem.getEndDate().isBefore(LocalDateTime.now());
            boolean notStarted = couponItem.getStartDate() != null && couponItem.getStartDate().isAfter(LocalDateTime.now());
            boolean exceededUsage = couponItem.getUsageLimit() != null && couponItem.getUsedCount() >= couponItem.getUsageLimit();
            boolean belowMin = totalBeforeDiscount < couponItem.getMinOrderAmount();
            boolean inactive = !Boolean.TRUE.equals(couponItem.getIsActive());
            if (expired || notStarted || exceededUsage || belowMin || inactive) {
                throw new RuntimeException("Mã giảm giá không hợp lệ hoặc đã hết hạn.");
            }
        }

        var order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setPhone(request.getPhone());
        order.setQuantity(cart.getTotalQuantity());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(request.getShippingAddress());
        order.setCoupon(couponItem);
        double totalAfterDiscount = totalBeforeDiscount;
        if (couponItem != null) {
            order.setCoupon(couponItem);
            totalAfterDiscount = totalBeforeDiscount - couponItem.calculateDiscount(totalBeforeDiscount);
            couponItem.setUsedCount(couponItem.getUsedCount() + 1);
            couponRepository.save(couponItem);
        }
        order.setTotalAmount(totalAfterDiscount);

        List<OrderItem> orderItems = new ArrayList<>();
        for (var item : cart.getItems()) {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductPrice(item.getProductPrice());
            orderItem.setSize(item.getSize());
            orderItems.add(orderItem);
        }

        cart.getItems().forEach(ct -> {
            productRepository.findById(ct.getProduct().getId()).ifPresent(pt -> {
                if (pt.getStock() < ct.getQuantity()) {
                    throw new RuntimeException("Sản phẩm " + pt.getName() + "(%d)".formatted(pt.getId()) + " không đủ số lượng tồn kho");
                }
                pt.setStock(pt.getStock() - ct.getQuantity());
                productRepository.save(pt);
            });
        });
        order.setItems(orderItems);
        var newOrder = orderRepository.save(order);
        cartRepository.delete(cart);

        if ("VNPAY".equalsIgnoreCase(request.getPaymentMethod())) {
            var vnPayOrder = paymentService.createPayment(
                    new PaymentService.VnPayBody(newOrder.getTotalAmount(), "Thanh toan"));
            newOrder.setTxnId(vnPayOrder.getTxnId());
            newOrder.setStatus("WAITING_FOR_PAYMENT");
            orderRepository.save(newOrder);
            return vnPayOrder.getPaymentUrl();
        }
        return null;
    }
}
