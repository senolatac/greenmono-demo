package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.exception.DuplicateIngredientException;
import com.greenmono.mealplanner.mapper.IngredientMapper;
import com.greenmono.mealplanner.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Transactional
    public IngredientResponse createIngredient(IngredientRequest request) {
        log.info("Creating ingredient: {}", request.getName());

        // Check for duplicate ingredient (same name and userId)
        if (request.getUserId() != null &&
            ingredientRepository.existsByUserIdAndName(request.getUserId(), request.getName())) {
            log.warn("Duplicate ingredient detected for user {} with name: {}",
                    request.getUserId(), request.getName());
            throw new DuplicateIngredientException(
                    String.format("Ingredient with name '%s' already exists for this user", request.getName())
            );
        }

        // Map request to entity
        Ingredient ingredient = ingredientMapper.toEntity(request);

        // Save to database
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        log.info("Successfully created ingredient with id: {}", savedIngredient.getId());

        // Map entity to response
        return ingredientMapper.toResponse(savedIngredient);
    }
}
