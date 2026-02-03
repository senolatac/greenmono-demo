package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.entity.*;
import com.greenmono.mealplanner.repository.NutritionalInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutritionCalculatorService {

    private final NutritionalInfoRepository nutritionalInfoRepository;

    private static final BigDecimal CALORIES_PER_GRAM_PROTEIN = new BigDecimal("4");
    private static final BigDecimal CALORIES_PER_GRAM_CARB = new BigDecimal("4");
    private static final BigDecimal CALORIES_PER_GRAM_FAT = new BigDecimal("9");

    public static final BigDecimal DAILY_PROTEIN_MIN = new BigDecimal("20");
    public static final BigDecimal DAILY_PROTEIN_MAX = new BigDecimal("30");
    public static final BigDecimal DAILY_CARB_MIN = new BigDecimal("50");
    public static final BigDecimal DAILY_CARB_MAX = new BigDecimal("80");

    /**
     * Calculates total nutrition for a recipe from its ingredients
     *
     * @param recipeIngredients List of recipe ingredients with quantities
     * @return NutritionalInfo containing total nutrition values
     */
    public NutritionalInfo calculateRecipeNutrition(List<RecipeIngredient> recipeIngredients) {
        log.debug("Calculating nutrition for recipe with {} ingredients", recipeIngredients.size());

        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        BigDecimal totalFiber = BigDecimal.ZERO;
        BigDecimal totalSugar = BigDecimal.ZERO;
        BigDecimal totalSodium = BigDecimal.ZERO;
        BigDecimal totalCholesterol = BigDecimal.ZERO;
        BigDecimal totalSaturatedFat = BigDecimal.ZERO;
        BigDecimal totalTransFat = BigDecimal.ZERO;
        BigDecimal totalVitaminA = BigDecimal.ZERO;
        BigDecimal totalVitaminC = BigDecimal.ZERO;
        BigDecimal totalVitaminD = BigDecimal.ZERO;
        BigDecimal totalCalcium = BigDecimal.ZERO;
        BigDecimal totalIron = BigDecimal.ZERO;
        BigDecimal totalPotassium = BigDecimal.ZERO;

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            NutritionalInfo ingredientNutrition = ingredient.getNutritionalInfo();

            if (ingredientNutrition == null) {
                log.warn("No nutritional info found for ingredient: {}", ingredient.getName());
                continue;
            }

            BigDecimal quantityRatio = calculateQuantityRatio(
                recipeIngredient.getQuantity(),
                recipeIngredient.getUnit(),
                ingredientNutrition.getServingSize(),
                ingredientNutrition.getServingUnit()
            );

            totalCalories = totalCalories.add(ingredientNutrition.getCalories().multiply(quantityRatio));
            totalProtein = totalProtein.add(ingredientNutrition.getProtein().multiply(quantityRatio));
            totalCarbs = totalCarbs.add(ingredientNutrition.getCarbohydrates().multiply(quantityRatio));
            totalFat = totalFat.add(ingredientNutrition.getFat().multiply(quantityRatio));

            if (ingredientNutrition.getFiber() != null) {
                totalFiber = totalFiber.add(ingredientNutrition.getFiber().multiply(quantityRatio));
            }
            if (ingredientNutrition.getSugar() != null) {
                totalSugar = totalSugar.add(ingredientNutrition.getSugar().multiply(quantityRatio));
            }
            if (ingredientNutrition.getSodium() != null) {
                totalSodium = totalSodium.add(ingredientNutrition.getSodium().multiply(quantityRatio));
            }
            if (ingredientNutrition.getCholesterol() != null) {
                totalCholesterol = totalCholesterol.add(ingredientNutrition.getCholesterol().multiply(quantityRatio));
            }
            if (ingredientNutrition.getSaturatedFat() != null) {
                totalSaturatedFat = totalSaturatedFat.add(ingredientNutrition.getSaturatedFat().multiply(quantityRatio));
            }
            if (ingredientNutrition.getTransFat() != null) {
                totalTransFat = totalTransFat.add(ingredientNutrition.getTransFat().multiply(quantityRatio));
            }
            if (ingredientNutrition.getVitaminA() != null) {
                totalVitaminA = totalVitaminA.add(ingredientNutrition.getVitaminA().multiply(quantityRatio));
            }
            if (ingredientNutrition.getVitaminC() != null) {
                totalVitaminC = totalVitaminC.add(ingredientNutrition.getVitaminC().multiply(quantityRatio));
            }
            if (ingredientNutrition.getVitaminD() != null) {
                totalVitaminD = totalVitaminD.add(ingredientNutrition.getVitaminD().multiply(quantityRatio));
            }
            if (ingredientNutrition.getCalcium() != null) {
                totalCalcium = totalCalcium.add(ingredientNutrition.getCalcium().multiply(quantityRatio));
            }
            if (ingredientNutrition.getIron() != null) {
                totalIron = totalIron.add(ingredientNutrition.getIron().multiply(quantityRatio));
            }
            if (ingredientNutrition.getPotassium() != null) {
                totalPotassium = totalPotassium.add(ingredientNutrition.getPotassium().multiply(quantityRatio));
            }
        }

        return NutritionalInfo.builder()
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .calories(totalCalories.setScale(2, RoundingMode.HALF_UP))
            .protein(totalProtein.setScale(2, RoundingMode.HALF_UP))
            .carbohydrates(totalCarbs.setScale(2, RoundingMode.HALF_UP))
            .fat(totalFat.setScale(2, RoundingMode.HALF_UP))
            .fiber(totalFiber.setScale(2, RoundingMode.HALF_UP))
            .sugar(totalSugar.setScale(2, RoundingMode.HALF_UP))
            .sodium(totalSodium.setScale(2, RoundingMode.HALF_UP))
            .cholesterol(totalCholesterol.setScale(2, RoundingMode.HALF_UP))
            .saturatedFat(totalSaturatedFat.setScale(2, RoundingMode.HALF_UP))
            .transFat(totalTransFat.setScale(2, RoundingMode.HALF_UP))
            .vitaminA(totalVitaminA.setScale(2, RoundingMode.HALF_UP))
            .vitaminC(totalVitaminC.setScale(2, RoundingMode.HALF_UP))
            .vitaminD(totalVitaminD.setScale(2, RoundingMode.HALF_UP))
            .calcium(totalCalcium.setScale(2, RoundingMode.HALF_UP))
            .iron(totalIron.setScale(2, RoundingMode.HALF_UP))
            .potassium(totalPotassium.setScale(2, RoundingMode.HALF_UP))
            .notes("Calculated from ingredients")
            .build();
    }

    /**
     * Calculates daily nutrition from multiple meals/recipes
     *
     * @param recipes List of recipes for the day
     * @return NutritionalInfo containing total daily nutrition
     */
    public NutritionalInfo calculateDailyNutrition(List<Recipe> recipes) {
        log.debug("Calculating daily nutrition for {} meals", recipes.size());

        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbs = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        BigDecimal totalFiber = BigDecimal.ZERO;

        for (Recipe recipe : recipes) {
            totalCalories = totalCalories.add(recipe.getCaloriesPerServing());
            totalProtein = totalProtein.add(recipe.getProteinPerServing());
            totalCarbs = totalCarbs.add(recipe.getCarbsPerServing());

            if (recipe.getFat() != null) {
                BigDecimal fatPerServing = recipe.getFat().divide(
                    new BigDecimal(recipe.getServings()),
                    2,
                    RoundingMode.HALF_UP
                );
                totalFat = totalFat.add(fatPerServing);
            }

            if (recipe.getFiber() != null) {
                BigDecimal fiberPerServing = recipe.getFiber().divide(
                    new BigDecimal(recipe.getServings()),
                    2,
                    RoundingMode.HALF_UP
                );
                totalFiber = totalFiber.add(fiberPerServing);
            }
        }

        return NutritionalInfo.builder()
            .servingSize(BigDecimal.ONE)
            .servingUnit(Ingredient.Unit.PIECE)
            .calories(totalCalories.setScale(2, RoundingMode.HALF_UP))
            .protein(totalProtein.setScale(2, RoundingMode.HALF_UP))
            .carbohydrates(totalCarbs.setScale(2, RoundingMode.HALF_UP))
            .fat(totalFat.setScale(2, RoundingMode.HALF_UP))
            .fiber(totalFiber.setScale(2, RoundingMode.HALF_UP))
            .notes("Daily nutrition summary")
            .build();
    }

    /**
     * Validates if daily nutrition meets requirements:
     * - Protein: 20-30g
     * - Carbohydrates: 50-80g
     *
     * @param dailyNutrition Daily nutritional info
     * @return true if within range, false otherwise
     */
    public boolean validateDailyNutrition(NutritionalInfo dailyNutrition) {
        BigDecimal protein = dailyNutrition.getProtein();
        BigDecimal carbs = dailyNutrition.getCarbohydrates();

        boolean proteinValid = protein.compareTo(DAILY_PROTEIN_MIN) >= 0
            && protein.compareTo(DAILY_PROTEIN_MAX) <= 0;

        boolean carbsValid = carbs.compareTo(DAILY_CARB_MIN) >= 0
            && carbs.compareTo(DAILY_CARB_MAX) <= 0;

        log.debug("Nutrition validation - Protein: {}g (valid: {}), Carbs: {}g (valid: {})",
            protein, proteinValid, carbs, carbsValid);

        return proteinValid && carbsValid;
    }

    /**
     * Calculates nutrition balance score for daily meals
     * Returns 100 if perfect, lower scores for deviations
     *
     * @param dailyNutrition Daily nutritional info
     * @return Balance score from 0 to 100
     */
    public double calculateNutritionBalanceScore(NutritionalInfo dailyNutrition) {
        BigDecimal protein = dailyNutrition.getProtein();
        BigDecimal carbs = dailyNutrition.getCarbohydrates();

        double proteinScore = calculateNutrientScore(
            protein,
            DAILY_PROTEIN_MIN,
            DAILY_PROTEIN_MAX
        );

        double carbScore = calculateNutrientScore(
            carbs,
            DAILY_CARB_MIN,
            DAILY_CARB_MAX
        );

        double totalScore = (proteinScore + carbScore) / 2.0;

        log.debug("Balance score - Protein: {}, Carbs: {}, Total: {}",
            proteinScore, carbScore, totalScore);

        return Math.round(totalScore * 100.0) / 100.0;
    }

    /**
     * Calculates score for a single nutrient based on min/max range
     */
    private double calculateNutrientScore(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value.compareTo(min) >= 0 && value.compareTo(max) <= 0) {
            return 100.0;
        } else if (value.compareTo(min) < 0) {
            return value.divide(min, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
        } else {
            return max.divide(value, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
        }
    }

    /**
     * Calculates the ratio between recipe quantity and serving size
     * Handles unit conversions
     */
    private BigDecimal calculateQuantityRatio(
            BigDecimal recipeQuantity,
            Ingredient.Unit recipeUnit,
            BigDecimal servingSize,
            Ingredient.Unit servingUnit) {

        BigDecimal recipeInGrams = convertToGrams(recipeQuantity, recipeUnit);
        BigDecimal servingInGrams = convertToGrams(servingSize, servingUnit);

        if (servingInGrams.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("Serving size is zero, using 1:1 ratio");
            return BigDecimal.ONE;
        }

        return recipeInGrams.divide(servingInGrams, 4, RoundingMode.HALF_UP);
    }

    /**
     * Converts various units to grams for comparison
     * Simplified conversion - assumes standard densities
     */
    private BigDecimal convertToGrams(BigDecimal quantity, Ingredient.Unit unit) {
        return switch (unit) {
            case GRAM -> quantity;
            case KILOGRAM -> quantity.multiply(new BigDecimal("1000"));
            case MILLILITER -> quantity;
            case LITER -> quantity.multiply(new BigDecimal("1000"));
            case PIECE -> quantity.multiply(new BigDecimal("100"));
            case TABLESPOON -> quantity.multiply(new BigDecimal("15"));
            case TEASPOON -> quantity.multiply(new BigDecimal("5"));
            case CUP -> quantity.multiply(new BigDecimal("240"));
            case OUNCE -> quantity.multiply(new BigDecimal("28.35"));
            case POUND -> quantity.multiply(new BigDecimal("453.59"));
        };
    }

    /**
     * Calculates calories from macronutrients
     *
     * @param protein Protein in grams
     * @param carbs Carbohydrates in grams
     * @param fat Fat in grams
     * @return Total calories
     */
    public BigDecimal calculateCaloriesFromMacros(BigDecimal protein, BigDecimal carbs, BigDecimal fat) {
        BigDecimal proteinCalories = protein.multiply(CALORIES_PER_GRAM_PROTEIN);
        BigDecimal carbCalories = carbs.multiply(CALORIES_PER_GRAM_CARB);
        BigDecimal fatCalories = fat.multiply(CALORIES_PER_GRAM_FAT);

        return proteinCalories.add(carbCalories).add(fatCalories)
            .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Gets nutritional info for a specific ingredient
     *
     * @param ingredientId Ingredient ID
     * @return NutritionalInfo or null if not found
     */
    public NutritionalInfo getIngredientNutrition(Long ingredientId) {
        return nutritionalInfoRepository.findByIngredientId(ingredientId).orElse(null);
    }

    /**
     * Gets nutritional info for a specific meal
     *
     * @param mealId Meal ID
     * @return NutritionalInfo or null if not found
     */
    public NutritionalInfo getMealNutrition(Long mealId) {
        return nutritionalInfoRepository.findByMealId(mealId).orElse(null);
    }
}
