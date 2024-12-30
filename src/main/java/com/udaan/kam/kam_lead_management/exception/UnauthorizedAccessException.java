package com.udaan.kam.kam_lead_management.exception;

public class UnauthorizedAccessException extends RuntimeException {
    
    // Constructor with a custom message
    public UnauthorizedAccessException(String message) {
        super(message);  // Call the constructor of RuntimeException
    }
    
    // Constructor with custom message and cause
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
