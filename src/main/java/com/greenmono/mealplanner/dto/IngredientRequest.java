package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {

    @NotBlank(message = "Ingredient name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Category is required")
    private Ingredient.IngredientCategory category;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotNull(message = "Unit is required")
    private Ingredient.Unit unit;

    private LocalDate expiryDate;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    private Boolean available;

    private Long userId;
}
