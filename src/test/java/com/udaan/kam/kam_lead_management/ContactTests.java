package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.ContactNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.service.ContactService;
import com.udaan.kam.kam_lead_management.util.PermissionUtils;

@ExtendWith(MockitoExtension.class)
class ContactTests {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private PermissionUtils permissionUtils;

    @InjectMocks
    private ContactService contactService;

    @Test
    void testCreateContact_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        Mockito.when(contactRepository.save(contact)).thenReturn(contact);

        Contact createdContact = contactService.createContact(contact, restaurantId, userId);

        assertEquals(contact, createdContact);
        Mockito.verify(contactRepository).save(contact);
    }

    @Test
    void testCreateContact_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;
        Contact contact = new Contact();

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                contactService.createContact(contact, restaurantId, userId));

        Mockito.verify(contactRepository, Mockito.never()).save(contact);
    }

    @Test
    void testGetContactsByRestaurantId_Success() {
        Integer restaurantId = 1;
        Integer userId = 101;
        List<Contact> contacts = List.of(new Contact(), new Contact());

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(contactRepository.findByRestaurantId(restaurantId)).thenReturn(contacts);

        List<Contact> result = contactService.getContactsByRestaurantId(restaurantId, userId);

        assertEquals(contacts, result);
        Mockito.verify(contactRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void testGetContactsByRestaurantId_Unauthorized() {
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                contactService.getContactsByRestaurantId(restaurantId, userId));

        Mockito.verify(contactRepository, Mockito.never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testUpdateContact_Success() {
        Integer contactId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Contact existingContact = new Contact();
        existingContact.setId(contactId);

        Contact updatedContact = new Contact();
        updatedContact.setFirstName("Jane");
        updatedContact.setLastName("Smith");

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);
        Mockito.when(contactRepository.save(existingContact)).thenReturn(existingContact);

        Contact result = contactService.updateContact(contactId, updatedContact, restaurantId, userId);

        assertEquals(existingContact, result);
        Mockito.verify(contactRepository).save(existingContact);
    }

    @Test
    void testUpdateContact_Unauthorized() {
        Integer contactId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;
        Contact updatedContact = new Contact();

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(new Contact()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                contactService.updateContact(contactId, updatedContact, restaurantId, userId));

        Mockito.verify(contactRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testDeleteContact_Success() {
        Integer contactId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Contact contact = new Contact();
        contact.setId(contactId);

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(true);

        contactService.deleteContact(contactId, restaurantId, userId);

        Mockito.verify(contactRepository).deleteById(contactId);
    }

    @Test
    void testDeleteContact_Unauthorized() {
        Integer contactId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.of(new Contact()));
        Mockito.when(permissionUtils.isAdminOrAssignedManager(userId, restaurantId)).thenReturn(false);

        assertThrows(BadRequestException.class, () ->
                contactService.deleteContact(contactId, restaurantId, userId));

        Mockito.verify(contactRepository, Mockito.never()).deleteById(contactId);
    }

    @Test
    void testDeleteContact_NotFound() {
        Integer contactId = 1;
        Integer restaurantId = 1;
        Integer userId = 101;

        Mockito.when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () ->
                contactService.deleteContact(contactId, restaurantId, userId));

        Mockito.verify(contactRepository, Mockito.never()).deleteById(contactId);
    }
}
