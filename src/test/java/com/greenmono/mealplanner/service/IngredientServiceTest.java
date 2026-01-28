package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.exception.DuplicateIngredientException;
import com.greenmono.mealplanner.mapper.IngredientMapper;
import com.greenmono.mealplanner.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngredientService Unit Tests")
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    private IngredientRequest validRequest;
    private Ingredient ingredient;
    private IngredientResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = IngredientRequest.builder()
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .expiryDate(LocalDate.now().plusDays(7))
                .notes("Fresh chicken")
                .available(true)
                .userId(1L)
                .build();

        ingredient = Ingredient.builder()
                .id(1L)
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .expiryDate(LocalDate.now().plusDays(7))
                .notes("Fresh chicken")
                .available(true)
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        expectedResponse = IngredientResponse.builder()
                .id(1L)
                .name("Chicken Breast")
                .category(Ingredient.IngredientCategory.POULTRY)
                .quantity(new BigDecimal("500.00"))
                .unit(Ingredient.Unit.GRAM)
                .expiryDate(LocalDate.now().plusDays(7))
                .notes("Fresh chicken")
                .available(true)
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should successfully create ingredient when valid request is provided")
    void shouldCreateIngredientSuccessfully() {
        // Arrange
        when(ingredientRepository.existsByUserIdAndName(anyLong(), anyString())).thenReturn(false);
        when(ingredientMapper.toEntity(any(IngredientRequest.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toResponse(any(Ingredient.class))).thenReturn(expectedResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Chicken Breast");
        assertThat(result.getCategory()).isEqualTo(Ingredient.IngredientCategory.POULTRY);
        assertThat(result.getQuantity()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(result.getUnit()).isEqualTo(Ingredient.Unit.GRAM);

        verify(ingredientRepository).existsByUserIdAndName(1L, "Chicken Breast");
        verify(ingredientMapper).toEntity(validRequest);
        verify(ingredientRepository).save(ingredient);
        verify(ingredientMapper).toResponse(ingredient);
    }

    @Test
    @DisplayName("Should throw DuplicateIngredientException when ingredient already exists for user")
    void shouldThrowDuplicateIngredientExceptionWhenIngredientExists() {
        // Arrange
        when(ingredientRepository.existsByUserIdAndName(anyLong(), anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> ingredientService.createIngredient(validRequest))
                .isInstanceOf(DuplicateIngredientException.class)
                .hasMessageContaining("Ingredient with name 'Chicken Breast' already exists for this user");

        verify(ingredientRepository).existsByUserIdAndName(1L, "Chicken Breast");
        verify(ingredientMapper, never()).toEntity(any());
        verify(ingredientRepository, never()).save(any());
        verify(ingredientMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should create ingredient when userId is null")
    void shouldCreateIngredientWhenUserIdIsNull() {
        // Arrange
        validRequest.setUserId(null);
        ingredient.setUserId(null);
        expectedResponse.setUserId(null);

        when(ingredientMapper.toEntity(any(IngredientRequest.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toResponse(any(Ingredient.class))).thenReturn(expectedResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isNull();

        verify(ingredientRepository, never()).existsByUserIdAndName(anyLong(), anyString());
        verify(ingredientMapper).toEntity(validRequest);
        verify(ingredientRepository).save(ingredient);
        verify(ingredientMapper).toResponse(ingredient);
    }

    @Test
    @DisplayName("Should create ingredient with minimal required fields")
    void shouldCreateIngredientWithMinimalFields() {
        // Arrange
        IngredientRequest minimalRequest = IngredientRequest.builder()
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
                .build();

        Ingredient minimalIngredient = Ingredient.builder()
                .id(2L)
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        IngredientResponse minimalResponse = IngredientResponse.builder()
                .id(2L)
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ingredientMapper.toEntity(any(IngredientRequest.class))).thenReturn(minimalIngredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(minimalIngredient);
        when(ingredientMapper.toResponse(any(Ingredient.class))).thenReturn(minimalResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(minimalRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Tomato");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getExpiryDate()).isNull();
        assertThat(result.getNotes()).isNull();
        assertThat(result.getUserId()).isNull();

        verify(ingredientMapper).toEntity(minimalRequest);
        verify(ingredientRepository).save(minimalIngredient);
        verify(ingredientMapper).toResponse(minimalIngredient);
    }

    @Test
    @DisplayName("Should create ingredient with all optional fields")
    void shouldCreateIngredientWithAllFields() {
        // Arrange
        when(ingredientRepository.existsByUserIdAndName(anyLong(), anyString())).thenReturn(false);
        when(ingredientMapper.toEntity(any(IngredientRequest.class))).thenReturn(ingredient);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(ingredientMapper.toResponse(any(Ingredient.class))).thenReturn(expectedResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getExpiryDate()).isNotNull();
        assertThat(result.getNotes()).isEqualTo("Fresh chicken");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(ingredientRepository).save(ingredient);
    }
}
