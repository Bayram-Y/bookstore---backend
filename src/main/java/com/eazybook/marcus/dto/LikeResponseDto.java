package com.eazybook.marcus.dto;

public record LikeResponseDto(
        boolean liked,
        int likesCount
) {}
