package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("IngredientRepository Integration Tests")
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    private Ingredient testIngredient1;
    private Ingredient testIngredient2;
    private Ingredient testIngredient3;

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();

        testIngredient1 = Ingredient.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .available(true)
                .expiryDate(LocalDate.now().plusDays(7))
                .notes("Fresh chicken")
                .build();

        testIngredient2 = Ingredient.builder()
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("300.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .available(true)
                .expiryDate(LocalDate.now().plusDays(3))
                .notes("Organic tomatoes")
                .build();

        testIngredient3 = Ingredient.builder()
                .name("Rice")
                .category(Ingredient.IngredientCategory.GRAINS)
                .quantity(new BigDecimal("2.00"))
                .unit(Ingredient.Unit.KILOGRAM)
                .userId(2L)
                .available(false)
                .expiryDate(LocalDate.now().plusMonths(6))
                .notes("Basmati rice")
                .build();
    }

    @Test
    @DisplayName("Should save and retrieve ingredient by id")
    void shouldSaveAndRetrieveIngredientById() {
        Ingredient savedIngredient = ingredientRepository.save(testIngredient1);

        assertNotNull(savedIngredient.getId());

        Optional<Ingredient> retrievedIngredient = ingredientRepository.findById(savedIngredient.getId());

        assertTrue(retrievedIngredient.isPresent());
        assertEquals("Chicken Breast", retrievedIngredient.get().getName());
        assertEquals(Ingredient.IngredientCategory.POULTRY, retrievedIngredient.get().getCategory());
        assertEquals(new BigDecimal("500.00"), retrievedIngredient.get().getQuantity());
        assertEquals(Ingredient.Unit.GRAM, retrievedIngredient.get().getUnit());
    }

    @Test
    @DisplayName("Should find all ingredients for a specific user")
    void shouldFindAllIngredientsByUserId() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> user1Ingredients = ingredientRepository.findByUserId(1L);

        assertEquals(2, user1Ingredients.size());
        assertThat(user1Ingredients)
                .extracting(Ingredient::getName)
                .containsExactlyInAnyOrder("Chicken Breast", "Tomato");
    }

    @Test
    @DisplayName("Should find only available ingredients for a user")
    void shouldFindOnlyAvailableIngredientsForUser() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);

        testIngredient1.setAvailable(false);
        ingredientRepository.save(testIngredient1);

        List<Ingredient> availableIngredients = ingredientRepository.findByUserIdAndAvailableTrue(1L);

        assertEquals(1, availableIngredients.size());
        assertEquals("Tomato", availableIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should find ingredients by category")
    void shouldFindIngredientsByCategory() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> vegetables = ingredientRepository.findByCategory(Ingredient.IngredientCategory.VEGETABLES);

        assertEquals(1, vegetables.size());
        assertEquals("Tomato", vegetables.get(0).getName());
    }

    @Test
    @DisplayName("Should find ingredients by user and category")
    void shouldFindIngredientsByUserIdAndCategory() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> user1Poultry = ingredientRepository.findByUserIdAndCategory(
                1L,
                Ingredient.IngredientCategory.POULTRY
        );

        assertEquals(1, user1Poultry.size());
        assertEquals("Chicken Breast", user1Poultry.get(0).getName());
    }

    @Test
    @DisplayName("Should find ingredients expiring before a specific date")
    void shouldFindIngredientsExpiringBeforeDate() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        LocalDate cutoffDate = LocalDate.now().plusDays(5);
        List<Ingredient> expiringIngredients = ingredientRepository.findByExpiryDateBefore(cutoffDate);

        assertEquals(1, expiringIngredients.size());
        assertEquals("Tomato", expiringIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should find ingredients expiring within a date range for a user")
    void shouldFindIngredientsExpiringWithinDateRangeForUser() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        List<Ingredient> expiringIngredients = ingredientRepository.findByUserIdAndExpiryDateBetween(
                1L,
                startDate,
                endDate
        );

        assertEquals(1, expiringIngredients.size());
        assertEquals("Tomato", expiringIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should find available ingredients for user that are not expired")
    void shouldFindAvailableIngredientsForUserNotExpired() {
        testIngredient2.setExpiryDate(LocalDate.now().minusDays(1));
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> availableIngredients = ingredientRepository.findAvailableIngredientsForUser(
                1L,
                LocalDate.now()
        );

        assertEquals(1, availableIngredients.size());
        assertEquals("Chicken Breast", availableIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should include ingredients with no expiry date in available ingredients")
    void shouldIncludeIngredientsWithNoExpiryDateInAvailableIngredients() {
        testIngredient1.setExpiryDate(null);
        ingredientRepository.save(testIngredient1);

        List<Ingredient> availableIngredients = ingredientRepository.findAvailableIngredientsForUser(
                1L,
                LocalDate.now()
        );

        assertEquals(1, availableIngredients.size());
        assertEquals("Chicken Breast", availableIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should search ingredients by name")
    void shouldSearchIngredientsByName() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> searchResults = ingredientRepository.searchIngredients("Chicken");

        assertEquals(1, searchResults.size());
        assertEquals("Chicken Breast", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Should search ingredients by notes")
    void shouldSearchIngredientsByNotes() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> searchResults = ingredientRepository.searchIngredients("Organic");

        assertEquals(1, searchResults.size());
        assertEquals("Tomato", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Should perform case-insensitive search")
    void shouldPerformCaseInsensitiveSearch() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        List<Ingredient> searchResults = ingredientRepository.searchIngredients("chicken");

        assertEquals(1, searchResults.size());
        assertEquals("Chicken Breast", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Should check if ingredient exists for user by name")
    void shouldCheckIfIngredientExistsForUserByName() {
        ingredientRepository.save(testIngredient1);

        assertTrue(ingredientRepository.existsByUserIdAndName(1L, "Chicken Breast"));
        assertFalse(ingredientRepository.existsByUserIdAndName(1L, "Beef"));
        assertFalse(ingredientRepository.existsByUserIdAndName(2L, "Chicken Breast"));
    }

    @Test
    @DisplayName("Should update ingredient quantity")
    void shouldUpdateIngredientQuantity() {
        Ingredient savedIngredient = ingredientRepository.save(testIngredient1);

        savedIngredient.setQuantity(new BigDecimal("750.00"));
        ingredientRepository.save(savedIngredient);

        Optional<Ingredient> updatedIngredient = ingredientRepository.findById(savedIngredient.getId());

        assertTrue(updatedIngredient.isPresent());
        assertEquals(new BigDecimal("750.00"), updatedIngredient.get().getQuantity());
    }

    @Test
    @DisplayName("Should delete ingredient by id")
    void shouldDeleteIngredientById() {
        Ingredient savedIngredient = ingredientRepository.save(testIngredient1);
        Long ingredientId = savedIngredient.getId();

        ingredientRepository.deleteById(ingredientId);

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(ingredientId);
        assertFalse(deletedIngredient.isPresent());
    }

    @Test
    @DisplayName("Should count all ingredients")
    void shouldCountAllIngredients() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);

        long count = ingredientRepository.count();

        assertEquals(3, count);
    }

    @Test
    @DisplayName("Should save ingredient with all field types")
    void shouldSaveIngredientWithAllFieldTypes() {
        Ingredient ingredient = Ingredient.builder()
                .name("Olive Oil")
                .category(Ingredient.IngredientCategory.OILS_FATS)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.MILLILITER)
                .userId(1L)
                .available(true)
                .expiryDate(LocalDate.now().plusYears(1))
                .notes("Extra virgin olive oil")
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        assertNotNull(savedIngredient.getId());
        assertNotNull(savedIngredient.getCreatedAt());
        assertNotNull(savedIngredient.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle multiple categories correctly")
    void shouldHandleMultipleCategoriesCorrectly() {
        Ingredient meat = Ingredient.builder()
                .name("Beef")
                .category(Ingredient.IngredientCategory.MEAT)
                .quantity(new BigDecimal("1000.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .build();

        Ingredient fish = Ingredient.builder()
                .name("Salmon")
                .category(Ingredient.IngredientCategory.FISH)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .build();

        Ingredient legume = Ingredient.builder()
                .name("Lentils")
                .category(Ingredient.IngredientCategory.LEGUMES)
                .quantity(new BigDecimal("250.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .build();

        ingredientRepository.save(meat);
        ingredientRepository.save(fish);
        ingredientRepository.save(legume);

        List<Ingredient> meatIngredients = ingredientRepository.findByCategory(Ingredient.IngredientCategory.MEAT);
        List<Ingredient> fishIngredients = ingredientRepository.findByCategory(Ingredient.IngredientCategory.FISH);
        List<Ingredient> legumeIngredients = ingredientRepository.findByCategory(Ingredient.IngredientCategory.LEGUMES);

        assertEquals(1, meatIngredients.size());
        assertEquals(1, fishIngredients.size());
        assertEquals(1, legumeIngredients.size());
        assertEquals("Beef", meatIngredients.get(0).getName());
        assertEquals("Salmon", fishIngredients.get(0).getName());
        assertEquals("Lentils", legumeIngredients.get(0).getName());
    }

    @Test
    @DisplayName("Should handle various units correctly")
    void shouldHandleVariousUnitsCorrectly() {
        Ingredient gramIngredient = Ingredient.builder()
                .name("Sugar")
                .category(Ingredient.IngredientCategory.OTHER)
                .quantity(new BigDecimal("250.00"))
                .unit(Ingredient.Unit.GRAM)
                .userId(1L)
                .build();

        Ingredient literIngredient = Ingredient.builder()
                .name("Water")
                .category(Ingredient.IngredientCategory.BEVERAGES)
                .quantity(new BigDecimal("1.50"))
                .unit(Ingredient.Unit.LITER)
                .userId(1L)
                .build();

        Ingredient pieceIngredient = Ingredient.builder()
                .name("Eggs")
                .category(Ingredient.IngredientCategory.DAIRY)
                .quantity(new BigDecimal("12.00"))
                .unit(Ingredient.Unit.PIECE)
                .userId(1L)
                .build();

        ingredientRepository.save(gramIngredient);
        ingredientRepository.save(literIngredient);
        ingredientRepository.save(pieceIngredient);

        List<Ingredient> allIngredients = ingredientRepository.findByUserId(1L);

        assertEquals(3, allIngredients.size());
    }

    @Test
    @DisplayName("Should return empty list when no ingredients match criteria")
    void shouldReturnEmptyListWhenNoIngredientsMatchCriteria() {
        ingredientRepository.save(testIngredient1);

        List<Ingredient> nonExistentUserIngredients = ingredientRepository.findByUserId(999L);
        List<Ingredient> nonExistentCategoryIngredients = ingredientRepository.findByCategory(
                Ingredient.IngredientCategory.BEVERAGES
        );

        assertTrue(nonExistentUserIngredients.isEmpty());
        assertTrue(nonExistentCategoryIngredients.isEmpty());
    }
}
