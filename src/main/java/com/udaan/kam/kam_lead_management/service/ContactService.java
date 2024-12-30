package com.udaan.kam.kam_lead_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Restaurant;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.exception.BadRequestException;
import com.udaan.kam.kam_lead_management.exception.ContactNotFoundException;
import com.udaan.kam.kam_lead_management.repository.ContactRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantRepository;
import com.udaan.kam.kam_lead_management.repository.RestaurantUserRepository;
import com.udaan.kam.kam_lead_management.repository.UserRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestaurantUserRepository restaurantUserRepository;

    @Autowired
    private RestaurantUserService restaurantUserService;


    // Create a new Contact
    public Contact createContact(Contact contact, Integer restaurantId, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to add a contact for this restaurant.");
        }

        // Ensure the restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));

        contact.setRestaurant(restaurant);
        return contactRepository.save(contact);
    }
    
    // Get all Contacts for a specific Restaurant
    public List<Contact> getContactsByRestaurantId(Integer restaurantId, Integer userId) {
        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to view contacts for this restaurant.");
        }

        return contactRepository.findByRestaurantId(restaurantId);
    }


    // Update Contact by ID for a specific Restaurant
    public Contact updateContact(Integer contactId, Contact updatedContact, Integer restaurantId, Integer userId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with ID: " + contactId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to update this contact.");
        }

        contact.setFirstName(updatedContact.getFirstName());
        contact.setLastName(updatedContact.getLastName());
        contact.setEmail(updatedContact.getEmail());
        contact.setPhone(updatedContact.getPhone());
        contact.setRole(updatedContact.getRole());
        contact.setIsPrimary(updatedContact.getIsPrimary());
        return contactRepository.save(contact);
    }
    
    // Delete Contact by ID for a specific Restaurant
    public void deleteContact(Integer contactId, Integer restaurantId, Integer userId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with ID: " + contactId));

        if (!isAdminOrAssignedManager(userId, restaurantId)) {
            throw new BadRequestException("You are not authorized to delete this contact.");
        }

        contactRepository.deleteById(contactId);
    }
    

    
    private boolean isAdminOrAssignedManager(Integer userId, Integer restaurantId) {
        // Check if the user is either an Admin or assigned Manager for this restaurant
        return isAdmin(userId) || restaurantUserService.isRestaurantAssignedToUser(restaurantId, userId);
    }
    
    private boolean isAdmin(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole().toString().equals("ADMIN");
    }
}
