package com.udaan.kam.kam_lead_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.exception.ContactNotFoundException;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.service.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;
    
    @Autowired
    private RestaurantRepository restaurantRepository;


    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        // Find the restaurant by ID
        Restaurant restaurant = restaurantRepository.findById(contact.getRestaurant().getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Set the found restaurant to the contact
        contact.setRestaurant(restaurant);

        // Save the contact
        Contact createdContact = contactService.createContact(contact);

        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // API to get all Contacts
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    // API to get a Contact by ID
    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable Integer id) {
        return contactService.getContactById(id).orElseThrow(() -> new ContactNotFoundException("Contact not found with ID: " + id));
    }

    // API to update a Contact by ID
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Integer id, @RequestBody Contact updatedContact) {
        return contactService.updateContact(id, updatedContact);
    }

    // API to delete a Contact by ID
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Integer id) {
        contactService.deleteContact(id);
    }
}
