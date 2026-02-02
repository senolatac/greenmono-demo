package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientResponse {

    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private BigDecimal quantity;
    private Ingredient.Unit unit;
    private String notes;
    private Boolean optional;
}
