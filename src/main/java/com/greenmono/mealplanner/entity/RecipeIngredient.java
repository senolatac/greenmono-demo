package com.greenmono.mealplanner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_ingredients", indexes = {
    @Index(name = "idx_recipe_ingredient_recipe", columnList = "recipe_id"),
    @Index(name = "idx_recipe_ingredient_ingredient", columnList = "ingredient_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @NotNull(message = "Recipe is required")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    @NotNull(message = "Ingredient is required")
    private Ingredient ingredient;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Unit is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Ingredient.Unit unit;

    @Column(length = 500)
    private String notes;

    @NotNull(message = "Optional flag is required")
    @Column(nullable = false)
    @Builder.Default
    private Boolean optional = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredient)) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
