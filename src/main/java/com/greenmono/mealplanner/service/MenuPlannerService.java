package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.MenuPlanRequest;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.entity.*;
import com.greenmono.mealplanner.repository.IngredientRepository;
import com.greenmono.mealplanner.repository.MenuPlanRepository;
import com.greenmono.mealplanner.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuPlannerService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MenuPlanRepository menuPlanRepository;
    private final MenuPlanService menuPlanService;

    private static final int PLANNING_DAYS = 5;
    private static final int MEALS_PER_DAY = 3; // Breakfast, Lunch, Dinner
    private static final double MIN_BALANCE_SCORE = 70.0;
    private static final double PROTEIN_MIN_RATIO = 0.20; // 20% of calories
    private static final double PROTEIN_MAX_RATIO = 0.35; // 35% of calories
    private static final double CARB_MIN_RATIO = 0.45; // 45% of calories
    private static final double CARB_MAX_RATIO = 0.65; // 65% of calories

    /**
     * Generates a balanced 5-day menu plan based on available ingredients
     *
     * Algorithm:
     * 1. Fetch available ingredients for user
     * 2. Find all recipes that can be made with available ingredients
     * 3. For each day (5 days):
     *    - Select breakfast (BREAKFAST category, 500-700 kcal)
     *    - Select lunch (MAIN_COURSE/SOUP, 500-700 kcal)
     *    - Select dinner (MAIN_COURSE, 500-700 kcal)
     * 4. Ensure constraints:
     *    - No same recipe used 2 consecutive days
     *    - Daily calorie target met
     *    - Protein-carb balance maintained
     * 5. Calculate balance score and mark as balanced if score > 70
     */
    @Transactional
    public MenuPlanResponse generateBalancedMenuPlan(MenuPlanRequest request) {
        log.info("Generating balanced menu plan for user {}", request.getUserId());

        // Step 1: Get available ingredients
        List<Ingredient> availableIngredients = ingredientRepository
            .findAvailableIngredientsForUser(request.getUserId(), LocalDate.now());

        if (availableIngredients.isEmpty()) {
            throw new IllegalStateException("No available ingredients found for user");
        }

        Set<Long> availableIngredientIds = availableIngredients.stream()
            .map(Ingredient::getId)
            .collect(Collectors.toSet());

        log.info("Found {} available ingredients", availableIngredients.size());

        // Step 2: Find recipes that can be made with available ingredients
        List<Recipe> allRecipes = recipeRepository.findByUserIdAndActiveTrue(
            request.getUserId(),
            org.springframework.data.domain.Pageable.unpaged()
        ).getContent();

        List<Recipe> feasibleRecipes = filterFeasibleRecipes(allRecipes, availableIngredientIds);

        if (feasibleRecipes.isEmpty()) {
            throw new IllegalStateException("No feasible recipes found with available ingredients");
        }

        log.info("Found {} feasible recipes out of {} total recipes",
            feasibleRecipes.size(), allRecipes.size());

        // Step 3: Create menu plan entity
        MenuPlan menuPlan = MenuPlan.builder()
            .name("5-Day Balanced Menu Plan")
            .description("Automatically generated balanced menu plan based on available ingredients")
            .userId(request.getUserId())
            .startDate(request.getStartDate())
            .endDate(request.getStartDate().plusDays(PLANNING_DAYS - 1))
            .status(MenuPlan.MenuPlanStatus.DRAFT)
            .notes(request.getNotes())
            .isBalanced(false)
            .build();

        // Step 4: Generate daily meal plans
        List<DailyMealPlan> dailyPlans = generateDailyMealPlans(
            menuPlan,
            feasibleRecipes,
            request
        );

        menuPlan.setDailyMealPlans(new HashSet<>(dailyPlans));

        // Step 5: Calculate nutrition metrics and balance score
        calculateNutritionMetrics(menuPlan);
        double balanceScore = calculateBalanceScore(menuPlan);
        menuPlan.setBalanceScore(balanceScore);
        menuPlan.setIsBalanced(balanceScore >= MIN_BALANCE_SCORE);

        // Step 6: Save and return
        MenuPlan savedPlan = menuPlanRepository.save(menuPlan);
        log.info("Menu plan created with balance score: {}", balanceScore);

        return menuPlanService.convertToResponse(savedPlan);
    }

    /**
     * Filters recipes that can be made with available ingredients
     */
    private List<Recipe> filterFeasibleRecipes(List<Recipe> recipes, Set<Long> availableIngredientIds) {
        return recipes.stream()
            .filter(recipe -> {
                Set<Long> requiredIngredientIds = recipe.getRecipeIngredients().stream()
                    .map(ri -> ri.getIngredient().getId())
                    .collect(Collectors.toSet());

                // Recipe is feasible if all required ingredients are available
                return availableIngredientIds.containsAll(requiredIngredientIds);
            })
            .collect(Collectors.toList());
    }

    /**
     * Generates daily meal plans for all 5 days
     */
    private List<DailyMealPlan> generateDailyMealPlans(
            MenuPlan menuPlan,
            List<Recipe> feasibleRecipes,
            MenuPlanRequest request) {

        List<DailyMealPlan> dailyPlans = new ArrayList<>();
        Set<Long> usedRecipeIdsYesterday = new HashSet<>();

        // Categorize recipes by meal type
        List<Recipe> breakfastRecipes = filterRecipesByCategory(feasibleRecipes, Recipe.RecipeCategory.BREAKFAST);
        List<Recipe> lunchRecipes = filterRecipesByCategories(feasibleRecipes,
            Arrays.asList(Recipe.RecipeCategory.MAIN_COURSE, Recipe.RecipeCategory.SOUP));
        List<Recipe> dinnerRecipes = filterRecipesByCategory(feasibleRecipes, Recipe.RecipeCategory.MAIN_COURSE);

        if (breakfastRecipes.isEmpty() || lunchRecipes.isEmpty() || dinnerRecipes.isEmpty()) {
            throw new IllegalStateException(
                "Insufficient recipes in required categories. Breakfast: " + breakfastRecipes.size() +
                ", Lunch: " + lunchRecipes.size() +
                ", Dinner: " + dinnerRecipes.size()
            );
        }

        for (int day = 1; day <= PLANNING_DAYS; day++) {
            LocalDate mealDate = request.getStartDate().plusDays(day - 1);

            DailyMealPlan dailyPlan = DailyMealPlan.builder()
                .menuPlan(menuPlan)
                .dayNumber(day)
                .mealDate(mealDate)
                .build();

            // Select meals ensuring no ingredient repetition from previous day
            Recipe breakfast = selectRecipe(breakfastRecipes, usedRecipeIdsYesterday, request);
            Recipe lunch = selectRecipe(lunchRecipes, usedRecipeIdsYesterday, request);
            Recipe dinner = selectRecipe(dinnerRecipes, usedRecipeIdsYesterday, request);

            dailyPlan.setBreakfastRecipe(breakfast);
            dailyPlan.setLunchRecipe(lunch);
            dailyPlan.setDinnerRecipe(dinner);

            // Calculate total calories for the day
            int totalCalories = calculateDailyCalories(breakfast, lunch, dinner);
            dailyPlan.setTotalCalories(totalCalories);

            dailyPlans.add(dailyPlan);

            // Update used recipes for next iteration
            usedRecipeIdsYesterday.clear();
            usedRecipeIdsYesterday.add(breakfast.getId());
            usedRecipeIdsYesterday.add(lunch.getId());
            usedRecipeIdsYesterday.add(dinner.getId());

            log.debug("Day {}: Breakfast={}, Lunch={}, Dinner={}, Total Calories={}",
                day, breakfast.getName(), lunch.getName(), dinner.getName(), totalCalories);
        }

        return dailyPlans;
    }

    /**
     * Selects a recipe that meets calorie requirements and wasn't used yesterday
     */
    private Recipe selectRecipe(
            List<Recipe> candidateRecipes,
            Set<Long> usedRecipeIdsYesterday,
            MenuPlanRequest request) {

        // Filter recipes not used yesterday
        List<Recipe> availableRecipes = candidateRecipes.stream()
            .filter(r -> !usedRecipeIdsYesterday.contains(r.getId()))
            .collect(Collectors.toList());

        if (availableRecipes.isEmpty()) {
            // Fallback: allow repetition if no other option
            availableRecipes = candidateRecipes;
        }

        // Filter by calorie range
        List<Recipe> calorieFilteredRecipes = availableRecipes.stream()
            .filter(r -> {
                BigDecimal caloriesPerServing = r.getCaloriesPerServing();
                int calories = caloriesPerServing.intValue();
                return calories >= request.getCaloriesPerMealMin() &&
                       calories <= request.getCaloriesPerMealMax();
            })
            .collect(Collectors.toList());

        if (!calorieFilteredRecipes.isEmpty()) {
            availableRecipes = calorieFilteredRecipes;
        }

        // Select recipe with best balance score
        return availableRecipes.stream()
            .max(Comparator.comparingDouble(this::calculateRecipeBalanceScore))
            .orElse(availableRecipes.get(0));
    }

    /**
     * Calculates balance score for a single recipe (protein-carb ratio)
     */
    private double calculateRecipeBalanceScore(Recipe recipe) {
        BigDecimal protein = recipe.getProtein();
        BigDecimal carbs = recipe.getCarbohydrates();
        BigDecimal calories = recipe.getCalories();

        if (calories.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        // Calculate macronutrient ratios
        // Protein: 4 kcal/g, Carbs: 4 kcal/g
        double proteinCalories = protein.multiply(new BigDecimal("4")).doubleValue();
        double carbCalories = carbs.multiply(new BigDecimal("4")).doubleValue();
        double totalCalories = calories.doubleValue();

        double proteinRatio = proteinCalories / totalCalories;
        double carbRatio = carbCalories / totalCalories;

        // Score based on how close to ideal ratios
        double proteinScore = 100.0;
        if (proteinRatio < PROTEIN_MIN_RATIO) {
            proteinScore = (proteinRatio / PROTEIN_MIN_RATIO) * 100.0;
        } else if (proteinRatio > PROTEIN_MAX_RATIO) {
            proteinScore = (PROTEIN_MAX_RATIO / proteinRatio) * 100.0;
        }

        double carbScore = 100.0;
        if (carbRatio < CARB_MIN_RATIO) {
            carbScore = (carbRatio / CARB_MIN_RATIO) * 100.0;
        } else if (carbRatio > CARB_MAX_RATIO) {
            carbScore = (CARB_MAX_RATIO / carbRatio) * 100.0;
        }

        return (proteinScore + carbScore) / 2.0;
    }

    /**
     * Calculates total daily calories from 3 meals
     */
    private int calculateDailyCalories(Recipe breakfast, Recipe lunch, Recipe dinner) {
        return breakfast.getCaloriesPerServing().intValue() +
               lunch.getCaloriesPerServing().intValue() +
               dinner.getCaloriesPerServing().intValue();
    }

    /**
     * Calculates and sets total/average calories for the menu plan
     */
    private void calculateNutritionMetrics(MenuPlan menuPlan) {
        int totalCalories = menuPlan.getDailyMealPlans().stream()
            .mapToInt(dmp -> dmp.getTotalCalories() != null ? dmp.getTotalCalories() : 0)
            .sum();

        int days = menuPlan.getDailyMealPlans().size();
        int averageDailyCalories = days > 0 ? totalCalories / days : 0;

        menuPlan.setTotalCalories(totalCalories);
        menuPlan.setAverageDailyCalories(averageDailyCalories);
    }

    /**
     * Calculates overall balance score for the menu plan
     *
     * Scoring criteria:
     * 1. Protein-carb balance across all days (40%)
     * 2. Calorie consistency across days (30%)
     * 3. Recipe variety (no repetition penalty) (30%)
     */
    private double calculateBalanceScore(MenuPlan menuPlan) {
        List<DailyMealPlan> dailyPlans = new ArrayList<>(menuPlan.getDailyMealPlans());

        if (dailyPlans.isEmpty()) {
            return 0.0;
        }

        // 1. Macro balance score (40%)
        double macroScore = calculateMacroBalanceScore(dailyPlans);

        // 2. Calorie consistency score (30%)
        double calorieScore = calculateCalorieConsistencyScore(dailyPlans);

        // 3. Variety score (30%)
        double varietyScore = calculateVarietyScore(dailyPlans);

        double totalScore = (macroScore * 0.4) + (calorieScore * 0.3) + (varietyScore * 0.3);

        log.debug("Balance scores - Macro: {}, Calorie: {}, Variety: {}, Total: {}",
            macroScore, calorieScore, varietyScore, totalScore);

        return Math.round(totalScore * 100.0) / 100.0;
    }

    /**
     * Calculates macro balance score across all days
     */
    private double calculateMacroBalanceScore(List<DailyMealPlan> dailyPlans) {
        double totalScore = 0.0;
        int count = 0;

        for (DailyMealPlan dailyPlan : dailyPlans) {
            List<Recipe> recipes = Arrays.asList(
                dailyPlan.getBreakfastRecipe(),
                dailyPlan.getLunchRecipe(),
                dailyPlan.getDinnerRecipe()
            );

            double dayScore = recipes.stream()
                .filter(Objects::nonNull)
                .mapToDouble(this::calculateRecipeBalanceScore)
                .average()
                .orElse(0.0);

            totalScore += dayScore;
            count++;
        }

        return count > 0 ? totalScore / count : 0.0;
    }

    /**
     * Calculates calorie consistency score (lower variance = higher score)
     */
    private double calculateCalorieConsistencyScore(List<DailyMealPlan> dailyPlans) {
        List<Integer> dailyCalories = dailyPlans.stream()
            .map(DailyMealPlan::getTotalCalories)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (dailyCalories.size() < 2) {
            return 100.0;
        }

        double mean = dailyCalories.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);

        double variance = dailyCalories.stream()
            .mapToDouble(cal -> Math.pow(cal - mean, 2))
            .average()
            .orElse(0.0);

        double stdDev = Math.sqrt(variance);
        double coefficientOfVariation = mean > 0 ? (stdDev / mean) * 100.0 : 0.0;

        // Lower CV = higher score (ideal CV < 10%)
        if (coefficientOfVariation < 10.0) {
            return 100.0;
        } else if (coefficientOfVariation < 20.0) {
            return 90.0;
        } else if (coefficientOfVariation < 30.0) {
            return 75.0;
        } else {
            return 50.0;
        }
    }

    /**
     * Calculates variety score (fewer repetitions = higher score)
     */
    private double calculateVarietyScore(List<DailyMealPlan> dailyPlans) {
        Set<Long> allRecipeIds = new HashSet<>();
        int totalMeals = 0;

        for (DailyMealPlan dailyPlan : dailyPlans) {
            if (dailyPlan.getBreakfastRecipe() != null) {
                allRecipeIds.add(dailyPlan.getBreakfastRecipe().getId());
                totalMeals++;
            }
            if (dailyPlan.getLunchRecipe() != null) {
                allRecipeIds.add(dailyPlan.getLunchRecipe().getId());
                totalMeals++;
            }
            if (dailyPlan.getDinnerRecipe() != null) {
                allRecipeIds.add(dailyPlan.getDinnerRecipe().getId());
                totalMeals++;
            }
        }

        if (totalMeals == 0) {
            return 0.0;
        }

        // Variety ratio: unique recipes / total meals
        double varietyRatio = (double) allRecipeIds.size() / totalMeals;
        return varietyRatio * 100.0;
    }

    private List<Recipe> filterRecipesByCategory(List<Recipe> recipes, Recipe.RecipeCategory category) {
        return recipes.stream()
            .filter(r -> r.getCategory() == category)
            .collect(Collectors.toList());
    }

    private List<Recipe> filterRecipesByCategories(List<Recipe> recipes, List<Recipe.RecipeCategory> categories) {
        return recipes.stream()
            .filter(r -> categories.contains(r.getCategory()))
            .collect(Collectors.toList());
    }
}
