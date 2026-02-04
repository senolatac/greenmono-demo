package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.entity.NutritionalInfo;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.entity.RecipeIngredient;
import com.greenmono.mealplanner.repository.NutritionalInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NutritionCalculatorService Tests")
class NutritionCalculatorServiceTest {

    @Mock
    private NutritionalInfoRepository nutritionalInfoRepository;

    @InjectMocks
    private NutritionCalculatorService nutritionCalculatorService;

    private Ingredient chickenIngredient;
    private Ingredient riceIngredient;
    private NutritionalInfo chickenNutrition;
    private NutritionalInfo riceNutrition;

    @BeforeEach
    void setUp() {
        // Setup chicken ingredient
        chickenIngredient = Ingredient.builder()
            .id(1L)
            .name("Chicken Breast")
            .category(Ingredient.IngredientCategory.POULTRY)
            .quantity(new BigDecimal("500"))
            .unit(Ingredient.Unit.GRAM)
            .build();

        chickenNutrition = NutritionalInfo.builder()
            .id(1L)
            .ingredient(chickenIngredient)
            .servingSize(new BigDecimal("100"))
            .servingUnit(Ingredient.Unit.GRAM)
            .calories(new BigDecimal("165"))
            .protein(new BigDecimal("31"))
            .carbohydrates(new BigDecimal("0"))
            .fat(new BigDecimal("3.6"))
            .fiber(new BigDecimal("0"))
            .build();

        chickenIngredient.setNutritionalInfo(chickenNutrition);

        // Setup rice ingredient
        riceIngredient = Ingredient.builder()
            .id(2L)
            .name("Rice")
            .category(Ingredient.IngredientCategory.GRAINS)
            .quantity(new BigDecimal("1000"))
            .unit(Ingredient.Unit.GRAM)
            .build();

        riceNutrition = NutritionalInfo.builder()
            .id(2L)
            .ingredient(riceIngredient)
            .servingSize(new BigDecimal("100"))
            .servingUnit(Ingredient.Unit.GRAM)
            .calories(new BigDecimal("130"))
            .protein(new BigDecimal("2.7"))
            .carbohydrates(new BigDecimal("28"))
            .fat(new BigDecimal("0.3"))
            .fiber(new BigDecimal("0.4"))
            .build();

        riceIngredient.setNutritionalInfo(riceNutrition);
    }

    @Test
    @DisplayName("Should calculate recipe nutrition from ingredients correctly")
    void testCalculateRecipeNutrition() {
        // Given
        RecipeIngredient chickenRecipeIngredient = RecipeIngredient.builder()
            .ingredient(chickenIngredient)
            .quantity(new BigDecimal("200"))
            .unit(Ingredient.Unit.GRAM)
            .build();

        RecipeIngredient riceRecipeIngredient = RecipeIngredient.builder()
            .ingredient(riceIngredient)
            .quantity(new BigDecimal("150"))
            .unit(Ingredient.Unit.GRAM)
            .build();

        List<RecipeIngredient> recipeIngredients = Arrays.asList(
            chickenRecipeIngredient,
            riceRecipeIngredient
        );

        // When
        NutritionalInfo result = nutritionCalculatorService.calculateRecipeNutrition(recipeIngredients);

        // Then
        assertThat(result).isNotNull();
        // Chicken: 200g = 2 * (165cal, 31g protein, 0g carbs, 3.6g fat)
        // Rice: 150g = 1.5 * (130cal, 2.7g protein, 28g carbs, 0.3g fat)
        // Total: 525cal, 66.05g protein, 42g carbs, 7.65g fat
        assertThat(result.getCalories()).isEqualByComparingTo("525.00");
        assertThat(result.getProtein()).isEqualByComparingTo("66.05");
        assertThat(result.getCarbohydrates()).isEqualByComparingTo("42.00");
        assertThat(result.getFat()).isEqualByComparingTo("7.65");
    }

    @Test
    @DisplayName("Should calculate daily nutrition from multiple recipes")
    void testCalculateDailyNutrition() {
        // Given
        Recipe breakfast = Recipe.builder()
            .id(1L)
            .name("Breakfast")
            .calories(new BigDecimal("600"))
            .protein(new BigDecimal("25"))
            .carbohydrates(new BigDecimal("60"))
            .fat(new BigDecimal("15"))
            .servings(2)
            .build();

        Recipe lunch = Recipe.builder()
            .id(2L)
            .name("Lunch")
            .calories(new BigDecimal("800"))
            .protein(new BigDecimal("30"))
            .carbohydrates(new BigDecimal("80"))
            .fat(new BigDecimal("20"))
            .servings(2)
            .build();

        Recipe dinner = Recipe.builder()
            .id(3L)
            .name("Dinner")
            .calories(new BigDecimal("700"))
            .protein(new BigDecimal("35"))
            .carbohydrates(new BigDecimal("70"))
            .fat(new BigDecimal("18"))
            .servings(2)
            .build();

        List<Recipe> recipes = Arrays.asList(breakfast, lunch, dinner);

        // When
        NutritionalInfo result = nutritionCalculatorService.calculateDailyNutrition(recipes);

        // Then
        assertThat(result).isNotNull();
        // Per serving: (600/2 + 800/2 + 700/2) = 1050 calories
        assertThat(result.getCalories()).isEqualByComparingTo("1050.00");
        // Per serving: (25/2 + 30/2 + 35/2) = 45g protein
        assertThat(result.getProtein()).isEqualByComparingTo("45.00");
        // Per serving: (60/2 + 80/2 + 70/2) = 105g carbs
        assertThat(result.getCarbohydrates()).isEqualByComparingTo("105.00");
    }

