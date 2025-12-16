package com.example.websiteauto.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super("Машина не найдена");
    }
}
