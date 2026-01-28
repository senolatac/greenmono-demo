package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByUserId(Long userId);

    List<Ingredient> findByUserIdAndAvailableTrue(Long userId);

    List<Ingredient> findByCategory(Ingredient.IngredientCategory category);

    List<Ingredient> findByUserIdAndCategory(Long userId, Ingredient.IngredientCategory category);

    List<Ingredient> findByExpiryDateBefore(LocalDate date);

    List<Ingredient> findByUserIdAndExpiryDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT i FROM Ingredient i WHERE i.userId = :userId AND i.available = true AND (i.expiryDate IS NULL OR i.expiryDate >= :currentDate)")
    List<Ingredient> findAvailableIngredientsForUser(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:searchTerm% OR i.notes LIKE %:searchTerm%")
    List<Ingredient> searchIngredients(@Param("searchTerm") String searchTerm);

    boolean existsByUserIdAndName(Long userId, String name);
}
