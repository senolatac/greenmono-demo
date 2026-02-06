package com.greenmono.mealplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMealPlanResponse {

    private Long id;
    private Integer dayNumber;
    private LocalDate mealDate;
    private RecipeResponse soupRecipe;
    private RecipeResponse mainCourseRecipe;
    private RecipeResponse sideDishRecipe;
    private Integer totalCalories;
    private String notes;
}
