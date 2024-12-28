package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.ContactNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // Create a new Contact
    public Contact createContact(Contact contact) {
        if (contact.getFirstName() == null || contact.getFirstName().isEmpty()) {
            throw new BadRequestException("First name is required.");
        }
        if (contact.getLastName() == null || contact.getLastName().isEmpty()) {
            throw new BadRequestException("Last name is required.");
        }
        return contactRepository.save(contact);
    }

    // Get all Contacts
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Get Contact by ID
    public Optional<Contact> getContactById(Integer id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (!contact.isPresent()) {
            throw new ContactNotFoundException("Contact not found with ID: " + id);
        }
        return contact;
    }
    
    // Get all contacts for a specific Restaurant
    public List<Contact> getContactsByRestaurantId(Integer restaurantId) {
        return contactRepository.findByRestaurantId(restaurantId);
    }

    // Update Contact by ID
    public Contact updateContact(Integer id, Contact updatedContact) {
        return contactRepository.findById(id).map(contact -> {
            contact.setFirstName(updatedContact.getFirstName());
            contact.setLastName(updatedContact.getLastName());
            contact.setRole(updatedContact.getRole());
            contact.setEmail(updatedContact.getEmail());
            contact.setPhone(updatedContact.getPhone());
            contact.setIsPrimary(updatedContact.getIsPrimary());
            return contactRepository.save(contact);
        }).orElseThrow(() -> new ContactNotFoundException("Contact not found with ID: " + id));
    }

    // Delete Contact by ID
    public void deleteContact(Integer id) {
        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException("Contact not found with ID: " + id);
        }
        contactRepository.deleteById(id);
    }
}
