package com.example.websiteauto.exception;

public class CarAdNotFoundException extends RuntimeException {
    public CarAdNotFoundException(Long id) {
        super("Объявление с ID " + id + " не найдено");
    }
}
