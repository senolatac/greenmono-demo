package com.greenmono.mealplanner.dto;

import com.greenmono.mealplanner.entity.MenuPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPlanResponse {

    private Long id;
    private String name;
    private String description;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DailyMealPlanResponse> dailyMealPlans = new ArrayList<>();
    private MenuPlan.MenuPlanStatus status;
    private Integer totalCalories;
    private Integer averageDailyCalories;
    private String notes;
    private Boolean isBalanced;
    private Double balanceScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
