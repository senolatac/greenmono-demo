package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.NutritionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionalInfoRepository extends JpaRepository<NutritionalInfo, Long> {

    Optional<NutritionalInfo> findByIngredientId(Long ingredientId);

    Optional<NutritionalInfo> findByMealId(Long mealId);

    @Query("SELECT ni FROM NutritionalInfo ni WHERE ni.meal.id IN :mealIds")
    List<NutritionalInfo> findByMealIds(@Param("mealIds") List<Long> mealIds);

    @Query("SELECT ni FROM NutritionalInfo ni WHERE ni.calories >= :minCalories AND ni.calories <= :maxCalories")
    List<NutritionalInfo> findByCaloriesRange(@Param("minCalories") BigDecimal minCalories, @Param("maxCalories") BigDecimal maxCalories);

    @Query("SELECT ni FROM NutritionalInfo ni WHERE ni.protein >= :minProtein")
    List<NutritionalInfo> findHighProteinItems(@Param("minProtein") BigDecimal minProtein);

    @Query("SELECT ni FROM NutritionalInfo ni WHERE ni.carbohydrates <= :maxCarbs")
    List<NutritionalInfo> findLowCarbItems(@Param("maxCarbs") BigDecimal maxCarbs);

    boolean existsByIngredientId(Long ingredientId);

    boolean existsByMealId(Long mealId);
}
