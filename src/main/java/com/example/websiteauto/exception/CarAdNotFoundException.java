package com.example.websiteauto.exception;

public class CarAdNotFoundException extends EntityNotFoundException {
    public CarAdNotFoundException(Long id) {
        super("Объявление с ID " + id + " не найдено");
    }
}
