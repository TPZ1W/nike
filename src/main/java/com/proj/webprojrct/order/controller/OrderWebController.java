package com.proj.webprojrct.order.controller;

import com.proj.webprojrct.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderWebController {
    private final OrderService orderService;

    @GetMapping("checkout")
    public String checkoutPage() {
        return "checkout";
    }

    @GetMapping("checkout/success")
    public String checkoutSuccessPage() {
        return "checkout-success";
    }
}
