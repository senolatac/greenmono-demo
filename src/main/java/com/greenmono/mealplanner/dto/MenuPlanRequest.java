package com.greenmono.mealplanner.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPlanRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Target daily calories is required")
    @Positive(message = "Target daily calories must be positive")
    private Integer targetDailyCalories;

    @NotNull(message = "Calories per meal minimum is required")
    @PositiveOrZero(message = "Calories per meal minimum must be zero or positive")
    @Builder.Default
    private Integer caloriesPerMealMin = 500;

    @NotNull(message = "Calories per meal maximum is required")
    @Positive(message = "Calories per meal maximum must be positive")
    @Builder.Default
    private Integer caloriesPerMealMax = 700;

    private String notes;
}
