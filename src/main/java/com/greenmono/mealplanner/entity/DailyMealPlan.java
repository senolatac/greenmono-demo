package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_meal_plans", indexes = {
    @Index(name = "idx_daily_meal_plan_menu", columnList = "menu_plan_id"),
    @Index(name = "idx_daily_meal_plan_date", columnList = "meal_date"),
    @Index(name = "idx_daily_meal_plan_day", columnList = "day_number")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyMealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Menu plan is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_plan_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MenuPlan menuPlan;

    @NotNull(message = "Day number is required")
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @NotNull(message = "Meal date is required")
    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breakfast_meal_id")
    private Meal breakfastMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunch_meal_id")
    private Meal lunchMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dinner_meal_id")
    private Meal dinnerMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_meal_id")
    private Meal snackMeal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breakfast_recipe_id")
    private Recipe soupRecipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunch_recipe_id")
    private Recipe mainCourseRecipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dinner_recipe_id")
    private Recipe sideDishRecipe;

    @Column(name = "total_calories")
    private Integer totalCalories;

    @Column(length = 1000)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
