package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.ProfileRequestDto;
import com.eazybook.marcus.dto.ProfileResponseDto;

public interface IProfileService {
    ProfileResponseDto getProfile();

    ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto);
}

