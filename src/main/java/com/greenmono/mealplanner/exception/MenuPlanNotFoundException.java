package com.greenmono.mealplanner.exception;

public class MenuPlanNotFoundException extends RuntimeException {
    public MenuPlanNotFoundException(String message) {
        super(message);
    }
}
