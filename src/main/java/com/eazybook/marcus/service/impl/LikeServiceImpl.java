package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.dto.LikeResponseDto;
import com.eazybook.marcus.entity.Customer;
import com.eazybook.marcus.entity.Like;
import com.eazybook.marcus.entity.Product;
import com.eazybook.marcus.repository.CustomerRepository;
import com.eazybook.marcus.repository.LikeRepository;
import com.eazybook.marcus.repository.ProductRepository;
import com.eazybook.marcus.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements ILikeService {

    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public LikeResponseDto toggleLike(Long productId) {

        Customer customer = getAuthenticatedCustomer();

        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean exists = likeRepository.existsByCustomerIdAndProductId(
                customer.getCustomerId(), productId
        );

        boolean isLiked;

        if (exists) {
            likeRepository.deleteByCustomerIdAndProductId(
                    customer.getCustomerId(), productId
            );
            isLiked = false;
        } else {
            Like like = new Like();
            like.setCustomerId(customer.getCustomerId());
            like.setProductId(productId);
            likeRepository.save(like);
            isLiked = true;
        }

        //  HAR DOIM DB dan hisoblaymiz
        int count = likeRepository.countByProductId(productId);

        return new LikeResponseDto(isLiked, count);
    }


    @Override
    public int getLikesCount(Long productId) {
        return likeRepository.countByProductId(productId);
    }

    @Override
    public boolean isLikedByCurrentUser(Long productId) {
        Customer customer = getAuthenticatedCustomer();
        return likeRepository.existsByCustomerIdAndProductId(
                customer.getCustomerId(), productId
        );
    }

    private Customer getAuthenticatedCustomer() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
