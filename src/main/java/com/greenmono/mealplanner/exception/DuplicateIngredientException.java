package com.greenmono.mealplanner.exception;

public class DuplicateIngredientException extends RuntimeException {
    public DuplicateIngredientException(String message) {
        super(message);
    }
}
