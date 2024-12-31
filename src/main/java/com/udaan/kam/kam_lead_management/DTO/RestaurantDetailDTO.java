package com.udaan.kam.kam_lead_management.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.udaan.kam.kam_lead_management.entity.CallSchedule;
import com.udaan.kam.kam_lead_management.entity.Contact;
import com.udaan.kam.kam_lead_management.entity.Interaction;
import com.udaan.kam.kam_lead_management.entity.Order;

public class RestaurantDetailDTO {
	 private Integer id;
	    private String name;
	    private String address;
	    private String city;
	    private String state;
	    private String phone;
	    private String email;
	    private LocalDateTime createdAt;
	    private String leadStatus;
	    private BigDecimal annualRevenue;
	    private String timezone;
	    private List<UserDTO> users;
	    private List<Contact> contacts;
	    private List<CallSchedule> callSchedules;
	    private List<InteractionDTO> interactions;
	    private List<Order> orders; 

	    
	    public RestaurantDetailDTO(Integer id, String name, String address, String city, String state,
	                         String phone, String email, LocalDateTime createdAt, String leadStatus,
	                         BigDecimal annualRevenue, String timezone, List<UserDTO> users, 
	                         List<Contact> contacts, List<CallSchedule> callSchedules,List<InteractionDTO> interactions,
	                         List<Order> orders) {
	        this.id = id;
	        this.name = name;
	        this.address = address;
	        this.city = city;
	        this.state = state;
	        this.phone = phone;
	        this.email = email;
	        this.createdAt = createdAt;
	        this.leadStatus = leadStatus;
	        this.annualRevenue = annualRevenue;
	        this.timezone = timezone;
	        this.users = users;
	        this.contacts = contacts;
	        this.callSchedules = callSchedules;
	        this.interactions = interactions;
	        this.orders = orders;
	    }
	    
	    

	    public List<Contact> getContacts() {
			return contacts;
		}



		public void setContacts(List<Contact> contacts) {
			this.contacts = contacts;
		}



		public List<CallSchedule> getCallSchedules() {
			return callSchedules;
		}



		public void setCallSchedules(List<CallSchedule> callSchedules) {
			this.callSchedules = callSchedules;
		}



		public List<InteractionDTO> getInteractions() {
			return interactions;
		}



		public void setInteractions(List<InteractionDTO> interactions) {
			this.interactions = interactions;
		}



		public List<Order> getOrders() {
			return orders;
		}



		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}



		public List<UserDTO> getUsers() {
			return users;
		}

		public void setUsers(List<UserDTO> users) {
			this.users = users;
		}

		public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }

	    public String getCity() {
	        return city;
	    }

	    public void setCity(String city) {
	        this.city = city;
	    }

	    public String getState() {
	        return state;
	    }

	    public void setState(String state) {
	        this.state = state;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }

	    public String getLeadStatus() {
	        return leadStatus;
	    }

	    public void setLeadStatus(String leadStatus) {
	        this.leadStatus = leadStatus;
	    }

	    public BigDecimal getAnnualRevenue() {
	        return annualRevenue;
	    }

	    public void setAnnualRevenue(BigDecimal annualRevenue) {
	        this.annualRevenue = annualRevenue;
	    }

	    public String getTimezone() {
	        return timezone;
	    }

	    public void setTimezone(String timezone) {
	        this.timezone = timezone;
	    }
}
