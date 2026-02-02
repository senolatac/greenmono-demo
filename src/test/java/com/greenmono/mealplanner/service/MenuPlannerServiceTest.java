package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.MenuPlanRequest;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.entity.*;
import com.greenmono.mealplanner.repository.IngredientRepository;
import com.greenmono.mealplanner.repository.MenuPlanRepository;
import com.greenmono.mealplanner.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuPlannerServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private MenuPlanRepository menuPlanRepository;

    @Mock
    private MenuPlanService menuPlanService;

    @InjectMocks
    private MenuPlannerService menuPlannerService;

    private Long userId;
    private LocalDate startDate;
    private List<Ingredient> availableIngredients;
    private List<Recipe> feasibleRecipes;

    @BeforeEach
    void setUp() {
        userId = 1L;
        startDate = LocalDate.now();
        setupTestData();
    }

    private void setupTestData() {
        // Create test ingredients
        availableIngredients = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Ingredient ingredient = Ingredient.builder()
                .id((long) i)
                .name("Ingredient " + i)
                .available(true)
                .userId(userId)
                .build();
            availableIngredients.add(ingredient);
        }

        // Create test recipes
        feasibleRecipes = new ArrayList<>();

        // Breakfast recipes
        for (int i = 1; i <= 3; i++) {
            Recipe breakfast = createRecipe(
                (long) i,
                "Breakfast " + i,
                Recipe.RecipeCategory.BREAKFAST,
                new BigDecimal("550"),
                new BigDecimal("20"),
                new BigDecimal("60"),
                new BigDecimal("15")
            );
            feasibleRecipes.add(breakfast);
        }

        // Lunch recipes (MAIN_COURSE and SOUP)
        for (int i = 4; i <= 7; i++) {
            Recipe.RecipeCategory category = (i % 2 == 0)
                ? Recipe.RecipeCategory.MAIN_COURSE
                : Recipe.RecipeCategory.SOUP;
            Recipe lunch = createRecipe(
                (long) i,
                "Lunch " + i,
                category,
                new BigDecimal("600"),
                new BigDecimal("30"),
                new BigDecimal("70"),
                new BigDecimal("18")
            );
            feasibleRecipes.add(lunch);
        }

        // Dinner recipes (MAIN_COURSE)
        for (int i = 8; i <= 12; i++) {
            Recipe dinner = createRecipe(
                (long) i,
                "Dinner " + i,
                Recipe.RecipeCategory.MAIN_COURSE,
                new BigDecimal("650"),
                new BigDecimal("35"),
                new BigDecimal("65"),
                new BigDecimal("20")
            );
            feasibleRecipes.add(dinner);
        }
    }

    private Recipe createRecipe(
            Long id,
            String name,
            Recipe.RecipeCategory category,
            BigDecimal calories,
            BigDecimal protein,
            BigDecimal carbs,
            BigDecimal fat) {

        Recipe recipe = Recipe.builder()
            .id(id)
            .name(name)
            .category(category)
            .calories(calories)
            .protein(protein)
            .carbohydrates(carbs)
            .fat(fat)
            .servings(1)
            .cookingTimeMinutes(30)
            .active(true)
            .userId(userId)
            .recipeIngredients(new ArrayList<>())
            .build();

        // Add ingredient associations
        for (int i = 0; i < 3; i++) {
            RecipeIngredient ri = RecipeIngredient.builder()
                .id((long) (id * 10 + i))
                .recipe(recipe)
                .ingredient(availableIngredients.get(i))
                .quantity(new BigDecimal("100"))
                .unit(Ingredient.Unit.GRAM)
                .build();
            recipe.getRecipeIngredients().add(ri);
        }

        return recipe;
    }

    @Test
    void generateBalancedMenuPlan_Success() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .notes("Test menu plan")
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        Page<Recipe> recipePage = new PageImpl<>(feasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        MenuPlan savedMenuPlan = MenuPlan.builder()
            .id(1L)
            .name("5-Day Balanced Menu Plan")
            .userId(userId)
            .startDate(startDate)
            .endDate(startDate.plusDays(4))
            .status(MenuPlan.MenuPlanStatus.DRAFT)
            .isBalanced(true)
            .balanceScore(85.0)
            .build();

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(savedMenuPlan);

        MenuPlanResponse mockResponse = MenuPlanResponse.builder()
            .id(1L)
            .name("5-Day Balanced Menu Plan")
            .build();

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(mockResponse);

        // Act
        MenuPlanResponse response = menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);

        // Verify repository interactions
        verify(ingredientRepository).findAvailableIngredientsForUser(eq(userId), any(LocalDate.class));
        verify(recipeRepository).findByUserIdAndActiveTrue(eq(userId), any(Pageable.class));
        verify(menuPlanRepository).save(any(MenuPlan.class));
        verify(menuPlanService).convertToResponse(any(MenuPlan.class));

        // Capture and verify saved menu plan
        ArgumentCaptor<MenuPlan> menuPlanCaptor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(menuPlanCaptor.capture());

        MenuPlan capturedPlan = menuPlanCaptor.getValue();
        assertThat(capturedPlan.getName()).isEqualTo("5-Day Balanced Menu Plan");
        assertThat(capturedPlan.getUserId()).isEqualTo(userId);
        assertThat(capturedPlan.getStartDate()).isEqualTo(startDate);
        assertThat(capturedPlan.getEndDate()).isEqualTo(startDate.plusDays(4));
        assertThat(capturedPlan.getDailyMealPlans()).hasSize(5);
        assertThat(capturedPlan.getBalanceScore()).isNotNull();
        assertThat(capturedPlan.getIsBalanced()).isNotNull();
    }

    @Test
    void generateBalancedMenuPlan_NoAvailableIngredients_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No available ingredients found for user");

        verify(ingredientRepository).findAvailableIngredientsForUser(eq(userId), any(LocalDate.class));
        verify(recipeRepository, never()).findByUserIdAndActiveTrue(any(), any());
        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_NoFeasibleRecipes_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        // Create recipes with ingredients NOT in available list
        List<Recipe> unfeasibleRecipes = new ArrayList<>();
        Recipe recipe = Recipe.builder()
            .id(100L)
            .name("Unfeasible Recipe")
            .category(Recipe.RecipeCategory.BREAKFAST)
            .calories(new BigDecimal("600"))
            .protein(new BigDecimal("25"))
            .carbohydrates(new BigDecimal("70"))
            .servings(1)
            .active(true)
            .userId(userId)
            .recipeIngredients(new ArrayList<>())
            .build();

        // Add unavailable ingredient
        Ingredient unavailableIngredient = Ingredient.builder()
            .id(999L)
            .name("Unavailable Ingredient")
            .build();

        RecipeIngredient ri = RecipeIngredient.builder()
            .id(1L)
            .recipe(recipe)
            .ingredient(unavailableIngredient)
            .quantity(new BigDecimal("100"))
            .build();
        recipe.getRecipeIngredients().add(ri);
        unfeasibleRecipes.add(recipe);

        Page<Recipe> recipePage = new PageImpl<>(unfeasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No feasible recipes found with available ingredients");

        verify(ingredientRepository).findAvailableIngredientsForUser(eq(userId), any(LocalDate.class));
        verify(recipeRepository).findByUserIdAndActiveTrue(eq(userId), any(Pageable.class));
        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_InsufficientRecipeCategories_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        // Only breakfast recipes, no lunch/dinner
        List<Recipe> limitedRecipes = new ArrayList<>();
        limitedRecipes.add(feasibleRecipes.get(0)); // Only one breakfast recipe

        Page<Recipe> recipePage = new PageImpl<>(limitedRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Insufficient recipes in required categories");

        verify(ingredientRepository).findAvailableIngredientsForUser(eq(userId), any(LocalDate.class));
        verify(recipeRepository).findByUserIdAndActiveTrue(eq(userId), any(Pageable.class));
        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_VerifyDailyMealPlansCreated() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        Page<Recipe> recipePage = new PageImpl<>(feasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        MenuPlan savedMenuPlan = MenuPlan.builder()
            .id(1L)
            .userId(userId)
            .build();

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(savedMenuPlan);

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> menuPlanCaptor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(menuPlanCaptor.capture());

        MenuPlan capturedPlan = menuPlanCaptor.getValue();
        Set<DailyMealPlan> dailyPlans = capturedPlan.getDailyMealPlans();

        assertThat(dailyPlans).hasSize(5);

        // Verify each day has breakfast, lunch, and dinner
        dailyPlans.forEach(dailyPlan -> {
            assertThat(dailyPlan.getBreakfastRecipe()).isNotNull();
            assertThat(dailyPlan.getLunchRecipe()).isNotNull();
            assertThat(dailyPlan.getDinnerRecipe()).isNotNull();
            assertThat(dailyPlan.getTotalCalories()).isNotNull();
            assertThat(dailyPlan.getTotalCalories()).isGreaterThan(0);
        });
    }

    @Test
    void generateBalancedMenuPlan_VerifyNoConsecutiveDayRepetition() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        Page<Recipe> recipePage = new PageImpl<>(feasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        MenuPlan savedMenuPlan = MenuPlan.builder()
            .id(1L)
            .userId(userId)
            .build();

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(savedMenuPlan);

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> menuPlanCaptor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(menuPlanCaptor.capture());

        MenuPlan capturedPlan = menuPlanCaptor.getValue();
        List<DailyMealPlan> sortedDailyPlans = capturedPlan.getDailyMealPlans().stream()
            .sorted(Comparator.comparing(DailyMealPlan::getDayNumber))
            .toList();

        // Check no recipe is used on consecutive days
        for (int i = 0; i < sortedDailyPlans.size() - 1; i++) {
            DailyMealPlan today = sortedDailyPlans.get(i);
            DailyMealPlan tomorrow = sortedDailyPlans.get(i + 1);

            Set<Long> todayRecipeIds = new HashSet<>();
            todayRecipeIds.add(today.getBreakfastRecipe().getId());
            todayRecipeIds.add(today.getLunchRecipe().getId());
            todayRecipeIds.add(today.getDinnerRecipe().getId());

            // Verify tomorrow's recipes are different from today
            assertThat(todayRecipeIds).doesNotContain(tomorrow.getBreakfastRecipe().getId());
            assertThat(todayRecipeIds).doesNotContain(tomorrow.getLunchRecipe().getId());
            assertThat(todayRecipeIds).doesNotContain(tomorrow.getDinnerRecipe().getId());
        }
    }

    @Test
    void generateBalancedMenuPlan_VerifyBalanceScoreCalculated() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        Page<Recipe> recipePage = new PageImpl<>(feasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        MenuPlan savedMenuPlan = MenuPlan.builder()
            .id(1L)
            .userId(userId)
            .build();

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(savedMenuPlan);

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> menuPlanCaptor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(menuPlanCaptor.capture());

        MenuPlan capturedPlan = menuPlanCaptor.getValue();
        assertThat(capturedPlan.getBalanceScore()).isNotNull();
        assertThat(capturedPlan.getBalanceScore()).isGreaterThanOrEqualTo(0.0);
        assertThat(capturedPlan.getBalanceScore()).isLessThanOrEqualTo(100.0);
        assertThat(capturedPlan.getIsBalanced()).isNotNull();
    }

    @Test
    void generateBalancedMenuPlan_VerifyNutritionMetricsCalculated() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(1800)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .build();

        when(ingredientRepository.findAvailableIngredientsForUser(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        Page<Recipe> recipePage = new PageImpl<>(feasibleRecipes);
        when(recipeRepository.findByUserIdAndActiveTrue(eq(userId), any(Pageable.class)))
            .thenReturn(recipePage);

        MenuPlan savedMenuPlan = MenuPlan.builder()
            .id(1L)
            .userId(userId)
            .build();

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(savedMenuPlan);

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> menuPlanCaptor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(menuPlanCaptor.capture());

        MenuPlan capturedPlan = menuPlanCaptor.getValue();
        assertThat(capturedPlan.getTotalCalories()).isNotNull();
        assertThat(capturedPlan.getAverageDailyCalories()).isNotNull();
        assertThat(capturedPlan.getTotalCalories()).isGreaterThan(0);
        assertThat(capturedPlan.getAverageDailyCalories()).isGreaterThan(0);
    }
}
