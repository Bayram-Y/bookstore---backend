package com.eazybook.marcus.service.impl;

import com.eazybook.marcus.constants.ApplicationConstants;
import com.eazybook.marcus.dto.ContactRequestDto;
import com.eazybook.marcus.dto.ContactResponseDto;
import com.eazybook.marcus.entity.Contact;
import com.eazybook.marcus.exception.ResourceNotFoundException;
import com.eazybook.marcus.repository.ContactRepository;
import com.eazybook.marcus.service.IContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        Contact contact = transformToEntity(contactRequestDto);
        contactRepository.save(contact);
        return true;

    }

    @Override
    public List<ContactResponseDto> getAllOpenMessages() {
        List<Contact> contacts = contactRepository.fetchByStatus(ApplicationConstants.OPEN_MESSAGE);
        return contacts.stream().map(this::mapToContactResponseDto).collect(Collectors.toList());
    }

    @Override
    public void updateMessageStatus(Long contactId, String status) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new ResourceNotFoundException("Contact", "ContactID", contactId.toString()));
        contact.setStatus(status);
        contactRepository.save(contact);
    }

    private ContactResponseDto mapToContactResponseDto(Contact contact) {
        ContactResponseDto responseDto = new ContactResponseDto(
                contact.getContactId(),
                contact.getName(),
                contact.getEmail(),
                contact.getMobileNumber(),
                contact.getMessage(),
                contact.getStatus()
        );
        return responseDto;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
        contact.setStatus(ApplicationConstants.OPEN_MESSAGE);
        return contact;
    }
}
