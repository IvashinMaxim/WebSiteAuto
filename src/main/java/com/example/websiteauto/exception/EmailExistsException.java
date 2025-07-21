package com.example.websiteauto.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("Email already exists: " + email);
    }
}
