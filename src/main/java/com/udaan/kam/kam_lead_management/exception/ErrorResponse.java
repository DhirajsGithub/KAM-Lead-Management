package com.udaan.kam.kam_lead_management.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private String details;
    private LocalDateTime timestamp;
    private int code;

    // Constructor
    public ErrorResponse(String message, String details, int code) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.code = code;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
