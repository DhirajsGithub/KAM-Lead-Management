package com.udaan.kam.kam_lead_management.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "restaurants", 
       indexes = {
           @Index(name = "idx_restaurants_status", columnList = "lead_status")
       })
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Integer id;

    @NotBlank(message = "Restaurant name cannot be blank")
    @Size(min = 3, max = 100, message = "Restaurant name must be between 3 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Size(max = 50, message = "City name must not exceed 50 characters")
    @Column(name = "city", length = 50)
    private String city;

    @Size(max = 50, message = "State name must not exceed 50 characters")
    @Column(name = "state", length = 50)
    private String state;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    @Column(name = "phone", length = 20)
    private String phone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "lead_status", nullable = false, length = 20)
    @NotNull(message = "Lead status cannot be null")
    private LeadStatus leadStatus = LeadStatus.NEW;

    @Digits(integer = 15, fraction = 2, message = "Annual revenue must be a valid decimal value")
    @Column(name = "annual_revenue", precision = 15, scale = 2)
    private BigDecimal annualRevenue;

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    @Column(name = "timezone", length = 50)
    private String timezone;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "primary_contact_id")
    private Contact primaryContact;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("restaurant")
    private List<Contact> contacts = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("restaurant")
    private List<RestaurantUser> restaurantUsers = new ArrayList<>();
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("restaurant")
    private List<CallSchedule> callSchedules = new ArrayList<>();



    public List<RestaurantUser> getRestaurantUsers() {
		return restaurantUsers;
	}

	public void setRestaurantUsers(List<RestaurantUser> restaurantUsers) {
		this.restaurantUsers = restaurantUsers;
	}

	public List<CallSchedule> getCallSchedules() {
		return callSchedules;
	}

	public void setCallSchedules(List<CallSchedule> callSchedules) {
		this.callSchedules = callSchedules;
	}

	@ManyToOne
    @JoinColumn(name = "latest_interaction_id")
    private Interaction latestInteraction;

    @ManyToOne
    @JoinColumn(name = "latest_order_id")
    private Order latestOrder;

    @ManyToOne
    @JoinColumn(name = "current_schedule_id")
    private CallSchedule currentSchedule;

    @ManyToOne
    @JoinColumn(name = "latest_metric_id")
    private PerformanceMetric latestMetric;

    // Default Constructor (Required by JPA)
    public Restaurant() {}

    // Constructor for mandatory fields
    public Restaurant(String name, String address, String city, String state, String phone, String email, LeadStatus leadStatus) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.email = email;
        this.leadStatus = leadStatus;
    }

    // Getters and Setters
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

    public LeadStatus getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(LeadStatus leadStatus) {
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

    public Contact getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Contact primaryContact) {
        this.primaryContact = primaryContact;
    }
    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Interaction getLatestInteraction() {
        return latestInteraction;
    }

    public void setLatestInteraction(Interaction latestInteraction) {
        this.latestInteraction = latestInteraction;
    }

    public Order getLatestOrder() {
        return latestOrder;
    }

    public void setLatestOrder(Order latestOrder) {
        this.latestOrder = latestOrder;
    }

    public CallSchedule getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(CallSchedule currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public PerformanceMetric getLatestMetric() {
        return latestMetric;
    }

    public void setLatestMetric(PerformanceMetric latestMetric) {
        this.latestMetric = latestMetric;
    }

    // Enum for LeadStatus
    public enum LeadStatus {
        NEW, CONTACTED, QUALIFIED, NEGOTIATING, CONVERTED, LOST
    }
}
