package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.PaymentIntentRequestDto;
import com.eazybook.marcus.dto.PaymentIntentResponseDto;

public interface IPaymentService {
    PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto requestDto);
}
