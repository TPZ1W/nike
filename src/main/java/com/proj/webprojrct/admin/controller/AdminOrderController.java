package com.proj.webprojrct.admin.controller;

import com.proj.webprojrct.admin.repository.AdminOrderRepository;
import com.proj.webprojrct.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller quản lý đơn hàng trong admin dashboard
 */
@RestController
@RequestMapping("/admin/api/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderRepository adminOrderRepository;

    // Helper methods để giảm code trùng lặp
    private <T> ResponseEntity<T> ok(T data) {
        return ResponseEntity.ok(data);
    }

    private ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Lấy danh sách tất cả đơn hàng (có phân trang)
     */
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = adminOrderRepository.findAll(pageable);
        return ok(orderPage);
    }

    /**
     * Lấy thông tin chi tiết một đơn hàng
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return adminOrderRepository.findById(id)
                .map(this::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        return adminOrderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = adminOrderRepository.save(order);
                    return ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy đơn hàng theo trạng thái
     */
    @GetMapping("/by-status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        // Tạm thời return empty list, cần implement method trong repository
        List<Order> orders = java.util.Collections.emptyList(); // adminOrderRepository.findByStatus(status);
        return ok(orders);
    }

    /**
     * Thống kê tổng số đơn hàng
     */
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalOrders() {
        Long totalOrders = adminOrderRepository.count();
        return ok(totalOrders);
    }

    /**
     * Thống kê đơn hàng theo trạng thái
     */
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getOrderStatsByStatus() {
        List<Object[]> stats = adminOrderRepository.countOrdersByStatus();
        return ok(stats);
    }

    /**
     * Thống kê doanh thu theo ngày
     */
    @GetMapping("/stats/daily-revenue")
    public ResponseEntity<List<Object[]>> getDailyRevenue(
            @RequestParam(required = false) Integer days) {
        
        // Tạm thời return empty list, cần implement method trong repository
        List<Object[]> revenue = java.util.Collections.emptyList(); // adminOrderRepository.getDailyRevenue(days != null ? days : 30);
        return ok(revenue);
    }

    /**
     * Thống kê tổng doanh thu
     */
    @GetMapping("/stats/total-revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        // Fix type conversion từ Double sang BigDecimal
        Double totalRevenueDouble = adminOrderRepository.calculateTotalRevenue();
        BigDecimal totalRevenue = totalRevenueDouble != null ? 
            BigDecimal.valueOf(totalRevenueDouble) : BigDecimal.ZERO;
        return ok(totalRevenue);
    }

    /**
     * Lấy đơn hàng trong khoảng thời gian
     */
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        // Tạm thời return empty list, cần implement method trong repository
        List<Order> orders = java.util.Collections.emptyList(); // adminOrderRepository.findOrdersInDateRange(startDate, endDate);
        return ok(orders);
    }

    /**
     * Xóa đơn hàng (chỉ dành cho admin cấp cao)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return adminOrderRepository.findById(id)
                .map(order -> {
                    adminOrderRepository.delete(order);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(notFound());
    }
}