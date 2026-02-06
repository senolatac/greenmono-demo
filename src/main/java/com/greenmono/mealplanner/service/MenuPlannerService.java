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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
    private final NutritionCalculatorService nutritionCalculatorService;

    private static final int PLANNING_DAYS = 5;

    /**
     * Generates a 5-day (Mon-Fri) menu plan with 3-component lunches:
     * soup + main course + side dish.
     *
     * Algorithm:
     * 1. Fetch all active recipes and categorize by SOUP / MAIN_COURSE / SIDE_DISH
     * 2. Validate at least 1 recipe per category exists
     * 3. Snap start date to Monday (if Sat/Sun, move to next Monday)
     * 4. For 5 days (Mon-Fri): select 1 soup + 1 main course + 1 side dish
     * 5. No same recipe on consecutive days (per category)
     * 6. Total daily calories = sum of 3 components
     * 7. Uses Collections.shuffle() for randomized selection
     */
    @Transactional
    public MenuPlanResponse generateBalancedMenuPlan(MenuPlanRequest request) {
        log.info("Generating 3-component menu plan for user {}", request.getUserId());

        // Step 1: Find all active recipes
        List<Recipe> allRecipes = recipeRepository.findByActiveTrue(
            org.springframework.data.domain.Pageable.unpaged()
        ).getContent();

        if (allRecipes.isEmpty()) {
            throw new IllegalStateException("No active recipes found");
        }

        // Step 2: Fetch available ingredients (user-specific or global)
        List<Ingredient> availableIngredients = ingredientRepository
            .findAvailableIngredientsForUserOrGlobal(request.getUserId(), LocalDate.now());

        if (availableIngredients.isEmpty()) {
            throw new IllegalStateException("No available ingredients found");
        }

        Map<Long, Ingredient> availableById = availableIngredients.stream()
            .filter(i -> i.getId() != null)
            .collect(Collectors.toMap(Ingredient::getId, i -> i, (a, b) -> a));

        // Step 3: Filter recipes by available ingredients
        List<Recipe> eligibleRecipes = allRecipes.stream()
            .filter(recipe -> isRecipeCookable(recipe, availableById))
            .collect(Collectors.toList());

        if (eligibleRecipes.isEmpty()) {
            throw new IllegalStateException("No recipes match available ingredients");
        }

        // Step 4: Categorize recipes
        List<Recipe> soups = filterRecipesByCategory(eligibleRecipes, Recipe.RecipeCategory.SOUP);
        List<Recipe> mainCourses = filterRecipesByCategory(eligibleRecipes, Recipe.RecipeCategory.MAIN_COURSE);
        List<Recipe> sideDishes = filterRecipesByCategory(eligibleRecipes, Recipe.RecipeCategory.SIDE_DISH);

        log.info("Found {} soups, {} main courses, {} side dishes", soups.size(), mainCourses.size(), sideDishes.size());

        if (soups.isEmpty() || mainCourses.isEmpty() || sideDishes.isEmpty()) {
            throw new IllegalStateException(
                String.format("Insufficient recipes. Need at least 1 soup, 1 main course, 1 side dish. Found: %d soups, %d main courses, %d side dishes",
                    soups.size(), mainCourses.size(), sideDishes.size())
            );
        }

        // Step 5: Snap start date to Monday
        LocalDate startDate = snapToMonday(request.getStartDate());

        // Step 6: Create menu plan entity
        MenuPlan menuPlan = MenuPlan.builder()
            .name("5-Day Balanced Menu Plan")
            .description("Automatically generated 3-component lunch menu (soup + main course + side dish)")
            .userId(request.getUserId())
            .startDate(startDate)
            .endDate(startDate.plusDays(PLANNING_DAYS - 1))
            .status(MenuPlan.MenuPlanStatus.DRAFT)
            .notes(request.getNotes())
            .isBalanced(false)
            .build();

        // Step 7: Generate daily meal plans
        List<DailyMealPlan> dailyPlans = generateDailyMealPlans(menuPlan, soups, mainCourses, sideDishes, startDate);
        menuPlan.setDailyMealPlans(new HashSet<>(dailyPlans));

        // Step 8: Calculate nutrition metrics
        calculateNutritionMetrics(menuPlan);
        double balanceScore = calculateBalanceScore(menuPlan);
        menuPlan.setBalanceScore(balanceScore);
        menuPlan.setIsBalanced(balanceScore >= 70.0);

        // Step 9: Save and return
        MenuPlan savedPlan = menuPlanRepository.save(menuPlan);
        log.info("Menu plan created with balance score: {}", balanceScore);

        return menuPlanService.convertToResponse(savedPlan);
    }

    /**
     * Snaps a date to Monday. If the date is Saturday or Sunday, moves to next Monday.
     * Otherwise returns the Monday of the same week.
     */
    LocalDate snapToMonday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Generates daily meal plans for 5 days with no consecutive-day repetition per category.
     */
    private List<DailyMealPlan> generateDailyMealPlans(
            MenuPlan menuPlan,
            List<Recipe> soups,
            List<Recipe> mainCourses,
            List<Recipe> sideDishes,
            LocalDate startDate) {

        List<DailyMealPlan> dailyPlans = new ArrayList<>();

        // Shuffle for randomness
        List<Recipe> shuffledSoups = new ArrayList<>(soups);
        List<Recipe> shuffledMains = new ArrayList<>(mainCourses);
        List<Recipe> shuffledSides = new ArrayList<>(sideDishes);
        Collections.shuffle(shuffledSoups);
        Collections.shuffle(shuffledMains);
        Collections.shuffle(shuffledSides);

        Recipe previousSoup = null;
        Recipe previousMain = null;
        Recipe previousSide = null;

        for (int day = 1; day <= PLANNING_DAYS; day++) {
            LocalDate mealDate = startDate.plusDays(day - 1);

            Recipe soup = selectRecipeAvoidingPrevious(shuffledSoups, previousSoup);
            Recipe main = selectRecipeAvoidingPrevious(shuffledMains, previousMain);
            Recipe side = selectRecipeAvoidingPrevious(shuffledSides, previousSide);

            int totalCalories = soup.getCaloriesPerServing().intValue()
                + main.getCaloriesPerServing().intValue()
                + side.getCaloriesPerServing().intValue();

            DailyMealPlan dailyPlan = DailyMealPlan.builder()
                .menuPlan(menuPlan)
                .dayNumber(day)
                .mealDate(mealDate)
                .soupRecipe(soup)
                .mainCourseRecipe(main)
                .sideDishRecipe(side)
                .totalCalories(totalCalories)
                .build();

            dailyPlans.add(dailyPlan);

            previousSoup = soup;
            previousMain = main;
            previousSide = side;

            log.debug("Day {}: Soup={}, Main={}, Side={}, Calories={}",
                day, soup.getName(), main.getName(), side.getName(), totalCalories);
        }

        return dailyPlans;
    }

    /**
     * Selects a recipe from the list, avoiding the previous day's recipe.
     * Falls back to allowing repetition if only 1 recipe is available.
     */
    private Recipe selectRecipeAvoidingPrevious(List<Recipe> recipes, Recipe previous) {
        if (previous == null || recipes.size() <= 1) {
            return recipes.get(0);
        }

        List<Recipe> candidates = recipes.stream()
            .filter(r -> !r.getId().equals(previous.getId()))
            .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            return recipes.get(0);
        }

        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    private void calculateNutritionMetrics(MenuPlan menuPlan) {
        int totalCalories = menuPlan.getDailyMealPlans().stream()
            .mapToInt(dmp -> dmp.getTotalCalories() != null ? dmp.getTotalCalories() : 0)
            .sum();

        int days = menuPlan.getDailyMealPlans().size();
        int averageDailyCalories = days > 0 ? totalCalories / days : 0;

        menuPlan.setTotalCalories(totalCalories);
        menuPlan.setAverageDailyCalories(averageDailyCalories);
    }

    private double calculateBalanceScore(MenuPlan menuPlan) {
        List<DailyMealPlan> dailyPlans = new ArrayList<>(menuPlan.getDailyMealPlans());

        if (dailyPlans.isEmpty()) {
            return 0.0;
        }

        // Macro balance score (40%)
        double macroScore = calculateMacroBalanceScore(dailyPlans);

        // Calorie consistency score (30%)
        double calorieScore = calculateCalorieConsistencyScore(dailyPlans);

        // Variety score (30%)
        double varietyScore = calculateVarietyScore(dailyPlans);

        double totalScore = (macroScore * 0.4) + (calorieScore * 0.3) + (varietyScore * 0.3);

        return Math.round(totalScore * 100.0) / 100.0;
    }

    private double calculateMacroBalanceScore(List<DailyMealPlan> dailyPlans) {
        double totalScore = 0.0;
        int count = 0;

        for (DailyMealPlan dailyPlan : dailyPlans) {
            List<Recipe> recipes = new ArrayList<>();
            if (dailyPlan.getSoupRecipe() != null) recipes.add(dailyPlan.getSoupRecipe());
            if (dailyPlan.getMainCourseRecipe() != null) recipes.add(dailyPlan.getMainCourseRecipe());
            if (dailyPlan.getSideDishRecipe() != null) recipes.add(dailyPlan.getSideDishRecipe());

            double dayScore = recipes.stream()
                .mapToDouble(this::calculateRecipeBalanceScore)
                .average()
                .orElse(0.0);

            totalScore += dayScore;
            count++;
        }

        return count > 0 ? totalScore / count : 0.0;
    }

    private double calculateRecipeBalanceScore(Recipe recipe) {
        if (recipe.getCalories().compareTo(java.math.BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        double proteinCalories = recipe.getProtein().multiply(new java.math.BigDecimal("4")).doubleValue();
        double carbCalories = recipe.getCarbohydrates().multiply(new java.math.BigDecimal("4")).doubleValue();
        double totalCalories = recipe.getCalories().doubleValue();

        double proteinRatio = proteinCalories / totalCalories;
        double carbRatio = carbCalories / totalCalories;

        double proteinScore = 100.0;
        if (proteinRatio < 0.20) proteinScore = (proteinRatio / 0.20) * 100.0;
        else if (proteinRatio > 0.35) proteinScore = (0.35 / proteinRatio) * 100.0;

        double carbScore = 100.0;
        if (carbRatio < 0.45) carbScore = (carbRatio / 0.45) * 100.0;
        else if (carbRatio > 0.65) carbScore = (0.65 / carbRatio) * 100.0;

        return (proteinScore + carbScore) / 2.0;
    }

    private double calculateCalorieConsistencyScore(List<DailyMealPlan> dailyPlans) {
        List<Integer> dailyCalories = dailyPlans.stream()
            .map(DailyMealPlan::getTotalCalories)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (dailyCalories.size() < 2) return 100.0;

        double mean = dailyCalories.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double variance = dailyCalories.stream().mapToDouble(cal -> Math.pow(cal - mean, 2)).average().orElse(0.0);
        double stdDev = Math.sqrt(variance);
        double cv = mean > 0 ? (stdDev / mean) * 100.0 : 0.0;

        if (cv < 10.0) return 100.0;
        else if (cv < 20.0) return 90.0;
        else if (cv < 30.0) return 75.0;
        else return 50.0;
    }

    private double calculateVarietyScore(List<DailyMealPlan> dailyPlans) {
        Set<Long> allRecipeIds = new HashSet<>();
        int totalMeals = 0;

        for (DailyMealPlan dailyPlan : dailyPlans) {
            if (dailyPlan.getSoupRecipe() != null) {
                allRecipeIds.add(dailyPlan.getSoupRecipe().getId());
                totalMeals++;
            }
            if (dailyPlan.getMainCourseRecipe() != null) {
                allRecipeIds.add(dailyPlan.getMainCourseRecipe().getId());
                totalMeals++;
            }
            if (dailyPlan.getSideDishRecipe() != null) {
                allRecipeIds.add(dailyPlan.getSideDishRecipe().getId());
                totalMeals++;
            }
        }

        if (totalMeals == 0) return 0.0;

        double varietyRatio = (double) allRecipeIds.size() / totalMeals;
        return varietyRatio * 100.0;
    }

    private List<Recipe> filterRecipesByCategory(List<Recipe> recipes, Recipe.RecipeCategory category) {
        return recipes.stream()
            .filter(r -> r.getCategory() == category)
            .collect(Collectors.toList());
    }

    private boolean isRecipeCookable(Recipe recipe, Map<Long, Ingredient> availableById) {
        if (recipe.getRecipeIngredients() == null || recipe.getRecipeIngredients().isEmpty()) {
            return false;
        }

        for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
            Ingredient ingredient = ri.getIngredient() != null ? availableById.get(ri.getIngredient().getId()) : null;

            if (ingredient == null) {
                if (Boolean.TRUE.equals(ri.getOptional())) {
                    continue;
                }
                return false;
            }

            if (ingredient.getUnit() != null && ri.getUnit() != null
                && !ingredient.getUnit().equals(ri.getUnit())) {
                if (Boolean.TRUE.equals(ri.getOptional())) {
                    continue;
                }
                return false;
            }

            if (ingredient.getQuantity() != null && ri.getQuantity() != null
                && ingredient.getQuantity().compareTo(ri.getQuantity()) < 0) {
                if (Boolean.TRUE.equals(ri.getOptional())) {
                    continue;
                }
                return false;
            }
        }

        return true;
    }
}
