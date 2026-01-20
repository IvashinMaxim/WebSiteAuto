package com.example.websiteauto.exception;

public class CarNotFoundException extends EntityNotFoundException {
    public CarNotFoundException(String message) {
        super("Машина не найдена");
    }
}
