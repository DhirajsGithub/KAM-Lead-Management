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
    private List<RestaurantUser> restaurantUsers;		// will update it later base on restuarantUser DTO
    private List<Interaction> interactions;
    private List<PerformanceMetric> performanceMetrices;
      
    
	public UserDetailDTO(Integer id, String username, String email, String firstName, String lastName, String role,
			LocalDateTime createdAt, Boolean isActive, List<RestaurantUser> restaurantUsers,
			List<Interaction> interactions, List<PerformanceMetric> performanceMetrices) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.createdAt = createdAt;
		this.isActive = isActive;
		this.restaurantUsers = restaurantUsers;
		this.interactions = interactions;
		this.performanceMetrices = performanceMetrices;
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
	public List<RestaurantUser> getRestaurantUsers() {
		return restaurantUsers;
	}
	public void setRestaurantUsers(List<RestaurantUser> restaurantUsers) {
		this.restaurantUsers = restaurantUsers;
	}
	public List<Interaction> getInteractions() {
		return interactions;
	}
	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}
	public List<PerformanceMetric> getPerformanceMetrices() {
		return performanceMetrices;
	}
	public void setPerformanceMetrices(List<PerformanceMetric> performanceMetrices) {
		this.performanceMetrices = performanceMetrices;
	}
    
    
}
