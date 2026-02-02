package com.greenmono.mealplanner.controller;

import com.greenmono.mealplanner.dto.MenuPlanRequest;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.MenuPlan;
import com.greenmono.mealplanner.service.MenuPlanService;
import com.greenmono.mealplanner.service.MenuPlannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menu-plans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu Plans", description = "Menu plan management and generation APIs")
public class MenuPlanController {

    private final MenuPlannerService menuPlannerService;
    private final MenuPlanService menuPlanService;

    @PostMapping("/generate")
    @Operation(
        summary = "Generate a balanced 5-day menu plan",
        description = "Generates a balanced menu plan based on available ingredients. " +
                      "Rules: (1) Protein-carb balance, (2) No same ingredient 2 consecutive days, " +
                      "(3) 500-700 kcal per meal, (4) Uses available ingredients only"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Menu plan generated successfully",
            content = @Content(schema = @Schema(implementation = MenuPlanResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request or insufficient recipes"),
        @ApiResponse(responseCode = "404", description = "No available ingredients found")
    })
    public ResponseEntity<MenuPlanResponse> generateMenuPlan(
            @Valid @RequestBody MenuPlanRequest request) {
        log.info("Received request to generate menu plan for user: {}", request.getUserId());

        MenuPlanResponse response = menuPlannerService.generateBalancedMenuPlan(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu plan by ID", description = "Retrieves a specific menu plan by its ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu plan found",
            content = @Content(schema = @Schema(implementation = MenuPlanResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Menu plan not found")
    })
    public ResponseEntity<MenuPlanResponse> getMenuPlanById(
            @Parameter(description = "Menu plan ID") @PathVariable Long id) {
        log.info("Fetching menu plan with id: {}", id);

        MenuPlanResponse response = menuPlanService.getMenuPlanById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all menu plans for a user", description = "Retrieves all menu plans for a specific user with pagination")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu plans retrieved successfully",
            content = @Content(schema = @Schema(implementation = PageResponse.class))
        )
    })
    public ResponseEntity<PageResponse<MenuPlanResponse>> getMenuPlansByUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection) {

        log.info("Fetching menu plans for user: {}", userId);

        PageResponse<MenuPlanResponse> response = menuPlanService.getMenuPlansByUser(
            userId, page, size, sortBy, sortDirection
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "Get menu plans by status", description = "Retrieves menu plans filtered by status")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu plans retrieved successfully"
        )
    })
    public ResponseEntity<List<MenuPlanResponse>> getMenuPlansByStatus(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Status (DRAFT, ACTIVE, COMPLETED, ARCHIVED)") @PathVariable MenuPlan.MenuPlanStatus status) {

        log.info("Fetching menu plans for user {} with status: {}", userId, status);

        List<MenuPlanResponse> responses = menuPlanService.getMenuPlansByStatus(userId, status);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get active menu plan", description = "Retrieves the currently active menu plan for a user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Active menu plan found",
            content = @Content(schema = @Schema(implementation = MenuPlanResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "No active menu plan found")
    })
    public ResponseEntity<MenuPlanResponse> getActiveMenuPlan(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        log.info("Fetching active menu plan for user: {}", userId);

        MenuPlanResponse response = menuPlanService.getActiveMenuPlan(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/balanced")
    @Operation(summary = "Get balanced menu plans", description = "Retrieves all balanced menu plans for a user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Balanced menu plans retrieved successfully"
        )
    })
    public ResponseEntity<List<MenuPlanResponse>> getBalancedMenuPlans(
            @Parameter(description = "User ID") @PathVariable Long userId) {

        log.info("Fetching balanced menu plans for user: {}", userId);

        List<MenuPlanResponse> responses = menuPlanService.getBalancedMenuPlans(userId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date-range")
    @Operation(summary = "Get menu plans by date range", description = "Retrieves menu plans within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu plans retrieved successfully"
        )
    })
    public ResponseEntity<List<MenuPlanResponse>> getMenuPlansByDateRange(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("Fetching menu plans for user {} between {} and {}", userId, startDate, endDate);

        List<MenuPlanResponse> responses = menuPlanService.getMenuPlansByDateRange(userId, startDate, endDate);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update menu plan status", description = "Updates the status of a menu plan")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = MenuPlanResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Menu plan not found")
    })
    public ResponseEntity<MenuPlanResponse> updateMenuPlanStatus(
            @Parameter(description = "Menu plan ID") @PathVariable Long id,
            @Parameter(description = "New status") @RequestParam MenuPlan.MenuPlanStatus status) {

        log.info("Updating menu plan {} status to: {}", id, status);

        MenuPlanResponse response = menuPlanService.updateMenuPlanStatus(id, status);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/activate")
    @Operation(
        summary = "Activate menu plan",
        description = "Activates a menu plan and deactivates any other active plans for the user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Menu plan activated successfully",
            content = @Content(schema = @Schema(implementation = MenuPlanResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Menu plan not found")
    })
    public ResponseEntity<MenuPlanResponse> activateMenuPlan(
            @Parameter(description = "Menu plan ID") @PathVariable Long id) {

        log.info("Activating menu plan: {}", id);

        MenuPlanResponse response = menuPlanService.activateMenuPlan(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu plan", description = "Deletes a menu plan by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Menu plan deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu plan not found")
    })
    public ResponseEntity<Void> deleteMenuPlan(
            @Parameter(description = "Menu plan ID") @PathVariable Long id) {

        log.info("Deleting menu plan: {}", id);

        menuPlanService.deleteMenuPlan(id);

        return ResponseEntity.noContent().build();
    }
}
