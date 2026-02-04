package com.greenmono.mealplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenmono.mealplanner.dto.RecipeNutritionRequest;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.entity.NutritionalInfo;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.exception.RecipeNotFoundException;
import com.greenmono.mealplanner.repository.RecipeRepository;
import com.greenmono.mealplanner.service.NutritionCalculatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NutritionController.class)
@DisplayName("NutritionController Integration Tests")
class NutritionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NutritionCalculatorService nutritionCalculatorService;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    @DisplayName("GET /api/nutrition/ingredient/{id} - Should return ingredient nutrition")
    void testGetIngredientNutrition_Success() throws Exception {
        // Given
        Long ingredientId = 1L;
        NutritionalInfo nutritionalInfo = NutritionalInfo.builder()
            .id(1L)
            .servingSize(new BigDecimal("100"))
            .servingUnit(Ingredient.Unit.GRAM)
            .calories(new BigDecimal("165"))
            .protein(new BigDecimal("31"))
            .carbohydrates(new BigDecimal("0"))
            .fat(new BigDecimal("3.6"))
            .fiber(new BigDecimal("0"))
            .build();

        when(nutritionCalculatorService.getIngredientNutrition(ingredientId))
            .thenReturn(nutritionalInfo);

        // When & Then
        mockMvc.perform(get("/api/nutrition/ingredient/{id}", ingredientId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.calories").value(165))
            .andExpect(jsonPath("$.protein").value(31))
            .andExpect(jsonPath("$.carbohydrates").value(0))
            .andExpect(jsonPath("$.fat").value(3.6));
    }

    @Test
    @DisplayName("GET /api/nutrition/ingredient/{id} - Should return 404 when not found")
    void testGetIngredientNutrition_NotFound() throws Exception {
        // Given
        Long ingredientId = 999L;
        when(nutritionCalculatorService.getIngredientNutrition(ingredientId))
            .thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/nutrition/ingredient/{id}", ingredientId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/nutrition/meal/{id} - Should return meal nutrition")
    void testGetMealNutrition_Success() throws Exception {
        // Given
        Long mealId = 1L;
        NutritionalInfo nutritionalInfo = NutritionalInfo.builder()
            .id(10L)
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .calories(new BigDecimal("500"))
            .protein(new BigDecimal("30"))
            .carbohydrates(new BigDecimal("50"))
            .fat(new BigDecimal("15"))
            .build();

        when(nutritionCalculatorService.getMealNutrition(mealId))
            .thenReturn(nutritionalInfo);

        // When & Then
        mockMvc.perform(get("/api/nutrition/meal/{id}", mealId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.calories").value(500))
            .andExpect(jsonPath("$.protein").value(30))
            .andExpect(jsonPath("$.carbohydrates").value(50));
    }

    @Test
    @DisplayName("POST /api/nutrition/daily - Should calculate daily nutrition successfully")
    void testCalculateDailyNutrition_Success() throws Exception {
        // Given
        Recipe recipe1 = Recipe.builder()
            .id(1L)
            .name("Breakfast")
            .calories(new BigDecimal("600"))
            .protein(new BigDecimal("30"))
            .carbohydrates(new BigDecimal("60"))
            .fat(new BigDecimal("15"))
            .servings(2)
            .build();

        Recipe recipe2 = Recipe.builder()
            .id(2L)
            .name("Lunch")
            .calories(new BigDecimal("800"))
            .protein(new BigDecimal("40"))
            .carbohydrates(new BigDecimal("80"))
            .fat(new BigDecimal("20"))
            .servings(2)
            .build();

        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("700"))
            .protein(new BigDecimal("25"))
            .carbohydrates(new BigDecimal("60"))
            .fat(new BigDecimal("17.5"))
            .fiber(new BigDecimal("5"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        RecipeNutritionRequest request = RecipeNutritionRequest.builder()
            .recipeIds(Arrays.asList(1L, 2L))
            .build();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe1));
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe2));
        when(nutritionCalculatorService.calculateDailyNutrition(any()))
            .thenReturn(dailyNutrition);
        when(nutritionCalculatorService.validateDailyNutrition(any()))
            .thenReturn(true);
        when(nutritionCalculatorService.calculateNutritionBalanceScore(any()))
            .thenReturn(95.5);

        // When & Then
        mockMvc.perform(post("/api/nutrition/daily")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalCalories").value(700))
            .andExpect(jsonPath("$.totalProtein").value(25))
            .andExpect(jsonPath("$.totalCarbohydrates").value(60))
            .andExpect(jsonPath("$.balanced").value(true))
            .andExpect(jsonPath("$.balanceScore").value(95.5))
            .andExpect(jsonPath("$.proteinInRange").value(true))
            .andExpect(jsonPath("$.carbohydratesInRange").value(true));
    }

    @Test
    @DisplayName("POST /api/nutrition/daily - Should return 404 when recipe not found")
    void testCalculateDailyNutrition_RecipeNotFound() throws Exception {
        // Given
        RecipeNutritionRequest request = RecipeNutritionRequest.builder()
            .recipeIds(Arrays.asList(999L))
            .build();

        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/nutrition/daily")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/nutrition/recipe/{id} - Should calculate recipe nutrition from ingredients")
    void testCalculateRecipeNutrition_Success() throws Exception {
        // Given
        Long recipeId = 1L;
        Recipe recipe = Recipe.builder()
            .id(recipeId)
            .name("Chicken Rice")
            .calories(new BigDecimal("600"))
            .protein(new BigDecimal("40"))
            .carbohydrates(new BigDecimal("50"))
            .servings(2)
            .build();

        NutritionalInfo calculatedNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("525"))
            .protein(new BigDecimal("66.05"))
            .carbohydrates(new BigDecimal("42"))
            .fat(new BigDecimal("7.65"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(nutritionCalculatorService.calculateRecipeNutrition(any()))
            .thenReturn(calculatedNutrition);

        // When & Then
        mockMvc.perform(get("/api/nutrition/recipe/{id}", recipeId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.calories").value(525))
            .andExpect(jsonPath("$.protein").value(66.05))
            .andExpect(jsonPath("$.carbohydrates").value(42))
            .andExpect(jsonPath("$.fat").value(7.65));
    }

    @Test
    @DisplayName("GET /api/nutrition/recipe/{id} - Should return 404 when recipe not found")
    void testCalculateRecipeNutrition_RecipeNotFound() throws Exception {
        // Given
        Long recipeId = 999L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/nutrition/recipe/{id}", recipeId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/nutrition/daily - Should detect out-of-range nutrition values")
    void testCalculateDailyNutrition_OutOfRange() throws Exception {
        // Given
        Recipe recipe = Recipe.builder()
            .id(1L)
            .name("High Carb Meal")
            .calories(new BigDecimal("1000"))
            .protein(new BigDecimal("20"))
            .carbohydrates(new BigDecimal("180"))
            .fat(new BigDecimal("20"))
            .servings(2)
            .build();

        NutritionalInfo dailyNutrition = NutritionalInfo.builder()
            .calories(new BigDecimal("500"))
            .protein(new BigDecimal("10"))
            .carbohydrates(new BigDecimal("90"))
            .fat(new BigDecimal("10"))
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .build();

        RecipeNutritionRequest request = RecipeNutritionRequest.builder()
            .recipeIds(Arrays.asList(1L))
            .build();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(nutritionCalculatorService.calculateDailyNutrition(any()))
            .thenReturn(dailyNutrition);
        when(nutritionCalculatorService.validateDailyNutrition(any()))
            .thenReturn(false);
        when(nutritionCalculatorService.calculateNutritionBalanceScore(any()))
            .thenReturn(45.0);

        // When & Then
        mockMvc.perform(post("/api/nutrition/daily")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balanced").value(false))
            .andExpect(jsonPath("$.balanceScore").value(45.0))
            .andExpect(jsonPath("$.proteinInRange").value(false))
            .andExpect(jsonPath("$.carbohydratesInRange").value(false))
            .andExpect(jsonPath("$.message").value("Daily nutrition outside recommended ranges"));
    }
}
