package com.eazybook.marcus.controller;

import com.eazybook.marcus.dto.ContactInfoDto;
import com.eazybook.marcus.dto.ContactRequestDto;
import com.eazybook.marcus.service.IContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService iContactService;

    private final ContactInfoDto contactInfoDto;

    @PostMapping()
    public ResponseEntity<String> saveContact(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        iContactService.saveContact(contactRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Request procced successfully");
    }

    @GetMapping()
    public ResponseEntity<ContactInfoDto> getContactInfo() {
        return ResponseEntity.ok(contactInfoDto);
    }
}
