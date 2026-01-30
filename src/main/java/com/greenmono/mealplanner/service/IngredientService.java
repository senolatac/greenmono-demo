package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.exception.DuplicateIngredientException;
import com.greenmono.mealplanner.mapper.IngredientMapper;
import com.greenmono.mealplanner.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public PageResponse<IngredientResponse> getIngredients(Ingredient.IngredientCategory category, Pageable pageable) {
        log.info("Fetching ingredients with category: {}, page: {}, size: {}",
                category, pageable.getPageNumber(), pageable.getPageSize());

        Page<Ingredient> ingredientPage;

        if (category != null) {
            ingredientPage = ingredientRepository.findByCategory(category, pageable);
            log.debug("Found {} ingredients for category: {}", ingredientPage.getTotalElements(), category);
        } else {
            ingredientPage = ingredientRepository.findAll(pageable);
            log.debug("Found {} total ingredients", ingredientPage.getTotalElements());
        }

        return PageResponse.<IngredientResponse>builder()
                .content(ingredientPage.getContent().stream()
                        .map(ingredientMapper::toResponse)
                        .collect(Collectors.toList()))
                .pageNumber(ingredientPage.getNumber())
                .pageSize(ingredientPage.getSize())
                .totalElements(ingredientPage.getTotalElements())
                .totalPages(ingredientPage.getTotalPages())
                .first(ingredientPage.isFirst())
                .last(ingredientPage.isLast())
                .build();
    }
}
