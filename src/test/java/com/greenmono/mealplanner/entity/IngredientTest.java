package com.greenmono.mealplanner.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ingredient Entity Unit Tests")
class IngredientTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create ingredient with all required fields")
    void shouldCreateIngredientWithRequiredFields() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        assertNotNull(ingredient);
        assertEquals("Chicken Breast", ingredient.getName());
        assertEquals(Ingredient.IngredientCategory.POULTRY, ingredient.getCategory());
        assertEquals(new BigDecimal("500.00"), ingredient.getQuantity());
        assertEquals(Ingredient.Unit.GRAM, ingredient.getUnit());
        assertTrue(ingredient.getAvailable());
    }

    @Test
    @DisplayName("Should create ingredient with all fields including optional ones")
    void shouldCreateIngredientWithAllFields() {
        LocalDate expiryDate = LocalDate.now().plusDays(7);

        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .expiryDate(expiryDate)
                .notes("Fresh from local market")
                .available(true)
                .userId(100L)
                .build();

        assertNotNull(ingredient);
        assertEquals(1L, ingredient.getId());
        assertEquals("Chicken Breast", ingredient.getName());
        assertEquals(Ingredient.IngredientCategory.POULTRY, ingredient.getCategory());
        assertEquals(new BigDecimal("500.00"), ingredient.getQuantity());
        assertEquals(Ingredient.Unit.GRAM, ingredient.getUnit());
        assertEquals(expiryDate, ingredient.getExpiryDate());
        assertEquals("Fresh from local market", ingredient.getNotes());
        assertTrue(ingredient.getAvailable());
        assertEquals(100L, ingredient.getUserId());
    }

    @Test
    @DisplayName("Should default available to true when using builder")
    void shouldDefaultAvailableToTrue() {
        Ingredient ingredient = Ingredient.builder()
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("300.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        assertTrue(ingredient.getAvailable());
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        Ingredient ingredient = Ingredient.builder()
                .name("")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);

        assertFalse(violations.isEmpty());
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("name") &&
            v.getMessage().contains("Ingredient name is required")
        );
    }

    @Test
    @DisplayName("Should fail validation when category is null")
    void shouldFailValidationWhenCategoryIsNull() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(null)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);

        assertFalse(violations.isEmpty());
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("category") &&
            v.getMessage().contains("Category is required")
        );
    }

    @Test
    @DisplayName("Should fail validation when quantity is null")
    void shouldFailValidationWhenQuantityIsNull() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(null)
                .unit(Ingredient.Unit.GRAM)
                .build();

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);

        assertFalse(violations.isEmpty());
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("quantity") &&
            v.getMessage().contains("Quantity is required")
        );
    }

    @Test
    @DisplayName("Should fail validation when quantity is zero or negative")
    void shouldFailValidationWhenQuantityIsNotPositive() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("0"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);

        assertFalse(violations.isEmpty());
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("quantity") &&
            v.getMessage().contains("Quantity must be positive")
        );
    }

    @Test
    @DisplayName("Should fail validation when unit is null")
    void shouldFailValidationWhenUnitIsNull() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(null)
                .build();

        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);

        assertFalse(violations.isEmpty());
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("unit") &&
            v.getMessage().contains("Unit is required")
        );
    }

    @Test
    @DisplayName("Should create ingredient with protein category (MEAT)")
    void shouldCreateIngredientWithProteinCategory() {
        Ingredient ingredient = Ingredient.builder()
                .name("Beef")
                .category(Ingredient.IngredientCategory.MEAT)
                .quantity(new BigDecimal("1000.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        assertEquals(Ingredient.IngredientCategory.MEAT, ingredient.getCategory());
    }

    @Test
    @DisplayName("Should create ingredient with carbohydrate category (GRAINS)")
    void shouldCreateIngredientWithCarbohydrateCategory() {
        Ingredient ingredient = Ingredient.builder()
                .name("Rice")
                .category(Ingredient.IngredientCategory.GRAINS)
                .quantity(new BigDecimal("2.00"))
                .unit(Ingredient.Unit.KILOGRAM)
                .build();

        assertEquals(Ingredient.IngredientCategory.GRAINS, ingredient.getCategory());
    }

    @Test
    @DisplayName("Should create ingredient with vegetable category")
    void shouldCreateIngredientWithVegetableCategory() {
        Ingredient ingredient = Ingredient.builder()
                .name("Carrot")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("5.00"))
                .unit(Ingredient.Unit.PIECE)
                .build();

        assertEquals(Ingredient.IngredientCategory.VEGETABLES, ingredient.getCategory());
    }

    @Test
    @DisplayName("Should test all unit types")
    void shouldSupportAllUnitTypes() {
        Ingredient.Unit[] expectedUnits = {
            Ingredient.Unit.GRAM,
            Ingredient.Unit.KILOGRAM,
            Ingredient.Unit.MILLILITER,
            Ingredient.Unit.LITER,
            Ingredient.Unit.PIECE,
            Ingredient.Unit.TABLESPOON,
            Ingredient.Unit.TEASPOON,
            Ingredient.Unit.CUP,
            Ingredient.Unit.OUNCE,
            Ingredient.Unit.POUND
        };

        Ingredient.Unit[] actualUnits = Ingredient.Unit.values();

        assertArrayEquals(expectedUnits, actualUnits);
    }

    @Test
    @DisplayName("Should test all ingredient categories")
    void shouldSupportAllIngredientCategories() {
        Ingredient.IngredientCategory[] expectedCategories = {
            Ingredient.IngredientCategory.VEGETABLES,
            Ingredient.IngredientCategory.FRUITS,
            Ingredient.IngredientCategory.MEAT,
            Ingredient.IngredientCategory.POULTRY,
            Ingredient.IngredientCategory.FISH,
            Ingredient.IngredientCategory.SEAFOOD,
            Ingredient.IngredientCategory.DAIRY,
            Ingredient.IngredientCategory.GRAINS,
            Ingredient.IngredientCategory.LEGUMES,
            Ingredient.IngredientCategory.NUTS_SEEDS,
            Ingredient.IngredientCategory.HERBS_SPICES,
            Ingredient.IngredientCategory.OILS_FATS,
            Ingredient.IngredientCategory.CONDIMENTS,
            Ingredient.IngredientCategory.BEVERAGES,
            Ingredient.IngredientCategory.OTHER
        };

        Ingredient.IngredientCategory[] actualCategories = Ingredient.IngredientCategory.values();

        assertArrayEquals(expectedCategories, actualCategories);
    }

    @Test
    @DisplayName("Should properly use Lombok equals and hashCode")
    void shouldProperlyUseEqualsAndHashCode() {
        Ingredient ingredient1 = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        assertEquals(ingredient1, ingredient2);
        assertEquals(ingredient1.hashCode(), ingredient2.hashCode());
    }

    @Test
    @DisplayName("Should properly use Lombok toString")
    void shouldProperlyUseToString() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        String toString = ingredient.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Chicken Breast"));
        assertTrue(toString.contains("POULTRY"));
        assertTrue(toString.contains("500"));
    }

    @Test
    @DisplayName("Should initialize meals collection as empty HashSet")
    void shouldInitializeMealsCollectionAsEmptyHashSet() {
        Ingredient ingredient = Ingredient.builder()
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("300.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        assertNotNull(ingredient.getMeals());
        assertTrue(ingredient.getMeals().isEmpty());
    }

    @Test
    @DisplayName("Should support expiry date tracking")
    void shouldSupportExpiryDateTracking() {
        LocalDate expiryDate = LocalDate.now().plusDays(5);

        Ingredient ingredient = Ingredient.builder()
                .name("Milk")
                .category(Ingredient.IngredientCategory.DAIRY)
                .quantity(new BigDecimal("1.00"))
                .unit(Ingredient.Unit.LITER)
                .expiryDate(expiryDate)
                .build();

        assertEquals(expiryDate, ingredient.getExpiryDate());
    }

    @Test
    @DisplayName("Should support notes field")
    void shouldSupportNotesField() {
        Ingredient ingredient = Ingredient.builder()
                .name("Organic Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .notes("Bought from local farmer's market")
                .build();

        assertEquals("Bought from local farmer's market", ingredient.getNotes());
    }

    @Test
    @DisplayName("Should support user association")
    void shouldSupportUserAssociation() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(42L)
                .build();

        assertEquals(42L, ingredient.getUserId());
    }

    @Test
    @DisplayName("Should allow marking ingredient as unavailable")
    void shouldAllowMarkingIngredientAsUnavailable() {
        Ingredient ingredient = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .available(false)
                .build();

        assertFalse(ingredient.getAvailable());
    }
}
