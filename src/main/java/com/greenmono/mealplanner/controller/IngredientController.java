package com.greenmono.mealplanner.controller;

import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ingredients", description = "Ingredient management APIs")
public class IngredientController {

    private final IngredientService ingredientService;

    @Operation(summary = "Create a new ingredient", description = "Creates a new ingredient with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingredient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Ingredient already exists")
    })
    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@Valid @RequestBody IngredientRequest request) {
        log.info("Received request to create ingredient: {}", request.getName());
        IngredientResponse response = ingredientService.createIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all ingredients", description = "Retrieves a paginated list of ingredients with optional category filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or filter parameters")
    })
    @GetMapping
    public ResponseEntity<PageResponse<IngredientResponse>> getIngredients(
            @Parameter(description = "Filter by ingredient category")
            @RequestParam(required = false) Ingredient.IngredientCategory category,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("Received request to get ingredients - category: {}, page: {}, size: {}, sortBy: {}, sortDirection: {}",
                category, page, size, sortBy, sortDirection);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PageResponse<IngredientResponse> response = ingredientService.getIngredients(category, pageable);
        return ResponseEntity.ok(response);
    }
}
