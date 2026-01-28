package com.greenmono.mealplanner.mapper;

import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "nutritionalInfo", ignore = true)
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "available", expression = "java(request.getAvailable() != null ? request.getAvailable() : true)")
    Ingredient toEntity(IngredientRequest request);

    IngredientResponse toResponse(Ingredient ingredient);
}
