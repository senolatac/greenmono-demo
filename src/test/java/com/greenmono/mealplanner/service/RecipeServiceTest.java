package com.greenmono.mealplanner.service;

import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.dto.RecipeIngredientRequest;
import com.greenmono.mealplanner.dto.RecipeRequest;
import com.greenmono.mealplanner.dto.RecipeResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.entity.Recipe;
import com.greenmono.mealplanner.entity.RecipeIngredient;
import com.greenmono.mealplanner.exception.DuplicateRecipeException;
import com.greenmono.mealplanner.exception.IngredientNotFoundException;
import com.greenmono.mealplanner.exception.RecipeNotFoundException;
import com.greenmono.mealplanner.mapper.RecipeMapper;
import com.greenmono.mealplanner.repository.IngredientRepository;
import com.greenmono.mealplanner.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeService Unit Tests")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeRequest validRequest;
    private Recipe recipe;
    private RecipeResponse expectedResponse;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = Ingredient.builder()
                .id(1L)
                .name("Soğan")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
                .available(true)
                .build();

        RecipeIngredientRequest ingredientRequest = RecipeIngredientRequest.builder()
                .ingredientId(1L)
                .quantity(new BigDecimal("50.00"))
                .unit(Ingredient.Unit.GRAM)
                .notes("Küp doğranmış")
                .optional(false)
                .build();

        validRequest = RecipeRequest.builder()
                .name("Mantı")
                .description("Geleneksel Türk mantısı")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .ingredients(Arrays.asList(ingredientRequest))
                .instructions(Arrays.asList("Hamuru hazırlayın", "İç harcı ekleyin", "Haşlayın"))
                .cookingTimeMinutes(90)
                .servings(4)
                .calories(new BigDecimal("520.00"))
                .protein(new BigDecimal("28.00"))
                .carbohydrates(new BigDecimal("65.00"))
                .fat(new BigDecimal("18.00"))
                .fiber(new BigDecimal("3.50"))
                .userId(1L)
                .active(true)
                .build();

        recipe = Recipe.builder()
                .id(1L)
                .name("Mantı")
                .description("Geleneksel Türk mantısı")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .recipeIngredients(new ArrayList<>())
                .instructions(Arrays.asList("Hamuru hazırlayın", "İç harcı ekleyin", "Haşlayın"))
                .cookingTimeMinutes(90)
                .servings(4)
                .calories(new BigDecimal("520.00"))
                .protein(new BigDecimal("28.00"))
                .carbohydrates(new BigDecimal("65.00"))
                .fat(new BigDecimal("18.00"))
                .fiber(new BigDecimal("3.50"))
                .userId(1L)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        expectedResponse = RecipeResponse.builder()
                .id(1L)
                .name("Mantı")
                .description("Geleneksel Türk mantısı")
                .category(Recipe.RecipeCategory.MAIN_COURSE)
                .ingredients(new ArrayList<>())
                .instructions(Arrays.asList("Hamuru hazırlayın", "İç harcı ekleyin", "Haşlayın"))
                .cookingTimeMinutes(90)
                .servings(4)
                .calories(new BigDecimal("520.00"))
                .protein(new BigDecimal("28.00"))
                .carbohydrates(new BigDecimal("65.00"))
                .fat(new BigDecimal("18.00"))
                .fiber(new BigDecimal("3.50"))
                .userId(1L)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should successfully create recipe when valid request is provided")
    void shouldCreateRecipeSuccessfully() {
        // Arrange
        when(recipeRepository.existsByUserIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(recipeMapper.toEntity(any(RecipeRequest.class))).thenReturn(recipe);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(recipeMapper.toResponse(any(Recipe.class))).thenReturn(expectedResponse);

        // Act
        RecipeResponse result = recipeService.createRecipe(validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Mantı");
        assertThat(result.getCategory()).isEqualTo(Recipe.RecipeCategory.MAIN_COURSE);
        assertThat(result.getCookingTimeMinutes()).isEqualTo(90);
        assertThat(result.getServings()).isEqualTo(4);
        assertThat(result.getCalories()).isEqualByComparingTo(new BigDecimal("520.00"));

        verify(recipeRepository).existsByUserIdAndNameIgnoreCase(1L, "Mantı");
        verify(recipeMapper).toEntity(validRequest);
        verify(ingredientRepository).findById(1L);
        verify(recipeRepository).save(any(Recipe.class));
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should throw DuplicateRecipeException when recipe already exists for user")
    void shouldThrowDuplicateRecipeExceptionWhenRecipeExists() {
        // Arrange
        when(recipeRepository.existsByUserIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> recipeService.createRecipe(validRequest))
                .isInstanceOf(DuplicateRecipeException.class)
                .hasMessageContaining("Recipe with name 'Mantı' already exists for this user");

        verify(recipeRepository).existsByUserIdAndNameIgnoreCase(1L, "Mantı");
        verify(recipeMapper, never()).toEntity(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IngredientNotFoundException when ingredient does not exist")
    void shouldThrowIngredientNotFoundExceptionWhenIngredientDoesNotExist() {
        // Arrange
        when(recipeRepository.existsByUserIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(recipeMapper.toEntity(any(RecipeRequest.class))).thenReturn(recipe);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> recipeService.createRecipe(validRequest))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining("Ingredient not found with id: 1");

        verify(ingredientRepository).findById(1L);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create recipe without ingredients")
    void shouldCreateRecipeWithoutIngredients() {
        // Arrange
        validRequest.setIngredients(new ArrayList<>());
        when(recipeRepository.existsByUserIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(recipeMapper.toEntity(any(RecipeRequest.class))).thenReturn(recipe);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(recipeMapper.toResponse(any(Recipe.class))).thenReturn(expectedResponse);

        // Act
        RecipeResponse result = recipeService.createRecipe(validRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(ingredientRepository, never()).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    @DisplayName("Should get recipe by id successfully")
    void shouldGetRecipeByIdSuccessfully() {
        // Arrange
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toResponse(any(Recipe.class))).thenReturn(expectedResponse);

        // Act
        RecipeResponse result = recipeService.getRecipeById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Mantı");

        verify(recipeRepository).findById(1L);
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should throw RecipeNotFoundException when recipe not found by id")
    void shouldThrowRecipeNotFoundExceptionWhenRecipeNotFoundById() {
        // Arrange
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> recipeService.getRecipeById(999L))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).findById(999L);
        verify(recipeMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get all active recipes without category filter")
    void shouldGetAllActiveRecipesWithoutCategoryFilter() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(
                Recipe.builder().id(1L).name("Mantı").category(Recipe.RecipeCategory.MAIN_COURSE).active(true).build(),
                Recipe.builder().id(2L).name("Çorba").category(Recipe.RecipeCategory.SOUP).active(true).build()
        );

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "name"));
        Page<Recipe> recipePage = new PageImpl<>(recipes, pageable, recipes.size());

        when(recipeRepository.findByActiveTrue(pageable)).thenReturn(recipePage);
        when(recipeMapper.toResponse(any(Recipe.class)))
                .thenReturn(RecipeResponse.builder().id(1L).name("Mantı").build())
                .thenReturn(RecipeResponse.builder().id(2L).name("Çorba").build());

        // Act
        PageResponse<RecipeResponse> result = recipeService.getRecipes(null, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);

        verify(recipeRepository).findByActiveTrue(pageable);
        verify(recipeMapper, times(2)).toResponse(any(Recipe.class));
    }

    @Test
    @DisplayName("Should get recipes filtered by category")
    void shouldGetRecipesFilteredByCategory() {
        // Arrange
        List<Recipe> soups = Arrays.asList(
                Recipe.builder().id(2L).name("Mercimek Çorbası").category(Recipe.RecipeCategory.SOUP).active(true).build(),
                Recipe.builder().id(3L).name("Ezogelin Çorbası").category(Recipe.RecipeCategory.SOUP).active(true).build()
        );

        Pageable pageable = PageRequest.of(0, 20);
        Page<Recipe> recipePage = new PageImpl<>(soups, pageable, soups.size());

        when(recipeRepository.findByCategoryAndActiveTrue(eq(Recipe.RecipeCategory.SOUP), eq(pageable)))
                .thenReturn(recipePage);
        when(recipeMapper.toResponse(any(Recipe.class)))
                .thenReturn(RecipeResponse.builder().id(2L).name("Mercimek Çorbası").build())
                .thenReturn(RecipeResponse.builder().id(3L).name("Ezogelin Çorbası").build());

        // Act
        PageResponse<RecipeResponse> result = recipeService.getRecipes(Recipe.RecipeCategory.SOUP, pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(recipeRepository).findByCategoryAndActiveTrue(Recipe.RecipeCategory.SOUP, pageable);
        verify(recipeMapper, times(2)).toResponse(any(Recipe.class));
    }

    @Test
    @DisplayName("Should get recipes by ingredient id")
    void shouldGetRecipesByIngredientId() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(
                Recipe.builder().id(1L).name("Mantı").build(),
                Recipe.builder().id(2L).name("Karnıyarık").build()
        );

        when(ingredientRepository.existsById(1L)).thenReturn(true);
        when(recipeRepository.findByIngredientId(1L)).thenReturn(recipes);
        when(recipeMapper.toResponse(any(Recipe.class)))
                .thenReturn(RecipeResponse.builder().id(1L).name("Mantı").build())
                .thenReturn(RecipeResponse.builder().id(2L).name("Karnıyarık").build());

        // Act
        List<RecipeResponse> result = recipeService.getRecipesByIngredient(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(ingredientRepository).existsById(1L);
        verify(recipeRepository).findByIngredientId(1L);
    }

    @Test
    @DisplayName("Should throw IngredientNotFoundException when searching by non-existent ingredient")
    void shouldThrowIngredientNotFoundExceptionWhenSearchingByNonExistentIngredient() {
        // Arrange
        when(ingredientRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> recipeService.getRecipesByIngredient(999L))
                .isInstanceOf(IngredientNotFoundException.class)
                .hasMessageContaining("Ingredient not found with id: 999");

        verify(ingredientRepository).existsById(999L);
        verify(recipeRepository, never()).findByIngredientId(anyLong());
    }

    @Test
    @DisplayName("Should get recipes by cooking time range")
    void shouldGetRecipesByCookingTimeRange() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(
                Recipe.builder().id(1L).name("Salata").cookingTimeMinutes(15).build(),
                Recipe.builder().id(2L).name("Omlet").cookingTimeMinutes(20).build()
        );

        when(recipeRepository.findByCookingTimeRange(10, 30)).thenReturn(recipes);
        when(recipeMapper.toResponse(any(Recipe.class)))
                .thenReturn(RecipeResponse.builder().id(1L).name("Salata").build())
                .thenReturn(RecipeResponse.builder().id(2L).name("Omlet").build());

        // Act
        List<RecipeResponse> result = recipeService.getRecipesByCookingTime(10, 30);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(recipeRepository).findByCookingTimeRange(10, 30);
    }

    @Test
    @DisplayName("Should update recipe successfully")
    void shouldUpdateRecipeSuccessfully() {
        // Arrange
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
        when(recipeMapper.toResponse(any(Recipe.class))).thenReturn(expectedResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(1L, validRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(any(Recipe.class));
        verify(recipeMapper).toResponse(recipe);
    }

    @Test
    @DisplayName("Should throw RecipeNotFoundException when updating non-existent recipe")
    void shouldThrowRecipeNotFoundExceptionWhenUpdatingNonExistentRecipe() {
        // Arrange
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> recipeService.updateRecipe(999L, validRequest))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).findById(999L);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateRecipeException when updating to existing name")
    void shouldThrowDuplicateRecipeExceptionWhenUpdatingToExistingName() {
        // Arrange
        recipe.setName("Old Name");
        validRequest.setName("New Name");

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.existsByUserIdAndNameIgnoreCase(1L, "New Name")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> recipeService.updateRecipe(1L, validRequest))
                .isInstanceOf(DuplicateRecipeException.class)
                .hasMessageContaining("Recipe with name 'New Name' already exists for this user");

        verify(recipeRepository).findById(1L);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete recipe successfully")
    void shouldDeleteRecipeSuccessfully() {
        // Arrange
        when(recipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(1L);

        // Act
        recipeService.deleteRecipe(1L);

        // Assert
        verify(recipeRepository).existsById(1L);
        verify(recipeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw RecipeNotFoundException when deleting non-existent recipe")
    void shouldThrowRecipeNotFoundExceptionWhenDeletingNonExistentRecipe() {
        // Arrange
        when(recipeRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> recipeService.deleteRecipe(999L))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessageContaining("Recipe not found with id: 999");

        verify(recipeRepository).existsById(999L);
        verify(recipeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should search recipes by name")
    void shouldSearchRecipesByName() {
        // Arrange
        List<Recipe> recipes = Arrays.asList(
                Recipe.builder().id(1L).name("Mantı").active(true).build(),
                Recipe.builder().id(2L).name("Kayseri Mantısı").active(true).build()
        );

        Pageable pageable = PageRequest.of(0, 20);
        Page<Recipe> recipePage = new PageImpl<>(recipes, pageable, recipes.size());

        when(recipeRepository.findByNameContainingIgnoreCaseAndActiveTrue("Mantı", pageable))
                .thenReturn(recipePage);
        when(recipeMapper.toResponse(any(Recipe.class)))
                .thenReturn(RecipeResponse.builder().id(1L).name("Mantı").build())
                .thenReturn(RecipeResponse.builder().id(2L).name("Kayseri Mantısı").build());

        // Act
        PageResponse<RecipeResponse> result = recipeService.searchRecipesByName("Mantı", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        verify(recipeRepository).findByNameContainingIgnoreCaseAndActiveTrue("Mantı", pageable);
    }

    @Test
    @DisplayName("Should return empty list when searching for non-existent recipe name")
    void shouldReturnEmptyListWhenSearchingForNonExistentRecipeName() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        Page<Recipe> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(recipeRepository.findByNameContainingIgnoreCaseAndActiveTrue("NonExistent", pageable))
                .thenReturn(emptyPage);

        // Act
        PageResponse<RecipeResponse> result = recipeService.searchRecipesByName("NonExistent", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);

        verify(recipeRepository).findByNameContainingIgnoreCaseAndActiveTrue("NonExistent", pageable);
    }
}
