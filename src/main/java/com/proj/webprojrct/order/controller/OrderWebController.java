package com.proj.webprojrct.order.controller;

import com.proj.webprojrct.order.repository.OrderRepository;
import com.proj.webprojrct.order.service.OrderService;
import com.proj.webprojrct.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderWebController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("history")
    public String orderHistoryPage(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("orders", orderRepository.findByUserId(user.getId()));
        return "history-order";
    }

    @GetMapping("/history/{id}")
    public String orderDetail(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              Model model) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy đơn hàng"));
        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders/history";
        }
        model.addAttribute("order", order);
        return "order-detail";
    }

    @GetMapping("checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("checkout/success")
    public String checkoutSuccessPage() {
        return "checkout-success";
    }
}
