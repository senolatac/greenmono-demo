package com.greenmono.mealplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenmono.mealplanner.dto.DailyMealPlanResponse;
import com.greenmono.mealplanner.dto.MenuPlanRequest;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.dto.RecipeResponse;
import com.greenmono.mealplanner.entity.MenuPlan;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.exception.MenuPlanNotFoundException;
import com.greenmono.mealplanner.service.MenuPlanService;
import com.greenmono.mealplanner.service.MenuPlannerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
@ActiveProfiles("test")
@DisplayName("MenuController Integration Tests")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuPlannerService menuPlannerService;

    @MockBean
    private MenuPlanService menuPlanService;

    private MenuPlanRequest validRequest;
    private MenuPlanResponse menuPlanResponse;

    @BeforeEach
    void setUp() {
        validRequest = MenuPlanRequest.builder()
                .userId(1L)
                .startDate(LocalDate.of(2026, 1, 26))
                .targetDailyCalories(2000)
                .caloriesPerMealMin(500)
                .caloriesPerMealMax(700)
                .build();

        // Create test recipes
        RecipeResponse breakfastRecipe = RecipeResponse.builder()
                .id(1L)
                .name("Menemen")
                .category(Recipe.RecipeCategory.BREAKFAST)
                .calories(BigDecimal.valueOf(450))
                .build();

        RecipeResponse lunchRecipe = RecipeResponse.builder()
                .id(2L)
                .name("Mantı")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .calories(BigDecimal.valueOf(650))
                .build();

        RecipeResponse dinnerRecipe = RecipeResponse.builder()
                .id(3L)
                .name("Izgara Tavuk")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .calories(BigDecimal.valueOf(550))
                .build();

        // Create daily meal plans for 5 days
        List<DailyMealPlanResponse> dailyMealPlans = Arrays.asList(
                createDailyMealPlan(1, LocalDate.of(2026, 1, 26), breakfastRecipe, lunchRecipe, dinnerRecipe),
                createDailyMealPlan(2, LocalDate.of(2026, 1, 27), breakfastRecipe, lunchRecipe, dinnerRecipe),
                createDailyMealPlan(3, LocalDate.of(2026, 1, 28), breakfastRecipe, lunchRecipe, dinnerRecipe),
                createDailyMealPlan(4, LocalDate.of(2026, 1, 29), breakfastRecipe, lunchRecipe, dinnerRecipe),
                createDailyMealPlan(5, LocalDate.of(2026, 1, 30), breakfastRecipe, lunchRecipe, dinnerRecipe)
        );

        menuPlanResponse = MenuPlanResponse.builder()
                .id(1L)
                .userId(1L)
                .startDate(LocalDate.of(2026, 1, 26))
                .endDate(LocalDate.of(2026, 1, 30))
                .dailyMealPlans(dailyMealPlans)
                .status(MenuPlan.MenuPlanStatus.ACTIVE)
                .totalCalories(8250)
                .averageDailyCalories(1650)
                .isBalanced(true)
                .balanceScore(85.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private DailyMealPlanResponse createDailyMealPlan(int dayNumber, LocalDate mealDate,
                                                      RecipeResponse breakfast,
                                                      RecipeResponse lunch,
                                                      RecipeResponse dinner) {
        return DailyMealPlanResponse.builder()
                .id((long) dayNumber)
                .dayNumber(dayNumber)
                .mealDate(mealDate)
                .breakfastRecipe(breakfast)
                .lunchRecipe(lunch)
                .dinnerRecipe(dinner)
                .totalCalories(1650)
                .build();
    }

    @Test
    @DisplayName("Should return 201 CREATED with simplified menu when generating menu plan")
    void shouldGenerateSimplifiedMenuSuccessfully() throws Exception {
        // Arrange
        when(menuPlannerService.generateBalancedMenuPlan(any(MenuPlanRequest.class)))
                .thenReturn(menuPlanResponse);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(15))) // 5 days × 3 meals = 15 items
                .andExpect(jsonPath("$[0].day", is("Pazar")))
                .andExpect(jsonPath("$[0].date", is("26.01.2026")))
                .andExpect(jsonPath("$[0].meal", is("Menemen")))
                .andExpect(jsonPath("$[1].day", is("Pazar")))
                .andExpect(jsonPath("$[1].date", is("26.01.2026")))
                .andExpect(jsonPath("$[1].meal", is("Mantı")))
                .andExpect(jsonPath("$[2].day", is("Pazar")))
                .andExpect(jsonPath("$[2].date", is("26.01.2026")))
                .andExpect(jsonPath("$[2].meal", is("Izgara Tavuk")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when userId is null")
    void shouldReturnBadRequestWhenUserIdIsNull() throws Exception {
        // Arrange
        validRequest.setUserId(null);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when startDate is null")
    void shouldReturnBadRequestWhenStartDateIsNull() throws Exception {
        // Arrange
        validRequest.setStartDate(null);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when targetDailyCalories is null")
    void shouldReturnBadRequestWhenTargetDailyCaloriesIsNull() throws Exception {
        // Arrange
        validRequest.setTargetDailyCalories(null);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    @DisplayName("Should return empty array when menu plan has no daily plans")
    void shouldReturnEmptyArrayWhenNoDailyPlans() throws Exception {
        // Arrange
        MenuPlanResponse emptyMenuPlan = MenuPlanResponse.builder()
                .id(1L)
                .userId(1L)
                .startDate(LocalDate.of(2026, 1, 26))
                .endDate(LocalDate.of(2026, 1, 30))
                .dailyMealPlans(Arrays.asList())
                .status(MenuPlan.MenuPlanStatus.ACTIVE)
                .build();

        when(menuPlannerService.generateBalancedMenuPlan(any(MenuPlanRequest.class)))
                .thenReturn(emptyMenuPlan);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return 200 OK with current menu for valid user")
    void shouldGetCurrentMenuSuccessfully() throws Exception {
        // Arrange
        Long userId = 1L;
        when(menuPlanService.getActiveMenuPlan(eq(userId)))
                .thenReturn(menuPlanResponse);

        // Act & Assert
        mockMvc.perform(get("/api/menu/current")
                        .param("userId", userId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(15))) // 5 days × 3 meals = 15 items
                .andExpect(jsonPath("$[0].day", is("Pazar")))
                .andExpect(jsonPath("$[0].date", is("26.01.2026")))
                .andExpect(jsonPath("$[0].meal", is("Menemen")))
                .andExpect(jsonPath("$[3].day", is("Pazartesi")))
                .andExpect(jsonPath("$[3].date", is("27.01.2026")))
                .andExpect(jsonPath("$[3].meal", is("Menemen")));
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when no active menu plan exists")
    void shouldReturnNotFoundWhenNoActiveMenuPlan() throws Exception {
        // Arrange
        Long userId = 999L;
        when(menuPlanService.getActiveMenuPlan(eq(userId)))
                .thenThrow(new MenuPlanNotFoundException("No active menu plan found for user: " + userId));

        // Act & Assert
        mockMvc.perform(get("/api/menu/current")
                        .param("userId", userId.toString()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("No active menu plan found")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when userId parameter is missing")
    void shouldReturnBadRequestWhenUserIdParameterIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/menu/current"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should format Turkish day names correctly")
    void shouldFormatTurkishDayNamesCorrectly() throws Exception {
        // Arrange
        // Create menu plan with all days of the week
        RecipeResponse recipe = RecipeResponse.builder()
                .id(1L)
                .name("Test Yemek")
                .calories(BigDecimal.valueOf(500))
                .build();

        List<DailyMealPlanResponse> weeklyPlans = Arrays.asList(
                createDailyMealPlan(1, LocalDate.of(2026, 2, 2), recipe, recipe, recipe),  // Pazartesi
                createDailyMealPlan(2, LocalDate.of(2026, 2, 3), recipe, recipe, recipe),  // Salı
                createDailyMealPlan(3, LocalDate.of(2026, 2, 4), recipe, recipe, recipe),  // Çarşamba
                createDailyMealPlan(4, LocalDate.of(2026, 2, 5), recipe, recipe, recipe),  // Perşembe
                createDailyMealPlan(5, LocalDate.of(2026, 2, 6), recipe, recipe, recipe)   // Cuma
        );

        MenuPlanResponse weeklyMenuPlan = MenuPlanResponse.builder()
                .id(1L)
                .userId(1L)
                .startDate(LocalDate.of(2026, 2, 2))
                .endDate(LocalDate.of(2026, 2, 6))
                .dailyMealPlans(weeklyPlans)
                .status(MenuPlan.MenuPlanStatus.ACTIVE)
                .build();

        when(menuPlanService.getActiveMenuPlan(eq(1L)))
                .thenReturn(weeklyMenuPlan);

        // Act & Assert
        mockMvc.perform(get("/api/menu/current")
                        .param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].day", is("Pazartesi")))
                .andExpect(jsonPath("$[0].date", is("02.02.2026")))
                .andExpect(jsonPath("$[3].day", is("Salı")))
                .andExpect(jsonPath("$[3].date", is("03.02.2026")))
                .andExpect(jsonPath("$[6].day", is("Çarşamba")))
                .andExpect(jsonPath("$[6].date", is("04.02.2026")))
                .andExpect(jsonPath("$[9].day", is("Perşembe")))
                .andExpect(jsonPath("$[9].date", is("05.02.2026")))
                .andExpect(jsonPath("$[12].day", is("Cuma")))
                .andExpect(jsonPath("$[12].date", is("06.02.2026")));
    }

    @Test
    @DisplayName("Should handle meals with null recipes gracefully")
    void shouldHandleMealsWithNullRecipesGracefully() throws Exception {
        // Arrange
        DailyMealPlanResponse dailyPlanWithNullRecipes = DailyMealPlanResponse.builder()
                .id(1L)
                .dayNumber(1)
                .mealDate(LocalDate.of(2026, 1, 26))
                .breakfastRecipe(null) // Null breakfast
                .lunchRecipe(RecipeResponse.builder()
                        .id(2L)
                        .name("Öğle Yemeği")
                        .build())
                .dinnerRecipe(null) // Null dinner
                .build();

        MenuPlanResponse menuPlanWithNulls = MenuPlanResponse.builder()
                .id(1L)
                .userId(1L)
                .dailyMealPlans(Arrays.asList(dailyPlanWithNulls))
                .build();

        when(menuPlannerService.generateBalancedMenuPlan(any(MenuPlanRequest.class)))
                .thenReturn(menuPlanWithNulls);

        // Act & Assert
        mockMvc.perform(post("/api/menu/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // Only lunch is present
                .andExpect(jsonPath("$[0].meal", is("Öğle Yemeği")));
    }
}
