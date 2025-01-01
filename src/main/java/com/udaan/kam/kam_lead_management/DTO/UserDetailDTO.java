package com.udaan.kam.kam_lead_management.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.PerformanceMetric;
import com.udaan.kam.kam_lead_management.entity.RestaurantUser;

public class UserDetailDTO {
	private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private List<RestaurantDTO> assignRestaurants;	

	private List<InteractionDTO> interactions;
      
    
	public UserDetailDTO(Integer id, String username, String email, String firstName, String lastName, String role,
			LocalDateTime createdAt, Boolean isActive, List<RestaurantDTO> assignRestaurants,
			List<InteractionDTO> interactions) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.createdAt = createdAt;
		this.isActive = isActive;
		this.assignRestaurants = assignRestaurants;
		this.interactions = interactions;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<InteractionDTO> getInteractions() {
		return interactions;
	}
	public void setInteractions(List<InteractionDTO> interactions) {
		this.interactions = interactions;
	}
	
    public List<RestaurantDTO> getAssignRestaurants() {
		return assignRestaurants;
	}
	public void setAssignRestaurants(List<RestaurantDTO> assignRestaurants) {
		this.assignRestaurants = assignRestaurants;
	}
    
    
}
