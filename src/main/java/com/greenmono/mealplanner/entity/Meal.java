package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meals", indexes = {
    @Index(name = "idx_meal_name", columnList = "name"),
    @Index(name = "idx_meal_type", columnList = "meal_type"),
    @Index(name = "idx_meal_difficulty", columnList = "difficulty_level")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Meal name is required")
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @NotNull(message = "Meal type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    @ManyToMany
    @JoinTable(
        name = "meal_ingredients",
        joinColumns = @JoinColumn(name = "meal_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id"),
        indexes = {
            @Index(name = "idx_meal_ingredient_meal", columnList = "meal_id"),
            @Index(name = "idx_meal_ingredient_ingredient", columnList = "ingredient_id")
        }
    )
    @Builder.Default
    private Set<Ingredient> ingredients = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "meal_instructions",
        joinColumns = @JoinColumn(name = "meal_id"),
        indexes = @Index(name = "idx_meal_instructions_meal", columnList = "meal_id")
    )
    @Column(name = "instruction", length = 1000)
    @OrderColumn(name = "step_order")
    @Builder.Default
    private Set<String> instructions = new HashSet<>();

    @NotNull(message = "Preparation time is required")
    @Positive(message = "Preparation time must be positive")
    @Column(name = "prep_time_minutes", nullable = false)
    private Integer prepTimeMinutes;

    @NotNull(message = "Cooking time is required")
    @Positive(message = "Cooking time must be positive")
    @Column(name = "cook_time_minutes", nullable = false)
    private Integer cookTimeMinutes;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    @Column(nullable = false)
    private Integer servings;

    @NotNull(message = "Difficulty level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;

    @ElementCollection
    @CollectionTable(
        name = "meal_tags",
        joinColumns = @JoinColumn(name = "meal_id"),
        indexes = @Index(name = "idx_meal_tags_meal", columnList = "meal_id")
    )
    @Column(name = "tag", length = 50)
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @OneToOne(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private NutritionalInfo nutritionalInfo;

    @ManyToMany(mappedBy = "meals")
    @Builder.Default
    private Set<MenuPlan> menuPlans = new HashSet<>();

    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Active status is required")
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACK,
        DESSERT
    }

    public enum DifficultyLevel {
        EASY,
        MEDIUM,
        HARD
    }

    public Integer getTotalTimeMinutes() {
        return prepTimeMinutes + cookTimeMinutes;
    }
}
