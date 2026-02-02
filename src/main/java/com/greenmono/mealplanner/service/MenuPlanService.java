package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.DailyMealPlanResponse;
import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.DailyMealPlan;
import com.greenmono.mealplanner.entity.MenuPlan;
import com.greenmono.mealplanner.exception.MenuPlanNotFoundException;
import com.greenmono.mealplanner.mapper.MenuPlanMapper;
import com.greenmono.mealplanner.repository.MenuPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuPlanService {

    private final MenuPlanRepository menuPlanRepository;
    private final MenuPlanMapper menuPlanMapper;

    /**
     * Retrieves a menu plan by ID
     */
    @Transactional(readOnly = true)
    public MenuPlanResponse getMenuPlanById(Long id) {
        log.info("Fetching menu plan with id: {}", id);
        MenuPlan menuPlan = menuPlanRepository.findById(id)
            .orElseThrow(() -> new MenuPlanNotFoundException("Menu plan not found with id: " + id));

        return convertToResponse(menuPlan);
    }

    /**
     * Retrieves all menu plans for a user with pagination
     */
    @Transactional(readOnly = true)
    public PageResponse<MenuPlanResponse> getMenuPlansByUser(
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.info("Fetching menu plans for user: {}", userId);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<MenuPlan> menuPlansPage = menuPlanRepository.findByUserId(userId, pageable);

        List<MenuPlanResponse> responses = menuPlansPage.getContent().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());

        return PageResponse.<MenuPlanResponse>builder()
            .content(responses)
            .pageNumber(menuPlansPage.getNumber())
            .pageSize(menuPlansPage.getSize())
            .totalElements(menuPlansPage.getTotalElements())
            .totalPages(menuPlansPage.getTotalPages())
            .first(menuPlansPage.isFirst())
            .last(menuPlansPage.isLast())
            .build();
    }

    /**
     * Retrieves menu plans by status
     */
    @Transactional(readOnly = true)
    public List<MenuPlanResponse> getMenuPlansByStatus(Long userId, MenuPlan.MenuPlanStatus status) {
        log.info("Fetching menu plans for user {} with status: {}", userId, status);

        List<MenuPlan> menuPlans = menuPlanRepository.findByUserIdAndStatus(userId, status);

        return menuPlans.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves the active menu plan for a user
     */
    @Transactional(readOnly = true)
    public MenuPlanResponse getActiveMenuPlan(Long userId) {
        log.info("Fetching active menu plan for user: {}", userId);

        MenuPlan menuPlan = menuPlanRepository.findActiveMenuPlanByUserId(userId)
            .orElseThrow(() -> new MenuPlanNotFoundException("No active menu plan found for user: " + userId));

        return convertToResponse(menuPlan);
    }

    /**
     * Retrieves balanced menu plans for a user
     */
    @Transactional(readOnly = true)
    public List<MenuPlanResponse> getBalancedMenuPlans(Long userId) {
        log.info("Fetching balanced menu plans for user: {}", userId);

        List<MenuPlan> menuPlans = menuPlanRepository.findBalancedMenuPlansByUserId(userId);

        return menuPlans.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Updates menu plan status
     */
    @Transactional
    public MenuPlanResponse updateMenuPlanStatus(Long id, MenuPlan.MenuPlanStatus status) {
        log.info("Updating menu plan {} status to: {}", id, status);

        MenuPlan menuPlan = menuPlanRepository.findById(id)
            .orElseThrow(() -> new MenuPlanNotFoundException("Menu plan not found with id: " + id));

        menuPlan.setStatus(status);
        MenuPlan savedPlan = menuPlanRepository.save(menuPlan);

        return convertToResponse(savedPlan);
    }

    /**
     * Activates a menu plan (sets status to ACTIVE and deactivates others)
     */
    @Transactional
    public MenuPlanResponse activateMenuPlan(Long id) {
        log.info("Activating menu plan: {}", id);

        MenuPlan menuPlan = menuPlanRepository.findById(id)
            .orElseThrow(() -> new MenuPlanNotFoundException("Menu plan not found with id: " + id));

        // Deactivate other active plans for the same user
        menuPlanRepository.findByUserIdAndStatus(menuPlan.getUserId(), MenuPlan.MenuPlanStatus.ACTIVE)
            .forEach(plan -> {
                plan.setStatus(MenuPlan.MenuPlanStatus.COMPLETED);
                menuPlanRepository.save(plan);
            });

        // Activate the selected plan
        menuPlan.setStatus(MenuPlan.MenuPlanStatus.ACTIVE);
        MenuPlan savedPlan = menuPlanRepository.save(menuPlan);

        return convertToResponse(savedPlan);
    }

    /**
     * Deletes a menu plan
     */
    @Transactional
    public void deleteMenuPlan(Long id) {
        log.info("Deleting menu plan: {}", id);

        if (!menuPlanRepository.existsById(id)) {
            throw new MenuPlanNotFoundException("Menu plan not found with id: " + id);
        }

        menuPlanRepository.deleteById(id);
        log.info("Menu plan deleted successfully: {}", id);
    }

    /**
     * Retrieves menu plans within a date range
     */
    @Transactional(readOnly = true)
    public List<MenuPlanResponse> getMenuPlansByDateRange(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {

        log.info("Fetching menu plans for user {} between {} and {}", userId, startDate, endDate);

        List<MenuPlan> menuPlans = menuPlanRepository.findMenuPlansBetweenDates(userId, startDate, endDate);

        return menuPlans.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Converts MenuPlan entity to response DTO
     */
    public MenuPlanResponse convertToResponse(MenuPlan menuPlan) {
        MenuPlanResponse response = menuPlanMapper.toResponse(menuPlan);

        // Sort daily meal plans by day number
        List<DailyMealPlanResponse> sortedDailyPlans = response.getDailyMealPlans().stream()
            .sorted(Comparator.comparing(DailyMealPlanResponse::getDayNumber))
            .collect(Collectors.toList());

        response.setDailyMealPlans(sortedDailyPlans);

        return response;
    }
}
