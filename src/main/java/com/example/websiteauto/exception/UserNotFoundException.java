package com.example.websiteauto.exception;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
