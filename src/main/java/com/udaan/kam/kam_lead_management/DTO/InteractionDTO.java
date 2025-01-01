package com.udaan.kam.kam_lead_management.DTO;

import java.time.LocalDateTime;

import com.udaan.kam.kam_lead_management.entity.Contact;

public class InteractionDTO {
    private Integer id;
    private String interactionType;
    private LocalDateTime interactionDate;
    private String notes;
    private LocalDateTime followUpDate;
    private LocalDateTime createdAt;
    private UserDTO user;
    private Contact contact;
    
    public InteractionDTO() {}

    public InteractionDTO(Integer id, String interactionType, LocalDateTime interactionDate, 
            String notes, LocalDateTime followUpDate, LocalDateTime createdAt, UserDTO user, Contact contact) {
        this.id = id;
        this.interactionType = interactionType;
        this.interactionDate = interactionDate;
        this.notes = notes;
        this.followUpDate = followUpDate;
        this.createdAt = createdAt;
        this.user = user;
        this.contact = contact;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getInteractionType() { return interactionType; }
    public void setInteractionType(String interactionType) { this.interactionType = interactionType; }
    
    public LocalDateTime getInteractionDate() { return interactionDate; }
    public void setInteractionDate(LocalDateTime interactionDate) { this.interactionDate = interactionDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDateTime followUpDate) { this.followUpDate = followUpDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    
    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }
}