package com.greenmono.mealplanner.mapper;

import com.greenmono.mealplanner.dto.RecipeRequest;
import com.greenmono.mealplanner.dto.RecipeResponse;
import com.greenmono.mealplanner.dto.RecipeIngredientResponse;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.entity.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recipeIngredients", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", expression = "java(request.getActive() != null ? request.getActive() : true)")
    @Mapping(target = "servings", expression = "java(request.getServings() != null ? request.getServings() : 1)")
    Recipe toEntity(RecipeRequest request);

    @Mapping(target = "ingredients", source = "recipeIngredients", qualifiedByName = "mapRecipeIngredients")
    RecipeResponse toResponse(Recipe recipe);

    @Named("mapRecipeIngredients")
    default List<RecipeIngredientResponse> mapRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        if (recipeIngredients == null) {
            return List.of();
        }
        return recipeIngredients.stream()
            .map(ri -> RecipeIngredientResponse.builder()
                .id(ri.getId())
                .ingredientId(ri.getIngredient().getId())
                .ingredientName(ri.getIngredient().getName())
                .quantity(ri.getQuantity())
                .unit(ri.getUnit())
                .notes(ri.getNotes())
                .optional(ri.getOptional())
                .build())
            .collect(Collectors.toList());
    }
}
