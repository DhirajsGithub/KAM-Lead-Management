package com.udaan.kam.kam_lead_management.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RestaurantDTO {
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

    public RestaurantDTO(Integer id, String name, String address, String city, String state,
                         String phone, String email, LocalDateTime createdAt, String leadStatus,
                         BigDecimal annualRevenue, String timezone) {
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
