package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserIdAndActiveTrue(Long userId);

    List<Meal> findByMealType(Meal.MealType mealType);

    List<Meal> findByUserIdAndMealType(Long userId, Meal.MealType mealType);

    List<Meal> findByDifficultyLevel(Meal.DifficultyLevel difficultyLevel);

    List<Meal> findByUserIdAndDifficultyLevel(Long userId, Meal.DifficultyLevel difficultyLevel);

    @Query("SELECT m FROM Meal m WHERE m.prepTimeMinutes + m.cookTimeMinutes <= :maxTimeMinutes AND m.active = true")
    List<Meal> findByTotalTimeMinutesLessThanEqual(@Param("maxTimeMinutes") Integer maxTimeMinutes);

    @Query("SELECT m FROM Meal m JOIN m.ingredients i WHERE i.id = :ingredientId AND m.active = true")
    List<Meal> findByIngredientId(@Param("ingredientId") Long ingredientId);

    @Query("SELECT DISTINCT m FROM Meal m JOIN m.ingredients i WHERE i.id IN :ingredientIds AND m.active = true")
    List<Meal> findByIngredientIds(@Param("ingredientIds") List<Long> ingredientIds);

    @Query("SELECT m FROM Meal m WHERE m.name LIKE %:searchTerm% OR m.description LIKE %:searchTerm%")
    List<Meal> searchMeals(@Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT m FROM Meal m JOIN m.tags t WHERE t IN :tags AND m.active = true")
    List<Meal> findByTags(@Param("tags") List<String> tags);

    boolean existsByUserIdAndName(Long userId, String name);
}
