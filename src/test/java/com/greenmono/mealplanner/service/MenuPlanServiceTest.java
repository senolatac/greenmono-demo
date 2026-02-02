package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.MenuPlanResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.MenuPlan;
import com.greenmono.mealplanner.exception.MenuPlanNotFoundException;
import com.greenmono.mealplanner.mapper.MenuPlanMapper;
import com.greenmono.mealplanner.repository.MenuPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuPlanServiceTest {

    @Mock
    private MenuPlanRepository menuPlanRepository;

    @Mock
    private MenuPlanMapper menuPlanMapper;

    @InjectMocks
    private MenuPlanService menuPlanService;

    private MenuPlan testMenuPlan;
    private MenuPlanResponse testMenuPlanResponse;
    private Long userId;
    private Long menuPlanId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        menuPlanId = 100L;

        testMenuPlan = MenuPlan.builder()
            .id(menuPlanId)
            .name("Test Menu Plan")
            .description("Test description")
            .userId(userId)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(4))
            .status(MenuPlan.MenuPlanStatus.DRAFT)
            .totalCalories(9000)
            .averageDailyCalories(1800)
            .isBalanced(true)
            .balanceScore(85.0)
            .dailyMealPlans(new HashSet<>())
            .build();

        testMenuPlanResponse = MenuPlanResponse.builder()
            .id(menuPlanId)
            .name("Test Menu Plan")
            .description("Test description")
            .userId(userId)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(4))
            .status(MenuPlan.MenuPlanStatus.DRAFT)
            .totalCalories(9000)
            .averageDailyCalories(1800)
            .isBalanced(true)
            .balanceScore(85.0)
            .dailyMealPlans(new ArrayList<>())
            .build();
    }

    @Test
    void getMenuPlanById_Success() {
        // Arrange
        when(menuPlanRepository.findById(menuPlanId))
            .thenReturn(Optional.of(testMenuPlan));
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        MenuPlanResponse result = menuPlanService.getMenuPlanById(menuPlanId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(menuPlanId);
        assertThat(result.getName()).isEqualTo("Test Menu Plan");

        verify(menuPlanRepository).findById(menuPlanId);
        verify(menuPlanMapper).toResponse(testMenuPlan);
    }

    @Test
    void getMenuPlanById_NotFound_ThrowsException() {
        // Arrange
        when(menuPlanRepository.findById(menuPlanId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> menuPlanService.getMenuPlanById(menuPlanId))
            .isInstanceOf(MenuPlanNotFoundException.class)
            .hasMessage("Menu plan not found with id: " + menuPlanId);

        verify(menuPlanRepository).findById(menuPlanId);
        verify(menuPlanMapper, never()).toResponse(any());
    }

    @Test
    void getMenuPlansByUser_Success() {
        // Arrange
        int page = 0;
        int size = 20;
        String sortBy = "createdAt";
        String sortDirection = "desc";

        List<MenuPlan> menuPlans = Arrays.asList(testMenuPlan);
        Page<MenuPlan> menuPlansPage = new PageImpl<>(
            menuPlans,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy)),
            1
        );

        when(menuPlanRepository.findByUserId(eq(userId), any(PageRequest.class)))
            .thenReturn(menuPlansPage);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        PageResponse<MenuPlanResponse> result = menuPlanService.getMenuPlansByUser(
            userId, page, size, sortBy, sortDirection
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPageNumber()).isEqualTo(page);
        assertThat(result.getPageSize()).isEqualTo(size);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isTrue();

        verify(menuPlanRepository).findByUserId(eq(userId), any(PageRequest.class));
    }

    @Test
    void getMenuPlansByStatus_Success() {
        // Arrange
        MenuPlan.MenuPlanStatus status = MenuPlan.MenuPlanStatus.ACTIVE;
        List<MenuPlan> menuPlans = Arrays.asList(testMenuPlan);

        when(menuPlanRepository.findByUserIdAndStatus(userId, status))
            .thenReturn(menuPlans);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        List<MenuPlanResponse> results = menuPlanService.getMenuPlansByStatus(userId, status);

        // Assert
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(menuPlanId);

        verify(menuPlanRepository).findByUserIdAndStatus(userId, status);
    }

    @Test
    void getActiveMenuPlan_Success() {
        // Arrange
        testMenuPlan.setStatus(MenuPlan.MenuPlanStatus.ACTIVE);

        when(menuPlanRepository.findActiveMenuPlanByUserId(userId))
            .thenReturn(Optional.of(testMenuPlan));
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        MenuPlanResponse result = menuPlanService.getActiveMenuPlan(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(menuPlanId);

        verify(menuPlanRepository).findActiveMenuPlanByUserId(userId);
    }

    @Test
    void getActiveMenuPlan_NotFound_ThrowsException() {
        // Arrange
        when(menuPlanRepository.findActiveMenuPlanByUserId(userId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> menuPlanService.getActiveMenuPlan(userId))
            .isInstanceOf(MenuPlanNotFoundException.class)
            .hasMessage("No active menu plan found for user: " + userId);

        verify(menuPlanRepository).findActiveMenuPlanByUserId(userId);
    }

    @Test
    void getBalancedMenuPlans_Success() {
        // Arrange
        List<MenuPlan> menuPlans = Arrays.asList(testMenuPlan);

        when(menuPlanRepository.findBalancedMenuPlansByUserId(userId))
            .thenReturn(menuPlans);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        List<MenuPlanResponse> results = menuPlanService.getBalancedMenuPlans(userId);

        // Assert
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getIsBalanced()).isTrue();

        verify(menuPlanRepository).findBalancedMenuPlansByUserId(userId);
    }

    @Test
    void updateMenuPlanStatus_Success() {
        // Arrange
        MenuPlan.MenuPlanStatus newStatus = MenuPlan.MenuPlanStatus.ACTIVE;

        when(menuPlanRepository.findById(menuPlanId))
            .thenReturn(Optional.of(testMenuPlan));
        when(menuPlanRepository.save(testMenuPlan))
            .thenReturn(testMenuPlan);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        MenuPlanResponse result = menuPlanService.updateMenuPlanStatus(menuPlanId, newStatus);

        // Assert
        assertThat(result).isNotNull();
        assertThat(testMenuPlan.getStatus()).isEqualTo(newStatus);

        verify(menuPlanRepository).findById(menuPlanId);
        verify(menuPlanRepository).save(testMenuPlan);
    }

    @Test
    void activateMenuPlan_Success() {
        // Arrange
        MenuPlan otherActivePlan = MenuPlan.builder()
            .id(200L)
            .userId(userId)
            .status(MenuPlan.MenuPlanStatus.ACTIVE)
            .build();

        when(menuPlanRepository.findById(menuPlanId))
            .thenReturn(Optional.of(testMenuPlan));
        when(menuPlanRepository.findByUserIdAndStatus(userId, MenuPlan.MenuPlanStatus.ACTIVE))
            .thenReturn(Arrays.asList(otherActivePlan));
        when(menuPlanRepository.save(any(MenuPlan.class)))
            .thenReturn(testMenuPlan);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        MenuPlanResponse result = menuPlanService.activateMenuPlan(menuPlanId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(testMenuPlan.getStatus()).isEqualTo(MenuPlan.MenuPlanStatus.ACTIVE);
        assertThat(otherActivePlan.getStatus()).isEqualTo(MenuPlan.MenuPlanStatus.COMPLETED);

        verify(menuPlanRepository).findById(menuPlanId);
        verify(menuPlanRepository).findByUserIdAndStatus(userId, MenuPlan.MenuPlanStatus.ACTIVE);
        verify(menuPlanRepository, times(2)).save(any(MenuPlan.class));
    }

    @Test
    void activateMenuPlan_NotFound_ThrowsException() {
        // Arrange
        when(menuPlanRepository.findById(menuPlanId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> menuPlanService.activateMenuPlan(menuPlanId))
            .isInstanceOf(MenuPlanNotFoundException.class)
            .hasMessage("Menu plan not found with id: " + menuPlanId);

        verify(menuPlanRepository).findById(menuPlanId);
        verify(menuPlanRepository, never()).save(any());
    }

    @Test
    void deleteMenuPlan_Success() {
        // Arrange
        when(menuPlanRepository.existsById(menuPlanId))
            .thenReturn(true);
        doNothing().when(menuPlanRepository).deleteById(menuPlanId);

        // Act
        menuPlanService.deleteMenuPlan(menuPlanId);

        // Assert
        verify(menuPlanRepository).existsById(menuPlanId);
        verify(menuPlanRepository).deleteById(menuPlanId);
    }

    @Test
    void deleteMenuPlan_NotFound_ThrowsException() {
        // Arrange
        when(menuPlanRepository.existsById(menuPlanId))
            .thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> menuPlanService.deleteMenuPlan(menuPlanId))
            .isInstanceOf(MenuPlanNotFoundException.class)
            .hasMessage("Menu plan not found with id: " + menuPlanId);

        verify(menuPlanRepository).existsById(menuPlanId);
        verify(menuPlanRepository, never()).deleteById(any());
    }

    @Test
    void getMenuPlansByDateRange_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);
        List<MenuPlan> menuPlans = Arrays.asList(testMenuPlan);

        when(menuPlanRepository.findMenuPlansBetweenDates(userId, startDate, endDate))
            .thenReturn(menuPlans);
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        List<MenuPlanResponse> results = menuPlanService.getMenuPlansByDateRange(
            userId, startDate, endDate
        );

        // Assert
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);

        verify(menuPlanRepository).findMenuPlansBetweenDates(userId, startDate, endDate);
    }

    @Test
    void convertToResponse_Success() {
        // Arrange
        when(menuPlanMapper.toResponse(testMenuPlan))
            .thenReturn(testMenuPlanResponse);

        // Act
        MenuPlanResponse result = menuPlanService.convertToResponse(testMenuPlan);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(menuPlanId);

        verify(menuPlanMapper).toResponse(testMenuPlan);
    }
}
