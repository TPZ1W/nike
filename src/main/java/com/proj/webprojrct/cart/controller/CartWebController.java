package com.proj.webprojrct.cart.controller;

import com.proj.webprojrct.cart.dto.CartDTO;
import com.proj.webprojrct.cart.service.CartService;
import com.proj.webprojrct.user.entity.User;
import com.proj.webprojrct.user.repository.UserRepository;
import com.proj.webprojrct.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartWebController {
    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping
    public Object getCartByUser(Model model) {
        log.debug("🛒 [CART] Accessing cart page");
        
        // Try to get the current authenticated user
        Optional<String> currentUserLogin = SecurityUtil.getCurrentUserLogin();
        
        if (currentUserLogin.isEmpty()) {
            log.info("🔒 [CART] User not authenticated, redirecting to login");
            return "redirect:/login?error=authentication_required";
        }
        
        String email = currentUserLogin.get();
        log.debug("👤 [CART] Current user: {}", email);
        
        // Find the user entity by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.error("❌ [CART] User not found in database: {}", email);
            return "redirect:/login?error=user_not_found";
        }
        
        User user = userOpt.get();
        log.debug("✅ [CART] User found: {} (ID: {})", user.getEmail(), user.getId());
        
        try {
            var cart = cartService.getCartByUser(user);
            model.addAttribute("cart", cart);
            log.debug("🛒 [CART] Cart loaded successfully for user: {}", user.getEmail());
            return "cart";
        } catch (Exception e) {
            log.error("💥 [CART] Error loading cart for user: {}", user.getEmail(), e);
            model.addAttribute("error", "Không thể tải giỏ hàng. Vui lòng thử lại.");
            return "error";
        }
    }
}
