package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "nutritional_info", indexes = {
    @Index(name = "idx_nutritional_ingredient", columnList = "ingredient_id"),
    @Index(name = "idx_nutritional_meal", columnList = "meal_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", unique = true)
    private Ingredient ingredient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", unique = true)
    private Meal meal;

    @NotNull(message = "Serving size is required")
    @PositiveOrZero(message = "Serving size must be zero or positive")
    @Column(name = "serving_size", nullable = false, precision = 10, scale = 2)
    private BigDecimal servingSize;

    @NotNull(message = "Serving unit is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "serving_unit", nullable = false, length = 20)
    private Ingredient.Unit servingUnit;

    @NotNull(message = "Calories are required")
    @PositiveOrZero(message = "Calories must be zero or positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal calories;

    @NotNull(message = "Protein is required")
    @PositiveOrZero(message = "Protein must be zero or positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal protein;

    @NotNull(message = "Carbohydrates are required")
    @PositiveOrZero(message = "Carbohydrates must be zero or positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal carbohydrates;

    @NotNull(message = "Fat is required")
    @PositiveOrZero(message = "Fat must be zero or positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fat;

    @PositiveOrZero(message = "Fiber must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal fiber;

    @PositiveOrZero(message = "Sugar must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal sugar;

    @PositiveOrZero(message = "Sodium must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal sodium;

    @PositiveOrZero(message = "Cholesterol must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal cholesterol;

    @PositiveOrZero(message = "Saturated fat must be zero or positive")
    @Column(name = "saturated_fat", precision = 10, scale = 2)
    private BigDecimal saturatedFat;

    @PositiveOrZero(message = "Trans fat must be zero or positive")
    @Column(name = "trans_fat", precision = 10, scale = 2)
    private BigDecimal transFat;

    @PositiveOrZero(message = "Vitamin A must be zero or positive")
    @Column(name = "vitamin_a", precision = 10, scale = 2)
    private BigDecimal vitaminA;

    @PositiveOrZero(message = "Vitamin C must be zero or positive")
    @Column(name = "vitamin_c", precision = 10, scale = 2)
    private BigDecimal vitaminC;

    @PositiveOrZero(message = "Vitamin D must be zero or positive")
    @Column(name = "vitamin_d", precision = 10, scale = 2)
    private BigDecimal vitaminD;

    @PositiveOrZero(message = "Calcium must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal calcium;

    @PositiveOrZero(message = "Iron must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal iron;

    @PositiveOrZero(message = "Potassium must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal potassium;

    @Column(length = 500)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal getTotalMacronutrients() {
        return protein.add(carbohydrates).add(fat);
    }

    public BigDecimal getProteinCalories() {
        return protein.multiply(new BigDecimal("4"));
    }

    public BigDecimal getCarbCalories() {
        return carbohydrates.multiply(new BigDecimal("4"));
    }

    public BigDecimal getFatCalories() {
        return fat.multiply(new BigDecimal("9"));
    }
}
