package com.udaan.kam.kam_lead_management.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "performance_metrics", 
       indexes = {
           @Index(name = "idx_metrics_user", columnList = "user_id"),
           @Index(name = "idx_metrics_date", columnList = "metric_date")
       })
public class PerformanceMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer id;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @NotNull(message = "Metric date cannot be null")
    @Column(name = "metric_date", nullable = false)
    private LocalDateTime metricDate;

    @PositiveOrZero(message = "Leads count must be zero or positive")
    @Column(name = "leads_count", nullable = false)
    private Integer leadsCount;

    @PositiveOrZero(message = "Closed deals must be zero or positive")
    @Column(name = "closed_deals", nullable = false)
    private Integer closedDeals;

    @PositiveOrZero(message = "Revenue must be zero or positive")
    @Column(name = "revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal revenue;

    @PositiveOrZero(message = "Follow-ups count must be zero or positive")
    @Column(name = "follow_ups_count", nullable = false)
    private Integer followUpsCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default Constructor
    public PerformanceMetric() {}

    // Constructor for mandatory fields
    public PerformanceMetric(User user, LocalDateTime metricDate, Integer leadsCount, Integer closedDeals, BigDecimal revenue, Integer followUpsCount) {
        this.user = user;
        this.metricDate = metricDate;
        this.leadsCount = leadsCount;
        this.closedDeals = closedDeals;
        this.revenue = revenue;
        this.followUpsCount = followUpsCount;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(String metricDate) {
        this.metricDate =  LocalDateTime.parse(metricDate , DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public Integer getLeadsCount() {
        return leadsCount;
    }

    public void setLeadsCount(Integer leadsCount) {
        this.leadsCount = leadsCount;
    }

    public Integer getClosedDeals() {
        return closedDeals;
    }

    public void setClosedDeals(Integer closedDeals) {
        this.closedDeals = closedDeals;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Integer getFollowUpsCount() {
        return followUpsCount;
    }

    public void setFollowUpsCount(Integer followUpsCount) {
        this.followUpsCount = followUpsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
