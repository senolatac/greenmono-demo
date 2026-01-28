package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_plans", indexes = {
    @Index(name = "idx_menu_plan_user", columnList = "user_id"),
    @Index(name = "idx_menu_plan_start_date", columnList = "start_date"),
    @Index(name = "idx_menu_plan_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Menu plan name is required")
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
        name = "menu_plan_meals",
        joinColumns = @JoinColumn(name = "menu_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "meal_id"),
        indexes = {
            @Index(name = "idx_menu_plan_meal_plan", columnList = "menu_plan_id"),
            @Index(name = "idx_menu_plan_meal_meal", columnList = "meal_id")
        }
    )
    @Builder.Default
    private Set<Meal> meals = new HashSet<>();

    @OneToMany(mappedBy = "menuPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<DailyMealPlan> dailyMealPlans = new HashSet<>();

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MenuPlanStatus status = MenuPlanStatus.DRAFT;

    @Column(name = "total_calories")
    private Integer totalCalories;

    @Column(name = "average_daily_calories")
    private Integer averageDailyCalories;

    @Column(length = 2000)
    private String notes;

    @NotNull(message = "Balanced status is required")
    @Column(name = "is_balanced", nullable = false)
    @Builder.Default
    private Boolean isBalanced = false;

    @Column(name = "balance_score")
    private Double balanceScore;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum MenuPlanStatus {
        DRAFT,
        ACTIVE,
        COMPLETED,
        ARCHIVED
    }

    public Integer getDurationDays() {
        if (startDate != null && endDate != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return 0;
    }
}
