package com.greenmono.mealplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNutritionResponse {

    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalCarbohydrates;
    private BigDecimal totalFat;
    private BigDecimal totalFiber;

    private boolean proteinInRange;
    private boolean carbohydratesInRange;
    private boolean balanced;

    private BigDecimal proteinMin;
    private BigDecimal proteinMax;
    private BigDecimal carbMin;
    private BigDecimal carbMax;

    private double balanceScore;

    private String message;
}
