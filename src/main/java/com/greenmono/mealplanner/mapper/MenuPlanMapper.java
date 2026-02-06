package com.greenmono.mealplanner.mapper;

import com.greenmono.mealplanner.dto.DailyMealPlanResponse;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.entity.DailyMealPlan;
import com.greenmono.mealplanner.entity.MenuPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RecipeMapper.class})
public interface MenuPlanMapper {

    @Mapping(target = "dailyMealPlans", source = "dailyMealPlans", qualifiedByName = "mapDailyMealPlans")
    MenuPlanResponse toResponse(MenuPlan menuPlan);

    @Named("mapDailyMealPlans")
    default List<DailyMealPlanResponse> mapDailyMealPlans(Set<DailyMealPlan> dailyMealPlans) {
        if (dailyMealPlans == null) {
            return List.of();
        }
        return dailyMealPlans.stream()
            .map(this::mapDailyMealPlan)
            .collect(Collectors.toList());
    }

    default DailyMealPlanResponse mapDailyMealPlan(DailyMealPlan dailyMealPlan) {
        if (dailyMealPlan == null) {
            return null;
        }

        return DailyMealPlanResponse.builder()
            .id(dailyMealPlan.getId())
            .dayNumber(dailyMealPlan.getDayNumber())
            .mealDate(dailyMealPlan.getMealDate())
            .soupRecipe(dailyMealPlan.getSoupRecipe() != null
                ? mapRecipeToResponse(dailyMealPlan.getSoupRecipe())
                : null)
            .mainCourseRecipe(dailyMealPlan.getMainCourseRecipe() != null
                ? mapRecipeToResponse(dailyMealPlan.getMainCourseRecipe())
                : null)
            .sideDishRecipe(dailyMealPlan.getSideDishRecipe() != null
                ? mapRecipeToResponse(dailyMealPlan.getSideDishRecipe())
                : null)
            .totalCalories(dailyMealPlan.getTotalCalories())
            .notes(dailyMealPlan.getNotes())
            .build();
    }

    default com.greenmono.mealplanner.dto.RecipeResponse mapRecipeToResponse(
            com.greenmono.mealplanner.entity.Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        return com.greenmono.mealplanner.dto.RecipeResponse.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .description(recipe.getDescription())
            .category(recipe.getCategory())
            .cookingTimeMinutes(recipe.getCookingTimeMinutes())
            .servings(recipe.getServings())
            .calories(recipe.getCalories())
            .protein(recipe.getProtein())
            .carbohydrates(recipe.getCarbohydrates())
            .fat(recipe.getFat())
            .fiber(recipe.getFiber())
            .imageUrl(recipe.getImageUrl())
            .userId(recipe.getUserId())
            .active(recipe.getActive())
            .createdAt(recipe.getCreatedAt())
            .updatedAt(recipe.getUpdatedAt())
            .build();
    }
}
