package com.greenmono.mealplanner.controller;

import com.greenmono.mealplanner.dto.MenuPlanRequest;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.dto.SimplifiedMenuItemResponse;
import com.greenmono.mealplanner.service.MenuPlanService;
import com.greenmono.mealplanner.service.MenuPlannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu", description = "Simplified menu generation and viewing APIs")
public class MenuController {

    private final MenuPlannerService menuPlannerService;
    private final MenuPlanService menuPlanService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Locale TURKISH_LOCALE = new Locale("tr", "TR");

    @PostMapping("/generate")
    @Operation(
        summary = "Generate a 5-day menu plan",
        description = "Generates a balanced 5-day menu plan based on user's available ingredients. " +
                      "Returns a simplified list of meals with day name, date, and meal name."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Menu plan generated successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SimplifiedMenuItemResponse.class))
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request or insufficient recipes"),
        @ApiResponse(responseCode = "404", description = "No available ingredients found")
    })
    public ResponseEntity<List<SimplifiedMenuItemResponse>> generateMenu(
            @Valid @RequestBody MenuPlanRequest request) {
        log.info("Received request to generate simplified menu for user: {}", request.getUserId());

        // Generate the full menu plan using existing service
        MenuPlanResponse menuPlanResponse = menuPlannerService.generateBalancedMenuPlan(request);

        // Convert to simplified format
        List<SimplifiedMenuItemResponse> simplifiedMenu = convertToSimplifiedFormat(menuPlanResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(simplifiedMenu);
    }

    @GetMapping("/current")
    @Operation(
        summary = "Get current weekly menu",
        description = "Retrieves the currently active menu plan in simplified format. " +
                      "Returns a list of meals with day name, date, and meal name."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Current menu retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = SimplifiedMenuItemResponse.class))
            )
        ),
        @ApiResponse(responseCode = "404", description = "No active menu plan found for user")
    })
    public ResponseEntity<List<SimplifiedMenuItemResponse>> getCurrentMenu(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId) {
        log.info("Fetching current menu for user: {}", userId);

        // Get active menu plan
        MenuPlanResponse menuPlanResponse = menuPlanService.getActiveMenuPlan(userId);

        // Convert to simplified format
        List<SimplifiedMenuItemResponse> simplifiedMenu = convertToSimplifiedFormat(menuPlanResponse);

        return ResponseEntity.ok(simplifiedMenu);
    }

    /**
     * Converts a full MenuPlanResponse to a simplified list format.
     * Each day becomes a single item with soup, mainCourse, and sideDish fields.
     *
     * @param menuPlanResponse The full menu plan response
     * @return List of simplified menu items with day, date, soup, mainCourse, sideDish
     */
    private List<SimplifiedMenuItemResponse> convertToSimplifiedFormat(MenuPlanResponse menuPlanResponse) {
        List<SimplifiedMenuItemResponse> simplifiedMenu = new ArrayList<>();

        if (menuPlanResponse.getDailyMealPlans() == null || menuPlanResponse.getDailyMealPlans().isEmpty()) {
            return simplifiedMenu;
        }

        for (var dailyPlan : menuPlanResponse.getDailyMealPlans()) {
            LocalDate mealDate = dailyPlan.getMealDate();

            // Get day name in Turkish (Pazartesi, Salı, etc.)
            String dayName = mealDate.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, TURKISH_LOCALE);

            // Format date as dd.MM.yyyy
            String formattedDate = mealDate.format(DATE_FORMATTER);

            simplifiedMenu.add(SimplifiedMenuItemResponse.builder()
                .day(dayName)
                .date(formattedDate)
                .soup(dailyPlan.getSoupRecipe() != null ? dailyPlan.getSoupRecipe().getName() : null)
                .mainCourse(dailyPlan.getMainCourseRecipe() != null ? dailyPlan.getMainCourseRecipe().getName() : null)
                .sideDish(dailyPlan.getSideDishRecipe() != null ? dailyPlan.getSideDishRecipe().getName() : null)
                .build());
        }

        return simplifiedMenu;
    }
}
