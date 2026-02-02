package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.Recipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    @NotBlank(message = "Recipe name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Recipe category is required")
    private Recipe.RecipeCategory category;

    @Valid
    private List<RecipeIngredientRequest> ingredients = new ArrayList<>();

    private List<@Size(max = 1000, message = "Instruction must not exceed 1000 characters") String> instructions = new ArrayList<>();

    @NotNull(message = "Cooking time is required")
    @Positive(message = "Cooking time must be positive")
    private Integer cookingTimeMinutes;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    private Integer servings;

    @NotNull(message = "Calories are required")
    @PositiveOrZero(message = "Calories must be zero or positive")
    private BigDecimal calories;

    @NotNull(message = "Protein is required")
    @PositiveOrZero(message = "Protein must be zero or positive")
    private BigDecimal protein;

    @NotNull(message = "Carbohydrates are required")
    @PositiveOrZero(message = "Carbohydrates must be zero or positive")
    private BigDecimal carbohydrates;

    @PositiveOrZero(message = "Fat must be zero or positive")
    private BigDecimal fat;

    @PositiveOrZero(message = "Fiber must be zero or positive")
    private BigDecimal fiber;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    private Long userId;

    private Boolean active;
}
