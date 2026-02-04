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
public class NutritionInfoResponse {

    private BigDecimal calories;
    private BigDecimal protein;
    private BigDecimal carbohydrates;
    private BigDecimal fat;
    private BigDecimal fiber;
    private BigDecimal sugar;
    private BigDecimal sodium;
    private BigDecimal cholesterol;
    private BigDecimal saturatedFat;
    private BigDecimal transFat;
    private BigDecimal vitaminA;
    private BigDecimal vitaminC;
    private BigDecimal vitaminD;
    private BigDecimal calcium;
    private BigDecimal iron;
    private BigDecimal potassium;

    private BigDecimal servingSize;
    private String servingUnit;

    private String notes;
}
