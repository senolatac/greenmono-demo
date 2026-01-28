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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients", indexes = {
    @Index(name = "idx_ingredient_name", columnList = "name"),
    @Index(name = "idx_ingredient_category", columnList = "category"),
    @Index(name = "idx_ingredient_expiry", columnList = "expiry_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ingredient name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private IngredientCategory category;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @NotNull(message = "Unit is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Unit unit;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(length = 500)
    private String notes;

    @NotNull(message = "Available status is required")
    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private NutritionalInfo nutritionalInfo;

    @ManyToMany(mappedBy = "ingredients")
    @Builder.Default
    private Set<Meal> meals = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum IngredientCategory {
        VEGETABLES,
        FRUITS,
        MEAT,
        POULTRY,
        FISH,
        SEAFOOD,
        DAIRY,
        GRAINS,
        LEGUMES,
        NUTS_SEEDS,
        HERBS_SPICES,
        OILS_FATS,
        CONDIMENTS,
        BEVERAGES,
        OTHER
    }

    public enum Unit {
        GRAM,
        KILOGRAM,
        MILLILITER,
        LITER,
        PIECE,
        TABLESPOON,
        TEASPOON,
        CUP,
        OUNCE,
        POUND
    }
}
