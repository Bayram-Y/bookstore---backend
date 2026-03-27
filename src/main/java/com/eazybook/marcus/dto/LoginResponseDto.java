package com.eazybook.marcus.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
