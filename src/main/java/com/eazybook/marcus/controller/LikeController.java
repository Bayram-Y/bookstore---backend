package com.eazybook.marcus.controller;

import com.eazybook.marcus.dto.LikeResponseDto;
import com.eazybook.marcus.service.ILikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final ILikeService likeService;

    @PostMapping("/toggle")
    public ResponseEntity<LikeResponseDto> toggleLike(@RequestParam Long productId) {
        return ResponseEntity.ok(likeService.toggleLike(productId));
    }

    @GetMapping("/count/{productId}")
    public ResponseEntity<Integer> getLikesCount(@PathVariable Long productId) {
        return ResponseEntity.ok(likeService.getLikesCount(productId));
    }

    @GetMapping("/status/{productId}")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long productId) {
        return ResponseEntity.ok(likeService.isLikedByCurrentUser(productId));
    }
}