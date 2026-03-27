package com.eazybook.marcus.service;


import com.eazybook.marcus.dto.ContactRequestDto;
import com.eazybook.marcus.dto.ContactResponseDto;

import java.util.List;

public interface IContactService {
    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> getAllOpenMessages();

    void updateMessageStatus(Long contactId, String status);
}
