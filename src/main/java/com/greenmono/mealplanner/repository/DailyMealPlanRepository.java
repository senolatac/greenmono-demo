package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.DailyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMealPlanRepository extends JpaRepository<DailyMealPlan, Long> {

    List<DailyMealPlan> findByMenuPlanId(Long menuPlanId);

    Optional<DailyMealPlan> findByMenuPlanIdAndDayNumber(Long menuPlanId, Integer dayNumber);

    Optional<DailyMealPlan> findByMenuPlanIdAndMealDate(Long menuPlanId, LocalDate mealDate);

    @Query("SELECT dmp FROM DailyMealPlan dmp WHERE dmp.menuPlan.id = :menuPlanId ORDER BY dmp.dayNumber ASC")
    List<DailyMealPlan> findByMenuPlanIdOrderedByDay(@Param("menuPlanId") Long menuPlanId);

    @Query("SELECT dmp FROM DailyMealPlan dmp WHERE dmp.mealDate = :date")
    List<DailyMealPlan> findByDate(@Param("date") LocalDate date);

    @Query("SELECT dmp FROM DailyMealPlan dmp WHERE dmp.menuPlan.userId = :userId AND dmp.mealDate BETWEEN :startDate AND :endDate")
    List<DailyMealPlan> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
