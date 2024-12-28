package com.udaan.kam.kam_lead_management.exception;

import org.springframework.validation.Errors;

public class ValidationException extends RuntimeException {

    private Errors errors;

    public ValidationException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
