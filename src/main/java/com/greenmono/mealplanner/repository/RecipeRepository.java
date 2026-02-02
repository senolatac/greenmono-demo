package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Find recipes by category with pagination
     */
    Page<Recipe> findByCategory(Recipe.RecipeCategory category, Pageable pageable);

    /**
     * Find active recipes by category
     */
    Page<Recipe> findByCategoryAndActiveTrue(Recipe.RecipeCategory category, Pageable pageable);

    /**
     * Find all active recipes
     */
    Page<Recipe> findByActiveTrue(Pageable pageable);

    /**
     * Find recipes by user ID
     */
    Page<Recipe> findByUserId(Long userId, Pageable pageable);

    /**
     * Find active recipes by user ID
     */
    Page<Recipe> findByUserIdAndActiveTrue(Long userId, Pageable pageable);

    /**
     * Find recipe by name (case-insensitive)
     */
    Optional<Recipe> findByNameIgnoreCase(String name);

    /**
     * Check if recipe exists by name for a user
     */
    boolean existsByUserIdAndName(Long userId, String name);

    /**
     * Check if recipe exists by name (case-insensitive) for a user
     */
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    /**
     * Find recipes by cooking time range
     */
    @Query("SELECT r FROM Recipe r WHERE r.cookingTimeMinutes BETWEEN :minTime AND :maxTime AND r.active = true")
    List<Recipe> findByCookingTimeRange(@Param("minTime") Integer minTime, @Param("maxTime") Integer maxTime);

    /**
     * Find recipes with specific ingredient
     */
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.recipeIngredients ri WHERE ri.ingredient.id = :ingredientId AND r.active = true")
    List<Recipe> findByIngredientId(@Param("ingredientId") Long ingredientId);

    /**
     * Find recipes by name containing (case-insensitive search)
     */
    Page<Recipe> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    /**
     * Count recipes by category
     */
    long countByCategory(Recipe.RecipeCategory category);

    /**
     * Count active recipes
     */
    long countByActiveTrue();
}
