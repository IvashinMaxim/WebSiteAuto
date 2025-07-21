package com.example.websiteauto.exception;

public class CarAdNotFoundException extends EntityNotFoundException {
    public CarAdNotFoundException(Long id) {
        super("Car ad not found with id: " + id);
    }
}
