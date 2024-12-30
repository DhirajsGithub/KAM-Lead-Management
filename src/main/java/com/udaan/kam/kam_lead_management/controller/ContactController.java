package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.service.ContactService;
import com.udaan.kam.kam_lead_management.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Create a new Contact for a specific Restaurant
    @PostMapping("/{restaurantId}")
    public ResponseEntity<Contact> createContact(
            @PathVariable Integer restaurantId,
            @RequestBody Contact contact,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        Contact createdContact = contactService.createContact(contact, restaurantId, userId);
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // Get all Contacts for a specific Restaurant
    @GetMapping("/{restaurantId}")
    public List<Contact> getContactsByRestaurantId(
            @PathVariable Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        return contactService.getContactsByRestaurantId(restaurantId, userId);
    }

    // Update Contact by ID for a specific Restaurant
    @PutMapping("/{restaurantId}/{contactId}")
    public ResponseEntity<Contact> updateContact(
            @PathVariable Integer restaurantId,
            @PathVariable Integer contactId,
            @RequestBody Contact updatedContact,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        Contact updated = contactService.updateContact(contactId, updatedContact, restaurantId, userId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete Contact by ID for a specific Restaurant
    @DeleteMapping("/{restaurantId}/{contactId}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Integer restaurantId,
            @PathVariable Integer contactId,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        Integer userId = currentUser.getUserId(); // Get the logged-in user ID
        contactService.deleteContact(contactId, restaurantId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