    @Test
    @DisplayName("Should validate daily nutrition within range")
    void testValidateDailyNutrition_WithinRange() {
        // Given
        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("1500"))
            .protein(new BigDecimal("25")) // Within 20-30g
            .carbohydrates(new BigDecimal("60")) // Within 50-80g
            .fat(new BigDecimal("50"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        // When
        boolean result = nutritionCalculatorService.validateDailyNutrition(dailyNutrition);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should invalidate daily nutrition below protein range")
    void testValidateDailyNutrition_BelowProteinRange() {
        // Given
        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("1500"))
            .protein(new BigDecimal("15")) // Below 20g
            .carbohydrates(new BigDecimal("60"))
            .fat(new BigDecimal("50"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        // When
        boolean result = nutritionCalculatorService.validateDailyNutrition(dailyNutrition);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should invalidate daily nutrition above carb range")
    void testValidateDailyNutrition_AboveCarbRange() {
        // Given
        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("1500"))
            .protein(new BigDecimal("25"))
            .carbohydrates(new BigDecimal("90")) // Above 80g
            .fat(new BigDecimal("50"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        // When
        boolean result = nutritionCalculatorService.validateDailyNutrition(dailyNutrition);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should calculate perfect nutrition balance score")
    void testCalculateNutritionBalanceScore_Perfect() {
        // Given - perfect nutrition within ranges
        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("1500"))
            .protein(new BigDecimal("25"))
            .carbohydrates(new BigDecimal("65"))
            .fat(new BigDecimal("50"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        // When
        double score = nutritionCalculatorService.calculateNutritionBalanceScore(dailyNutrition);

        // Then
        assertThat(score).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should calculate reduced score for nutrition outside ranges")
    void testCalculateNutritionBalanceScore_OutsideRange() {
        // Given - below protein range
        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("1500"))
            .protein(new BigDecimal("10")) // 50% of minimum
            .carbohydrates(new BigDecimal("65"))
            .fat(new BigDecimal("50"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        // When
        double score = nutritionCalculatorService.calculateNutritionBalanceScore(dailyNutrition);

        // Then
        assertThat(score).isLessThan(100.0);
        assertThat(score).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("Should calculate calories from macronutrients")
    void testCalculateCaloriesFromMacros() {
        // Given
        BigDecimal protein = new BigDecimal("25"); // 25g * 4 = 100 cal
        BigDecimal carbs = new BigDecimal("50"); // 50g * 4 = 200 cal
        BigDecimal fat = new BigDecimal("20"); // 20g * 9 = 180 cal
        // Total expected: 480 cal

        // When
        BigDecimal result = nutritionCalculatorService.calculateCaloriesFromMacros(protein, carbs, fat);

        // Then
        assertThat(result).isEqualByComparingTo("480.00");
    }

    @Test
    @DisplayName("Should get ingredient nutrition by ID")
    void testGetIngredientNutrition() {
        // Given
        Long ingredientId = 1L;
        when(nutritionalInfoRepository.findByIngredientId(ingredientId))
            .thenReturn(Optional.of(chickenNutrition));

        // When
        NutritionalInfo result = nutritionCalculatorService.getIngredientNutrition(ingredientId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProtein()).isEqualByComparingTo("31");
    }

    @Test
    @DisplayName("Should return null for non-existent ingredient nutrition")
    void testGetIngredientNutrition_NotFound() {
        // Given
        Long ingredientId = 999L;
        when(nutritionalInfoRepository.findByIngredientId(ingredientId))
            .thenReturn(Optional.empty());

        // When
        NutritionalInfo result = nutritionCalculatorService.getIngredientNutrition(ingredientId);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should get meal nutrition by ID")
    void testGetMealNutrition() {
        // Given
        Long mealId = 1L;
        NutritionalInfo mealNutrition = NutritionalInfo.builder()
            .id(10L)
            .calories(new BigDecimal("500"))
            .protein(new BigDecimal("30"))
            .carbohydrates(new BigDecimal("50"))
            .fat(new BigDecimal("15"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        when(nutritionalInfoRepository.findByMealId(mealId))
            .thenReturn(Optional.of(mealNutrition));

        // When
        NutritionalInfo result = nutritionCalculatorService.getMealNutrition(mealId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getCalories()).isEqualByComparingTo("500");
    }

    @Test
    @DisplayName("Should handle recipe with no ingredients")
    void testCalculateRecipeNutrition_EmptyIngredients() {
        // Given
        List<RecipeIngredient> emptyIngredients = Arrays.asList();

        // When
        NutritionalInfo result = nutritionCalculatorService.calculateRecipeNutrition(emptyIngredients);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCalories()).isEqualByComparingTo("0.00");
        assertThat(result.getProtein()).isEqualByComparingTo("0.00");
        assertThat(result.getCarbohydrates()).isEqualByComparingTo("0.00");
    }
}
