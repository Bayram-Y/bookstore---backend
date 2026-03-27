package com.eazybook.marcus.controller;

import com.eazybook.marcus.dto.ProfileRequestDto;
import com.eazybook.marcus.dto.ProfileResponseDto;
import com.eazybook.marcus.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final IProfileService iProfileService;


    @GetMapping()
    public ResponseEntity<ProfileResponseDto> getProfile() {
        ProfileResponseDto responseDto = iProfileService.getProfile();
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping()
    public ResponseEntity<ProfileResponseDto> updateProfile(@Validated @RequestBody ProfileRequestDto profileRequestDto) {
        ProfileResponseDto responseDto = iProfileService.updateProfile(profileRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
