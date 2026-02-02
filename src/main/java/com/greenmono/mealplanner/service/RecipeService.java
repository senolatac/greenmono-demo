package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.dto.RecipeRequest;
import com.greenmono.mealplanner.dto.RecipeResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.entity.RecipeIngredient;
import com.greenmono.mealplanner.exception.DuplicateRecipeException;
import com.greenmono.mealplanner.exception.IngredientNotFoundException;
import com.greenmono.mealplanner.exception.RecipeNotFoundException;
import com.greenmono.mealplanner.mapper.RecipeMapper;
import com.greenmono.mealplanner.repository.IngredientRepository;
import com.greenmono.mealplanner.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;

    @Transactional
    public RecipeResponse createRecipe(RecipeRequest request) {
        log.info("Creating recipe: {}", request.getName());

        // Check for duplicate recipe (same name and userId)
        if (request.getUserId() != null &&
            recipeRepository.existsByUserIdAndNameIgnoreCase(request.getUserId(), request.getName())) {
            log.warn("Duplicate recipe detected for user {} with name: {}",
                    request.getUserId(), request.getName());
            throw new DuplicateRecipeException(
                    String.format("Recipe with name '%s' already exists for this user", request.getName())
            );
        }

        // Map request to entity
        Recipe recipe = recipeMapper.toEntity(request);

        // Process recipe ingredients
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            request.getIngredients().forEach(ingredientRequest -> {
                Ingredient ingredient = ingredientRepository.findById(ingredientRequest.getIngredientId())
                    .orElseThrow(() -> {
                        log.error("Ingredient not found with id: {}", ingredientRequest.getIngredientId());
                        return new IngredientNotFoundException(
                            String.format("Ingredient not found with id: %d", ingredientRequest.getIngredientId())
                        );
                    });

                RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .ingredient(ingredient)
                    .quantity(ingredientRequest.getQuantity())
                    .unit(ingredientRequest.getUnit())
                    .notes(ingredientRequest.getNotes())
                    .optional(ingredientRequest.getOptional() != null ? ingredientRequest.getOptional() : false)
                    .build();

                recipe.addRecipeIngredient(recipeIngredient);
            });
        }

        // Save to database
        Recipe savedRecipe = recipeRepository.save(recipe);
        log.info("Successfully created recipe with id: {}", savedRecipe.getId());

        // Map entity to response
        return recipeMapper.toResponse(savedRecipe);
    }

    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(Long id) {
        log.info("Fetching recipe with id: {}", id);

        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Recipe not found with id: {}", id);
                return new RecipeNotFoundException(
                    String.format("Recipe not found with id: %d", id)
                );
            });

        return recipeMapper.toResponse(recipe);
    }

    @Transactional(readOnly = true)
    public PageResponse<RecipeResponse> getRecipes(Recipe.RecipeCategory category, Pageable pageable) {
        log.info("Fetching recipes with category: {}, page: {}, size: {}",
                category, pageable.getPageNumber(), pageable.getPageSize());

        Page<Recipe> recipePage;

        if (category != null) {
            recipePage = recipeRepository.findByCategoryAndActiveTrue(category, pageable);
            log.debug("Found {} recipes for category: {}", recipePage.getTotalElements(), category);
        } else {
            recipePage = recipeRepository.findByActiveTrue(pageable);
            log.debug("Found {} total active recipes", recipePage.getTotalElements());
        }

        return PageResponse.<RecipeResponse>builder()
                .content(recipePage.getContent().stream()
                        .map(recipeMapper::toResponse)
                        .collect(Collectors.toList()))
                .pageNumber(recipePage.getNumber())
                .pageSize(recipePage.getSize())
                .totalElements(recipePage.getTotalElements())
                .totalPages(recipePage.getTotalPages())
                .first(recipePage.isFirst())
                .last(recipePage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RecipeResponse> getRecipesByIngredient(Long ingredientId) {
        log.info("Fetching recipes containing ingredient with id: {}", ingredientId);

        // Verify ingredient exists
        if (!ingredientRepository.existsById(ingredientId)) {
            log.warn("Ingredient not found with id: {}", ingredientId);
            throw new IngredientNotFoundException(
                String.format("Ingredient not found with id: %d", ingredientId)
            );
        }

        List<Recipe> recipes = recipeRepository.findByIngredientId(ingredientId);
        log.debug("Found {} recipes containing ingredient id: {}", recipes.size(), ingredientId);

        return recipes.stream()
                .map(recipeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeResponse> getRecipesByCookingTime(Integer minTime, Integer maxTime) {
        log.info("Fetching recipes with cooking time between {} and {} minutes", minTime, maxTime);

        List<Recipe> recipes = recipeRepository.findByCookingTimeRange(minTime, maxTime);
        log.debug("Found {} recipes in cooking time range", recipes.size());

        return recipes.stream()
                .map(recipeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        log.info("Updating recipe with id: {}", id);

        Recipe recipe = recipeRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Recipe not found with id: {}", id);
                return new RecipeNotFoundException(
                    String.format("Recipe not found with id: %d", id)
                );
            });

        // Check for duplicate name if name is being changed
        if (!recipe.getName().equalsIgnoreCase(request.getName()) &&
            request.getUserId() != null &&
            recipeRepository.existsByUserIdAndNameIgnoreCase(request.getUserId(), request.getName())) {
            log.warn("Duplicate recipe name detected: {}", request.getName());
            throw new DuplicateRecipeException(
                String.format("Recipe with name '%s' already exists for this user", request.getName())
            );
        }

        // Update basic fields
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setCategory(request.getCategory());
        recipe.setCookingTimeMinutes(request.getCookingTimeMinutes());
        recipe.setServings(request.getServings() != null ? request.getServings() : 1);
        recipe.setCalories(request.getCalories());
        recipe.setProtein(request.getProtein());
        recipe.setCarbohydrates(request.getCarbohydrates());
        recipe.setFat(request.getFat());
        recipe.setFiber(request.getFiber());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setInstructions(request.getInstructions());

        if (request.getActive() != null) {
            recipe.setActive(request.getActive());
        }

        // Update ingredients
        recipe.getRecipeIngredients().clear();
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            request.getIngredients().forEach(ingredientRequest -> {
                Ingredient ingredient = ingredientRepository.findById(ingredientRequest.getIngredientId())
                    .orElseThrow(() -> new IngredientNotFoundException(
                        String.format("Ingredient not found with id: %d", ingredientRequest.getIngredientId())
                    ));

                RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .ingredient(ingredient)
                    .quantity(ingredientRequest.getQuantity())
                    .unit(ingredientRequest.getUnit())
                    .notes(ingredientRequest.getNotes())
                    .optional(ingredientRequest.getOptional() != null ? ingredientRequest.getOptional() : false)
                    .build();

                recipe.addRecipeIngredient(recipeIngredient);
            });
        }

        Recipe updatedRecipe = recipeRepository.save(recipe);
        log.info("Successfully updated recipe with id: {}", id);

        return recipeMapper.toResponse(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        log.info("Deleting recipe with id: {}", id);

        if (!recipeRepository.existsById(id)) {
            log.warn("Recipe not found with id: {}", id);
            throw new RecipeNotFoundException(
                    String.format("Recipe not found with id: %d", id)
            );
        }

        recipeRepository.deleteById(id);
        log.info("Successfully deleted recipe with id: {}", id);
    }

    @Transactional(readOnly = true)
    public PageResponse<RecipeResponse> searchRecipesByName(String name, Pageable pageable) {
        log.info("Searching recipes with name containing: {}", name);

        Page<Recipe> recipePage = recipeRepository.findByNameContainingIgnoreCaseAndActiveTrue(name, pageable);
        log.debug("Found {} recipes matching search term: {}", recipePage.getTotalElements(), name);

        return PageResponse.<RecipeResponse>builder()
                .content(recipePage.getContent().stream()
                        .map(recipeMapper::toResponse)
                        .collect(Collectors.toList()))
                .pageNumber(recipePage.getNumber())
                .pageSize(recipePage.getSize())
                .totalElements(recipePage.getTotalElements())
                .totalPages(recipePage.getTotalPages())
                .first(recipePage.isFirst())
                .last(recipePage.isLast())
                .build();
    }
}
