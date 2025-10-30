package com.proj.webprojrct.review.controller;

import com.proj.webprojrct.product.entity.Product;
import com.proj.webprojrct.product.repository.ProductRepository;
import com.proj.webprojrct.review.entity.Review;
import com.proj.webprojrct.review.repository.ReviewReplyRepository;
import com.proj.webprojrct.review.repository.ReviewRepository;
import com.proj.webprojrct.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @PostMapping("/add/{productId}")
    public String addReview(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user,
            @RequestParam int rating,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false, name = "images") MultipartFile[] files
    ) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product"));

        List<String> imageUrls = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        byte[] bytes = file.getBytes();
                        String base64 = Base64.getEncoder().encodeToString(bytes);
                        String mimeType = file.getContentType();
                        String dataUri = "data:" + mimeType + ";base64," + base64;
                        imageUrls.add(dataUri);
                    } catch (IOException e) {
                    }
                }
            }
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setTitle(title);
        review.setComment(comment);
        review.setImages(imageUrls);
        review.setIsHidden(false);
        review.setLikeCount(0);
        review.setDislikeCount(0);
        reviewRepository.save(review);
        return "redirect:/orders/history/" + productId;
    }
}