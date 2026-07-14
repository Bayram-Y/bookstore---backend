package com.eazybook.marcus.service;

import com.eazybook.marcus.dto.LikeResponseDto;

public interface ILikeService {

    LikeResponseDto toggleLike(Long productId);

    int getLikesCount(Long productId);

    boolean isLikedByCurrentUser(Long productId);
}
