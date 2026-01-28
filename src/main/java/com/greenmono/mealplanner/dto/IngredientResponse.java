package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponse {

    private Long id;
    private String name;
    private Ingredient.IngredientCategory category;
    private BigDecimal quantity;
    private Ingredient.Unit unit;
    private LocalDate expiryDate;
    private String notes;
    private Boolean available;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
