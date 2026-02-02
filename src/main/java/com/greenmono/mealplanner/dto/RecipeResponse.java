package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {

    private Long id;
    private String name;
    private String description;
    private Recipe.RecipeCategory category;
    private List<RecipeIngredientResponse> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();
    private Integer cookingTimeMinutes;
    private Integer servings;
    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbohydrates;
    private BigDecimal fat;
    private BigDecimal fiber;
    private String imageUrl;
    private Long userId;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
