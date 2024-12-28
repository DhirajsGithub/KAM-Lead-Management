package com.udaan.kam.kam_lead_management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "call_schedules", 
       indexes = {
           @Index(name = "idx_call_schedules_next_call", columnList = "next_call_date")
       })
public class CallSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer id;

    @NotNull(message = "Restaurant cannot be null")
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull(message = "Frequency in days is required")
    @Min(value = 1, message = "Frequency must be at least 1 day")
    @Column(name = "frequency_days", nullable = false)
    private Integer frequencyDays;

    @Column(name = "last_call_date")
    private LocalDateTime lastCallDate;

    @Column(name = "next_call_date")
    private LocalDateTime nextCallDate;

    @NotNull(message = "Priority level is required")
    @Min(value = 1, message = "Priority level must be between 1 and 5")
    @Max(value = 5, message = "Priority level must be between 1 and 5")
    @Column(name = "priority_level", nullable = false)
    private Integer priorityLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default Constructor
    public CallSchedule() {}

    // Constructor for mandatory fields
    public CallSchedule(Restaurant restaurant, Integer frequencyDays, Integer priorityLevel) {
        this.restaurant = restaurant;
        this.frequencyDays = frequencyDays;
        this.priorityLevel = priorityLevel;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getFrequencyDays() {
        return frequencyDays;
    }

    public void setFrequencyDays(Integer frequencyDays) {
        this.frequencyDays = frequencyDays;
    }

    public LocalDateTime getLastCallDate() {
        return lastCallDate;
    }

    public void setLastCallDate(LocalDateTime lastCallDate) {
        this.lastCallDate = lastCallDate;
    }

    public LocalDateTime getNextCallDate() {
        return nextCallDate;
    }

    public void setNextCallDate(LocalDateTime nextCallDate) {
        this.nextCallDate = nextCallDate;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
