package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes", indexes = {
    @Index(name = "idx_recipe_name", columnList = "name"),
    @Index(name = "idx_recipe_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipe name is required")
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @NotNull(message = "Recipe category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RecipeCategory category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "recipe_instructions",
        joinColumns = @JoinColumn(name = "recipe_id"),
        indexes = @Index(name = "idx_recipe_instructions_recipe", columnList = "recipe_id")
    )
    @Column(name = "instruction", length = 1000)
    @OrderColumn(name = "step_order")
    @Builder.Default
    private List<String> instructions = new ArrayList<>();

    @NotNull(message = "Cooking time is required")
    @Positive(message = "Cooking time must be positive")
    @Column(name = "cooking_time_minutes", nullable = false)
    private Integer cookingTimeMinutes;

    @NotNull(message = "Servings is required")
    @Positive(message = "Servings must be positive")
    @Column(nullable = false)
    @Builder.Default
    private Integer servings = 1;

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

    @PositiveOrZero(message = "Fat must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal fat;

    @PositiveOrZero(message = "Fiber must be zero or positive")
    @Column(precision = 10, scale = 2)
    private BigDecimal fiber;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

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

    public enum RecipeCategory {
        MAIN_COURSE,
        SOUP,
        APPETIZER,
        DESSERT,
        SIDE_DISH,
        SALAD,
        BREAKFAST,
        SNACK,
        BEVERAGE
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
    }

    public void removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setRecipe(null);
    }

    public BigDecimal getTotalMacronutrients() {
        BigDecimal total = protein.add(carbohydrates);
        if (fat != null) {
            total = total.add(fat);
        }
        return total;
    }

    public BigDecimal getCaloriesPerServing() {
        return calories.divide(new BigDecimal(servings), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getProteinPerServing() {
        return protein.divide(new BigDecimal(servings), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCarbsPerServing() {
        return carbohydrates.divide(new BigDecimal(servings), 2, BigDecimal.ROUND_HALF_UP);
    }
}
