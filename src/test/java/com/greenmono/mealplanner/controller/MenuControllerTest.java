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
                .startDate(LocalDate.of(2026, 2, 2)) // Monday
                .targetDailyCalories(2000)
                .caloriesPerMealMin(500)
                .caloriesPerMealMax(700)
                .build();

        // Create test recipes
        RecipeResponse soupRecipe = RecipeResponse.builder()
                .id(1L)
                .name("Mercimek \u00c7orbas\u0131")
                .category(Recipe.RecipeCategory.SOUP)
                .calories(BigDecimal.valueOf(180))
                .build();

        RecipeResponse mainCourseRecipe = RecipeResponse.builder()
                .id(2L)
                .name("Mant\u0131")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .calories(BigDecimal.valueOf(520))
                .build();

        RecipeResponse sideDishRecipe = RecipeResponse.builder()
                .id(3L)
                .name("Pilav")
                .category(Recipe.RecipeCategory.SIDE_DISH)
                .calories(BigDecimal.valueOf(250))
                .build();

        // Create daily meal plans for 5 days (Mon-Fri)
        List<DailyMealPlanResponse> dailyMealPlans = Arrays.asList(
                createDailyMealPlan(1, LocalDate.of(2026, 2, 2), soupRecipe, mainCourseRecipe, sideDishRecipe),
                createDailyMealPlan(2, LocalDate.of(2026, 2, 3), soupRecipe, mainCourseRecipe, sideDishRecipe),
                createDailyMealPlan(3, LocalDate.of(2026, 2, 4), soupRecipe, mainCourseRecipe, sideDishRecipe),
                createDailyMealPlan(4, LocalDate.of(2026, 2, 5), soupRecipe, mainCourseRecipe, sideDishRecipe),
                createDailyMealPlan(5, LocalDate.of(2026, 2, 6), soupRecipe, mainCourseRecipe, sideDishRecipe)
        );

        menuPlanResponse = MenuPlanResponse.builder()
                .id(1L)
                .userId(1L)
                .startDate(LocalDate.of(2026, 2, 2))
                .endDate(LocalDate.of(2026, 2, 6))
                .dailyMealPlans(dailyMealPlans)
                .status(MenuPlan.MenuPlanStatus.ACTIVE)
                .totalCalories(4750)
                .averageDailyCalories(950)
                .isBalanced(true)
                .balanceScore(85.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private DailyMealPlanResponse createDailyMealPlan(int dayNumber, LocalDate mealDate,
                                                      RecipeResponse soup,
                                                      RecipeResponse mainCourse,
                                                      RecipeResponse sideDish) {
        return DailyMealPlanResponse.builder()
                .id((long) dayNumber)
                .dayNumber(dayNumber)
                .mealDate(mealDate)
                .soupRecipe(soup)
                .mainCourseRecipe(mainCourse)
                .sideDishRecipe(sideDish)
                .totalCalories(950)
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
                .andExpect(jsonPath("$", hasSize(5))) // 5 days, 1 item per day
                .andExpect(jsonPath("$[0].day", is("Pazartesi")))
                .andExpect(jsonPath("$[0].date", is("02.02.2026")))
                .andExpect(jsonPath("$[0].soup", is("Mercimek \u00c7orbas\u0131")))
                .andExpect(jsonPath("$[0].mainCourse", is("Mant\u0131")))
                .andExpect(jsonPath("$[0].sideDish", is("Pilav")));
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
                .startDate(LocalDate.of(2026, 2, 2))
                .endDate(LocalDate.of(2026, 2, 6))
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
                .andExpect(jsonPath("$", hasSize(5))) // 5 days, 1 item per day
                .andExpect(jsonPath("$[0].day", is("Pazartesi")))
                .andExpect(jsonPath("$[0].date", is("02.02.2026")))
                .andExpect(jsonPath("$[0].soup", is("Mercimek \u00c7orbas\u0131")))
                .andExpect(jsonPath("$[0].mainCourse", is("Mant\u0131")))
                .andExpect(jsonPath("$[0].sideDish", is("Pilav")));
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
    @DisplayName("Should format Turkish day names correctly for Mon-Fri")
    void shouldFormatTurkishDayNamesCorrectly() throws Exception {
        // Arrange
        RecipeResponse soup = RecipeResponse.builder()
                .id(1L).name("Test \u00c7orba").calories(BigDecimal.valueOf(180)).build();
        RecipeResponse main = RecipeResponse.builder()
                .id(2L).name("Test Yemek").calories(BigDecimal.valueOf(500)).build();
        RecipeResponse side = RecipeResponse.builder()
                .id(3L).name("Test Pilav").calories(BigDecimal.valueOf(250)).build();

        List<DailyMealPlanResponse> weeklyPlans = Arrays.asList(
                createDailyMealPlan(1, LocalDate.of(2026, 2, 2), soup, main, side),  // Pazartesi
                createDailyMealPlan(2, LocalDate.of(2026, 2, 3), soup, main, side),  // Sal\u0131
                createDailyMealPlan(3, LocalDate.of(2026, 2, 4), soup, main, side),  // \u00c7ar\u015famba
                createDailyMealPlan(4, LocalDate.of(2026, 2, 5), soup, main, side),  // Per\u015fembe
                createDailyMealPlan(5, LocalDate.of(2026, 2, 6), soup, main, side)   // Cuma
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
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].day", is("Pazartesi")))
                .andExpect(jsonPath("$[0].date", is("02.02.2026")))
                .andExpect(jsonPath("$[0].soup", is("Test \u00c7orba")))
                .andExpect(jsonPath("$[0].mainCourse", is("Test Yemek")))
                .andExpect(jsonPath("$[0].sideDish", is("Test Pilav")))
                .andExpect(jsonPath("$[1].day", is("Sal\u0131")))
                .andExpect(jsonPath("$[2].day", is("\u00c7ar\u015famba")))
                .andExpect(jsonPath("$[3].day", is("Per\u015fembe")))
                .andExpect(jsonPath("$[4].day", is("Cuma")));
    }

    @Test
    @DisplayName("Should handle daily plans with null recipes gracefully")
    void shouldHandleDailyPlansWithNullRecipesGracefully() throws Exception {
        // Arrange
        DailyMealPlanResponse dailyPlanWithNulls = DailyMealPlanResponse.builder()
                .id(1L)
                .dayNumber(1)
                .mealDate(LocalDate.of(2026, 2, 2))
                .soupRecipe(null)
                .mainCourseRecipe(RecipeResponse.builder()
                        .id(2L)
                        .name("Ana Yemek")
                        .build())
                .sideDishRecipe(null)
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
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].soup").doesNotExist())
                .andExpect(jsonPath("$[0].mainCourse", is("Ana Yemek")))
                .andExpect(jsonPath("$[0].sideDish").doesNotExist());
    }
}
