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
import java.time.DayOfWeek;
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

    @Mock
    private NutritionCalculatorService nutritionCalculatorService;

    @InjectMocks
    private MenuPlannerService menuPlannerService;

    private Long userId;
    private LocalDate startDate;
    private List<Recipe> allRecipes;
    private Ingredient testIngredient;
    private List<Ingredient> availableIngredients;

    @BeforeEach
    void setUp() {
        userId = 1L;
        // Use a Monday as start date
        startDate = LocalDate.of(2026, 2, 2); // Monday
        testIngredient = Ingredient.builder()
            .id(1L)
            .name("Test Ingredient")
            .category(Ingredient.IngredientCategory.OTHER)
            .quantity(new BigDecimal("1000"))
            .unit(Ingredient.Unit.GRAM)
            .available(true)
            .build();
        availableIngredients = List.of(testIngredient);
        setupTestData();
    }

    private void setupTestData() {
        allRecipes = new ArrayList<>();

        // Soup recipes (5)
        for (int i = 1; i <= 5; i++) {
            allRecipes.add(createRecipe(
                (long) i,
                "Soup " + i,
                Recipe.RecipeCategory.SOUP,
                new BigDecimal("180"),
                new BigDecimal("9"),
                new BigDecimal("28"),
                new BigDecimal("4")
            ));
        }

        // Main course recipes (7)
        for (int i = 6; i <= 12; i++) {
            allRecipes.add(createRecipe(
                (long) i,
                "Main Course " + i,
                Recipe.RecipeCategory.MAIN_COURSE,
                new BigDecimal("450"),
                new BigDecimal("30"),
                new BigDecimal("40"),
                new BigDecimal("18")
            ));
        }

        // Side dish recipes (5)
        for (int i = 13; i <= 17; i++) {
            allRecipes.add(createRecipe(
                (long) i,
                "Side Dish " + i,
                Recipe.RecipeCategory.SIDE_DISH,
                new BigDecimal("250"),
                new BigDecimal("5"),
                new BigDecimal("45"),
                new BigDecimal("6")
            ));
        }
    }

    private Recipe createRecipe(Long id, String name, Recipe.RecipeCategory category,
                                 BigDecimal calories, BigDecimal protein,
                                 BigDecimal carbs, BigDecimal fat) {
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
            .build();

        RecipeIngredient ri = RecipeIngredient.builder()
            .recipe(recipe)
            .ingredient(testIngredient)
            .quantity(new BigDecimal("10"))
            .unit(Ingredient.Unit.GRAM)
            .optional(false)
            .build();
        recipe.setRecipeIngredients(new ArrayList<>(List.of(ri)));

        return recipe;
    }

    @Test
    void generateBalancedMenuPlan_Success() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .caloriesPerMealMin(500)
            .caloriesPerMealMax(700)
            .notes("Test menu plan")
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> {
                MenuPlan mp = inv.getArgument(0);
                mp.setId(1L);
                return mp;
            });

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().id(1L).name("5-Day Balanced Menu Plan").build());

        // Act
        MenuPlanResponse response = menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);

        verify(recipeRepository).findByActiveTrue(any(Pageable.class));
        verify(menuPlanRepository).save(any(MenuPlan.class));
        verify(menuPlanService).convertToResponse(any(MenuPlan.class));

        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        MenuPlan captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("5-Day Balanced Menu Plan");
        assertThat(captured.getUserId()).isEqualTo(userId);
        assertThat(captured.getDailyMealPlans()).hasSize(5);
        assertThat(captured.getBalanceScore()).isNotNull();
        assertThat(captured.getIsBalanced()).isNotNull();
    }

    @Test
    void generateBalancedMenuPlan_NoActiveRecipes_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> emptyPage = new PageImpl<>(Collections.emptyList());
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(emptyPage);

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No active recipes found");

        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_MissingSoupCategory_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        // Only main courses and side dishes, no soups
        List<Recipe> noSoups = allRecipes.stream()
            .filter(r -> r.getCategory() != Recipe.RecipeCategory.SOUP)
            .toList();

        Page<Recipe> recipePage = new PageImpl<>(noSoups);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Insufficient recipes");

        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_MissingSideDishCategory_ThrowsException() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        // Only soups and main courses, no side dishes
        List<Recipe> noSides = allRecipes.stream()
            .filter(r -> r.getCategory() != Recipe.RecipeCategory.SIDE_DISH)
            .toList();

        Page<Recipe> recipePage = new PageImpl<>(noSides);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        // Act & Assert
        assertThatThrownBy(() -> menuPlannerService.generateBalancedMenuPlan(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Insufficient recipes");

        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void generateBalancedMenuPlan_VerifyDailyPlansHave3Components() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        MenuPlan captured = captor.getValue();
        assertThat(captured.getDailyMealPlans()).hasSize(5);

        captured.getDailyMealPlans().forEach(dailyPlan -> {
            assertThat(dailyPlan.getSoupRecipe()).isNotNull();
            assertThat(dailyPlan.getSoupRecipe().getCategory()).isEqualTo(Recipe.RecipeCategory.SOUP);
            assertThat(dailyPlan.getMainCourseRecipe()).isNotNull();
            assertThat(dailyPlan.getMainCourseRecipe().getCategory()).isEqualTo(Recipe.RecipeCategory.MAIN_COURSE);
            assertThat(dailyPlan.getSideDishRecipe()).isNotNull();
            assertThat(dailyPlan.getSideDishRecipe().getCategory()).isEqualTo(Recipe.RecipeCategory.SIDE_DISH);
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
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        List<DailyMealPlan> sorted = captor.getValue().getDailyMealPlans().stream()
            .sorted(Comparator.comparing(DailyMealPlan::getDayNumber))
            .toList();

        for (int i = 0; i < sorted.size() - 1; i++) {
            DailyMealPlan today = sorted.get(i);
            DailyMealPlan tomorrow = sorted.get(i + 1);

            // Same category should not repeat on consecutive days
            assertThat(today.getSoupRecipe().getId())
                .isNotEqualTo(tomorrow.getSoupRecipe().getId());
            assertThat(today.getMainCourseRecipe().getId())
                .isNotEqualTo(tomorrow.getMainCourseRecipe().getId());
            assertThat(today.getSideDishRecipe().getId())
                .isNotEqualTo(tomorrow.getSideDishRecipe().getId());
        }
    }

    @Test
    void generateBalancedMenuPlan_VerifyCaloriesAreSum() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        captor.getValue().getDailyMealPlans().forEach(dailyPlan -> {
            int expectedCalories = dailyPlan.getSoupRecipe().getCaloriesPerServing().intValue()
                + dailyPlan.getMainCourseRecipe().getCaloriesPerServing().intValue()
                + dailyPlan.getSideDishRecipe().getCaloriesPerServing().intValue();
            assertThat(dailyPlan.getTotalCalories()).isEqualTo(expectedCalories);
        });
    }

    @Test
    void generateBalancedMenuPlan_VerifyNutritionMetrics() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        MenuPlan captured = captor.getValue();
        assertThat(captured.getTotalCalories()).isNotNull();
        assertThat(captured.getTotalCalories()).isGreaterThan(0);
        assertThat(captured.getAverageDailyCalories()).isNotNull();
        assertThat(captured.getAverageDailyCalories()).isGreaterThan(0);
        assertThat(captured.getBalanceScore()).isBetween(0.0, 100.0);
    }

    @Test
    void generateBalancedMenuPlan_VerifyDatesAreMonToFri() {
        // Arrange
        MenuPlanRequest request = MenuPlanRequest.builder()
            .userId(userId)
            .startDate(startDate)
            .targetDailyCalories(2000)
            .build();

        Page<Recipe> recipePage = new PageImpl<>(allRecipes);
        when(recipeRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(recipePage);
        when(ingredientRepository.findAvailableIngredientsForUserOrGlobal(eq(userId), any(LocalDate.class)))
            .thenReturn(availableIngredients);

        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        when(menuPlanService.convertToResponse(any(MenuPlan.class)))
            .thenReturn(MenuPlanResponse.builder().build());

        // Act
        menuPlannerService.generateBalancedMenuPlan(request);

        // Assert
        ArgumentCaptor<MenuPlan> captor = ArgumentCaptor.forClass(MenuPlan.class);
        verify(menuPlanRepository).save(captor.capture());

        MenuPlan captured = captor.getValue();
        assertThat(captured.getStartDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(captured.getEndDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        captured.getDailyMealPlans().forEach(dailyPlan -> {
            DayOfWeek dow = dailyPlan.getMealDate().getDayOfWeek();
            assertThat(dow.getValue()).isBetween(1, 5); // Mon=1, Fri=5
        });
    }

    @Test
    void snapToMonday_SaturdaySnapsToNextMonday() {
        LocalDate saturday = LocalDate.of(2026, 2, 7); // Saturday
        LocalDate result = menuPlannerService.snapToMonday(saturday);
        assertThat(result).isEqualTo(LocalDate.of(2026, 2, 9)); // Next Monday
        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    @Test
    void snapToMonday_SundaySnapsToNextMonday() {
        LocalDate sunday = LocalDate.of(2026, 2, 8); // Sunday
        LocalDate result = menuPlannerService.snapToMonday(sunday);
        assertThat(result).isEqualTo(LocalDate.of(2026, 2, 9)); // Next Monday
        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    @Test
    void snapToMonday_WednesdaySnapsToSameWeekMonday() {
        LocalDate wednesday = LocalDate.of(2026, 2, 4); // Wednesday
        LocalDate result = menuPlannerService.snapToMonday(wednesday);
        assertThat(result).isEqualTo(LocalDate.of(2026, 2, 2)); // Same week Monday
        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    @Test
    void snapToMonday_MondayStaysMonday() {
        LocalDate monday = LocalDate.of(2026, 2, 2); // Monday
        LocalDate result = menuPlannerService.snapToMonday(monday);
        assertThat(result).isEqualTo(monday);
        assertThat(result.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }
}
