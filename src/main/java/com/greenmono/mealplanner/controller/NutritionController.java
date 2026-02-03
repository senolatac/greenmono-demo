package com.greenmono.mealplanner.controller;

import com.greenmono.mealplanner.dto.DailyNutritionResponse;
import com.greenmono.mealplanner.dto.NutritionInfoResponse;
import com.greenmono.mealplanner.dto.RecipeNutritionRequest;
import com.greenmono.mealplanner.entity.NutritionalInfo;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.exception.RecipeNotFoundException;
import com.greenmono.mealplanner.repository.RecipeRepository;
import com.greenmono.mealplanner.service.NutritionCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/nutrition")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Nutrition", description = "Nutrition calculation and analysis APIs")
public class NutritionController {

    private final NutritionCalculatorService nutritionCalculatorService;
    private final RecipeRepository recipeRepository;

    @Operation(
        summary = "Get ingredient nutrition info",
        description = "Retrieves nutritional information for a specific ingredient"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nutrition info retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Ingredient not found or nutrition info not available")
    })
    @GetMapping("/ingredient/{ingredientId}")
    public ResponseEntity<NutritionInfoResponse> getIngredientNutrition(
            @Parameter(description = "ID of the ingredient", required = true)
            @PathVariable Long ingredientId) {

        log.info("Fetching nutrition info for ingredient id: {}", ingredientId);

        NutritionalInfo nutritionalInfo = nutritionCalculatorService.getIngredientNutrition(ingredientId);

        if (nutritionalInfo == null) {
            log.warn("Nutrition info not found for ingredient id: {}", ingredientId);
            return ResponseEntity.notFound().build();
        }

        NutritionInfoResponse response = mapToResponse(nutritionalInfo);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get meal nutrition info",
        description = "Retrieves nutritional information for a specific meal"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nutrition info retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Meal not found or nutrition info not available")
    })
    @GetMapping("/meal/{mealId}")
    public ResponseEntity<NutritionInfoResponse> getMealNutrition(
            @Parameter(description = "ID of the meal", required = true)
            @PathVariable Long mealId) {

        log.info("Fetching nutrition info for meal id: {}", mealId);

        NutritionalInfo nutritionalInfo = nutritionCalculatorService.getMealNutrition(mealId);

        if (nutritionalInfo == null) {
            log.warn("Nutrition info not found for meal id: {}", mealId);
            return ResponseEntity.notFound().build();
        }

        NutritionInfoResponse response = mapToResponse(nutritionalInfo);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Calculate daily nutrition from recipes",
        description = "Calculates total daily nutrition from a list of recipe IDs and validates against daily requirements (protein 20-30g, carbs 50-80g)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Daily nutrition calculated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "One or more recipes not found")
    })
    @PostMapping("/daily")
    public ResponseEntity<DailyNutritionResponse> calculateDailyNutrition(
            @Valid @RequestBody RecipeNutritionRequest request) {

        log.info("Calculating daily nutrition for {} recipes", request.getRecipeIds().size());

        List<Recipe> recipes = request.getRecipeIds().stream()
            .map(id -> recipeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Recipe not found with id: {}", id);
                    return new RecipeNotFoundException(
                        String.format("Recipe not found with id: %d", id)
                    );
                }))
            .collect(Collectors.toList());

        NutritionalInfo dailyNutrition = nutritionCalculatorService.calculateDailyNutrition(recipes);
        boolean isValid = nutritionCalculatorService.validateDailyNutrition(dailyNutrition);
        double balanceScore = nutritionCalculatorService.calculateNutritionBalanceScore(dailyNutrition);

        boolean proteinInRange = dailyNutrition.getProtein().compareTo(NutritionCalculatorService.DAILY_PROTEIN_MIN) >= 0
            && dailyNutrition.getProtein().compareTo(NutritionCalculatorService.DAILY_PROTEIN_MAX) <= 0;

        boolean carbsInRange = dailyNutrition.getCarbohydrates().compareTo(NutritionCalculatorService.DAILY_CARB_MIN) >= 0
            && dailyNutrition.getCarbohydrates().compareTo(NutritionCalculatorService.DAILY_CARB_MAX) <= 0;

        String message = isValid ? "Daily nutrition meets requirements" : "Daily nutrition outside recommended ranges";

        DailyNutritionResponse response = DailyNutritionResponse.builder()
            .totalCalories(dailyNutrition.getCalories())
            .totalProtein(dailyNutrition.getProtein())
            .totalCarbohydrates(dailyNutrition.getCarbohydrates())
            .totalFat(dailyNutrition.getFat())
            .totalFiber(dailyNutrition.getFiber())
            .proteinInRange(proteinInRange)
            .carbohydratesInRange(carbsInRange)
            .balanced(isValid)
            .proteinMin(NutritionCalculatorService.DAILY_PROTEIN_MIN)
            .proteinMax(NutritionCalculatorService.DAILY_PROTEIN_MAX)
            .carbMin(NutritionCalculatorService.DAILY_CARB_MIN)
            .carbMax(NutritionCalculatorService.DAILY_CARB_MAX)
            .balanceScore(balanceScore)
            .message(message)
            .build();

        log.info("Daily nutrition calculated: {} (Valid: {}, Score: {})",
            message, isValid, balanceScore);

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Calculate recipe nutrition from ingredients",
        description = "Calculates total nutrition for a recipe based on its ingredients"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe nutrition calculated successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<NutritionInfoResponse> calculateRecipeNutrition(
            @Parameter(description = "ID of the recipe", required = true)
            @PathVariable Long recipeId) {

        log.info("Calculating nutrition for recipe id: {}", recipeId);

        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> {
                log.error("Recipe not found with id: {}", recipeId);
                return new RecipeNotFoundException(
                    String.format("Recipe not found with id: %d", recipeId)
                );
            });

        if (recipe.getRecipeIngredients().isEmpty()) {
            log.warn("Recipe {} has no ingredients", recipeId);
            return ResponseEntity.badRequest().build();
        }

        NutritionalInfo nutritionalInfo = nutritionCalculatorService
            .calculateRecipeNutrition(recipe.getRecipeIngredients());

        NutritionInfoResponse response = mapToResponse(nutritionalInfo);
        return ResponseEntity.ok(response);
    }

    private NutritionInfoResponse mapToResponse(NutritionalInfo nutritionalInfo) {
        return NutritionInfoResponse.builder()
            .calories(nutritionalInfo.getCalories())
            .protein(nutritionalInfo.getProtein())
            .carbohydrates(nutritionalInfo.getCarbohydrates())
            .fat(nutritionalInfo.getFat())
            .fiber(nutritionalInfo.getFiber())
            .sugar(nutritionalInfo.getSugar())
            .sodium(nutritionalInfo.getSodium())
            .cholesterol(nutritionalInfo.getCholesterol())
            .saturatedFat(nutritionalInfo.getSaturatedFat())
            .transFat(nutritionalInfo.getTransFat())
            .vitaminA(nutritionalInfo.getVitaminA())
            .vitaminC(nutritionalInfo.getVitaminC())
            .vitaminD(nutritionalInfo.getVitaminD())
            .calcium(nutritionalInfo.getCalcium())
            .iron(nutritionalInfo.getIron())
            .potassium(nutritionalInfo.getPotassium())
            .servingSize(nutritionalInfo.getServingSize())
            .servingUnit(nutritionalInfo.getServingUnit() != null ?
                nutritionalInfo.getServingUnit().name() : null)
            .notes(nutritionalInfo.getNotes())
            .build();
    }
}
