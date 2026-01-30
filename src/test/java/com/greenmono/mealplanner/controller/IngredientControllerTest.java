package com.greenmono.mealplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenmono.mealplanner.dto.IngredientRequest;
import com.greenmono.mealplanner.dto.IngredientResponse;
import com.greenmono.mealplanner.dto.PageResponse;
import com.greenmono.mealplanner.entity.Ingredient;
import com.greenmono.mealplanner.exception.DuplicateIngredientException;
import com.greenmono.mealplanner.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
@ActiveProfiles("test")
@DisplayName("IngredientController Integration Tests")
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IngredientService ingredientService;

    private IngredientRequest validRequest;
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
    @DisplayName("Should return 201 CREATED when valid ingredient is submitted")
    void shouldReturnCreatedWhenValidIngredient() throws Exception {
        // Arrange
        when(ingredientService.createIngredient(any(IngredientRequest.class)))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Chicken Breast")))
                .andExpect(jsonPath("$.category", is("POULTRY")))
                .andExpect(jsonPath("$.quantity", is(500.00)))
                .andExpect(jsonPath("$.unit", is("GRAM")))
                .andExpect(jsonPath("$.notes", is("Fresh chicken")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when name is blank")
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        // Arrange
        validRequest.setName("");

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.name", containsString("Ingredient name is required")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when category is null")
    void shouldReturnBadRequestWhenCategoryIsNull() throws Exception {
        // Arrange
        validRequest.setCategory(null);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.category", containsString("Category is required")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when quantity is null")
    void shouldReturnBadRequestWhenQuantityIsNull() throws Exception {
        // Arrange
        validRequest.setQuantity(null);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.quantity", containsString("Quantity is required")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when quantity is negative")
    void shouldReturnBadRequestWhenQuantityIsNegative() throws Exception {
        // Arrange
        validRequest.setQuantity(new BigDecimal("-10.00"));

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.quantity", containsString("Quantity must be positive")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when unit is null")
    void shouldReturnBadRequestWhenUnitIsNull() throws Exception {
        // Arrange
        validRequest.setUnit(null);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.unit", containsString("Unit is required")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when name exceeds 100 characters")
    void shouldReturnBadRequestWhenNameTooLong() throws Exception {
        // Arrange
        validRequest.setName("A".repeat(101));

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.name", containsString("Name must not exceed 100 characters")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when notes exceed 500 characters")
    void shouldReturnBadRequestWhenNotesTooLong() throws Exception {
        // Arrange
        validRequest.setNotes("N".repeat(501));

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.notes", containsString("Notes must not exceed 500 characters")));
    }

    @Test
    @DisplayName("Should return 409 CONFLICT when duplicate ingredient is submitted")
    void shouldReturnConflictWhenDuplicateIngredient() throws Exception {
        // Arrange
        when(ingredientService.createIngredient(any(IngredientRequest.class)))
                .thenThrow(new DuplicateIngredientException("Ingredient with name 'Chicken Breast' already exists for this user"));

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", containsString("Ingredient with name 'Chicken Breast' already exists")));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when multiple validation errors occur")
    void shouldReturnBadRequestWithMultipleValidationErrors() throws Exception {
        // Arrange
        validRequest.setName("");
        validRequest.setCategory(null);
        validRequest.setQuantity(null);
        validRequest.setUnit(null);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.validationErrors.name", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.category", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.quantity", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.unit", notNullValue()));
    }

    @Test
    @DisplayName("Should successfully create ingredient with only required fields")
    void shouldCreateIngredientWithMinimalFields() throws Exception {
        // Arrange
        IngredientRequest minimalRequest = IngredientRequest.builder()
                .name("Tomato")
                .category(Ingredient.IngredientCategory.VEGETABLES)
                .quantity(new BigDecimal("100.00"))
                .unit(Ingredient.Unit.GRAM)
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

        when(ingredientService.createIngredient(any(IngredientRequest.class)))
                .thenReturn(minimalResponse);

        // Act & Assert
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Tomato")))
                .andExpect(jsonPath("$.category", is("VEGETABLES")))
                .andExpect(jsonPath("$.quantity", is(100.00)))
                .andExpect(jsonPath("$.unit", is("GRAM")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @DisplayName("Should return 200 OK with paginated ingredients without category filter")
    void shouldGetAllIngredientsWithoutCategoryFilter() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                .content(Arrays.asList(
                        IngredientResponse.builder()
                                .id(1L)
                                .name("Chicken Breast")
                                .category(Ingredient.IngredientCategory.POULTRY)
                                .quantity(new BigDecimal("500.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build(),
                        IngredientResponse.builder()
                                .id(2L)
                                .name("Tomato")
                                .category(Ingredient.IngredientCategory.VEGETABLES)
                                .quantity(new BigDecimal("200.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build()
                ))
                .pageNumber(0)
                .pageSize(20)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(null), any(Pageable.class)))
                .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortBy", "name")
                        .param("sortDirection", "ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Chicken Breast")))
                .andExpect(jsonPath("$.content[1].name", is("Tomato")))
                .andExpect(jsonPath("$.pageNumber", is(0)))
                .andExpect(jsonPath("$.pageSize", is(20)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.last", is(true)));
    }

    @Test
    @DisplayName("Should return 200 OK with ingredients filtered by category")
    void shouldGetIngredientsFilteredByCategory() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                .content(Arrays.asList(
                        IngredientResponse.builder()
                                .id(2L)
                                .name("Tomato")
                                .category(Ingredient.IngredientCategory.VEGETABLES)
                                .quantity(new BigDecimal("200.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build(),
                        IngredientResponse.builder()
                                .id(3L)
                                .name("Carrot")
                                .category(Ingredient.IngredientCategory.VEGETABLES)
                                .quantity(new BigDecimal("150.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build()
                ))
                .pageNumber(0)
                .pageSize(20)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(Ingredient.IngredientCategory.VEGETABLES), any(Pageable.class)))
                .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients")
                        .param("category", "VEGETABLES")
                        .param("page", "0")
                        .param("size", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].category", is("VEGETABLES")))
                .andExpect(jsonPath("$.content[1].category", is("VEGETABLES")))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    @DisplayName("Should return 200 OK with empty page when no ingredients found")
    void shouldReturnEmptyPageWhenNoIngredientsFound() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> emptyPage = PageResponse.<IngredientResponse>builder()
                .content(Collections.emptyList())
                .pageNumber(0)
                .pageSize(20)
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(null), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)))
                .andExpect(jsonPath("$.totalPages", is(0)));
    }

    @Test
    @DisplayName("Should use default pagination parameters when not provided")
    void shouldUseDefaultPaginationParameters() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                .content(Arrays.asList(
                        IngredientResponse.builder()
                                .id(1L)
                                .name("Apple")
                                .category(Ingredient.IngredientCategory.FRUITS)
                                .quantity(new BigDecimal("300.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build()
                ))
                .pageNumber(0)
                .pageSize(20)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(null), any(Pageable.class)))
                .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageNumber", is(0)))
                .andExpect(jsonPath("$.pageSize", is(20)));
    }

    @Test
    @DisplayName("Should handle custom page size and page number")
    void shouldHandleCustomPageSizeAndNumber() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                .content(Arrays.asList(
                        IngredientResponse.builder()
                                .id(11L)
                                .name("Ingredient 11")
                                .category(Ingredient.IngredientCategory.OTHER)
                                .quantity(new BigDecimal("100.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build()
                ))
                .pageNumber(2)
                .pageSize(5)
                .totalElements(15)
                .totalPages(3)
                .first(false)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(null), any(Pageable.class)))
                .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients")
                        .param("page", "2")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageNumber", is(2)))
                .andExpect(jsonPath("$.pageSize", is(5)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.first", is(false)))
                .andExpect(jsonPath("$.last", is(true)));
    }

    @Test
    @DisplayName("Should sort ingredients in descending order")
    void shouldSortIngredientsInDescendingOrder() throws Exception {
        // Arrange
        PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                .content(Arrays.asList(
                        IngredientResponse.builder()
                                .id(2L)
                                .name("Zucchini")
                                .category(Ingredient.IngredientCategory.VEGETABLES)
                                .quantity(new BigDecimal("200.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build(),
                        IngredientResponse.builder()
                                .id(1L)
                                .name("Apple")
                                .category(Ingredient.IngredientCategory.FRUITS)
                                .quantity(new BigDecimal("300.00"))
                                .unit(Ingredient.Unit.GRAM)
                                .available(true)
                                .build()
                ))
                .pageNumber(0)
                .pageSize(20)
                .totalElements(2)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        when(ingredientService.getIngredients(eq(null), any(Pageable.class)))
                .thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ingredients")
                        .param("sortBy", "name")
                        .param("sortDirection", "DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name", is("Zucchini")))
                .andExpect(jsonPath("$.content[1].name", is("Apple")));
    }
}
