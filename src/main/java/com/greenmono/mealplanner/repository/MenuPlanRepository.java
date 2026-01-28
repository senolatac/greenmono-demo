package com.greenmono.mealplanner.repository;

import com.greenmono.mealplanner.entity.MenuPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuPlanRepository extends JpaRepository<MenuPlan, Long> {

    List<MenuPlan> findByUserId(Long userId);

    List<MenuPlan> findByUserIdAndStatus(Long userId, MenuPlan.MenuPlanStatus status);

    List<MenuPlan> findByStatus(MenuPlan.MenuPlanStatus status);

    @Query("SELECT mp FROM MenuPlan mp WHERE mp.userId = :userId AND mp.startDate <= :date AND mp.endDate >= :date")
    List<MenuPlan> findActiveMenuPlansForUserOnDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT mp FROM MenuPlan mp WHERE mp.userId = :userId AND mp.status = 'ACTIVE'")
    Optional<MenuPlan> findActiveMenuPlanByUserId(@Param("userId") Long userId);

    @Query("SELECT mp FROM MenuPlan mp WHERE mp.userId = :userId AND mp.startDate >= :startDate AND mp.endDate <= :endDate")
    List<MenuPlan> findMenuPlansBetweenDates(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT mp FROM MenuPlan mp WHERE mp.userId = :userId AND mp.isBalanced = true")
    List<MenuPlan> findBalancedMenuPlansByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndStatus(Long userId, MenuPlan.MenuPlanStatus status);
}
