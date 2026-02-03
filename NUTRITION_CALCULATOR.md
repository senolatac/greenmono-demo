# Nutrition Calculator Service Documentation

## Overview
The Nutrition Calculator Service provides comprehensive nutritional analysis and calculation capabilities for the meal planning system. It calculates nutrition values from ingredients, validates daily nutrition against requirements, and provides detailed nutrition reports.

## Features

### 1. Recipe Nutrition Calculation
- Automatically calculates total nutrition for recipes based on ingredients
- Aggregates macronutrients and micronutrients
- Handles unit conversions (grams, kilograms, ml, cups, tablespoons, etc.)
- Supports per-serving calculations

### 2. Daily Nutrition Validation
- Validates daily protein intake: **20-30g**
- Validates daily carbohydrate intake: **50-80g**
- Returns balance scores (0-100)
- Provides detailed feedback on nutrition compliance

### 3. Ingredient Nutrition Database
- Comprehensive nutrition data for common ingredients
- Includes macronutrients (protein, carbs, fat, fiber)
- Includes micronutrients (vitamins A, C, D, calcium, iron, potassium)
- Per 100g serving size standardization

## API Endpoints

### Get Ingredient Nutrition
```http
GET /api/nutrition/ingredient/{ingredientId}
```

**Response:**
```json
{
  "calories": 165,
  "protein": 31,
  "carbohydrates": 0,
  "fat": 3.6,
  "fiber": 0,
  "sugar": 0,
  "sodium": 72,
  "cholesterol": 88,
  "saturatedFat": 6,
  "transFat": 0.5,
  "vitaminA": 0,
  "vitaminC": 0,
  "vitaminD": 0.2,
  "calcium": 18,
  "iron": 2.6,
  "potassium": 302,
  "servingSize": 100,
  "servingUnit": "GRAM",
  "notes": "Chicken breast - per 100g"
}
```

### Get Meal Nutrition
```http
GET /api/nutrition/meal/{mealId}
```

### Calculate Recipe Nutrition from Ingredients
```http
GET /api/nutrition/recipe/{recipeId}
```

Calculates total nutrition by summing all ingredients with their quantities.

### Calculate Daily Nutrition
```http
POST /api/nutrition/daily
Content-Type: application/json

{
  "recipeIds": [1, 2, 3]
}
```

**Response:**
```json
{
  "totalCalories": 1050.00,
  "totalProtein": 45.00,
  "totalCarbohydrates": 105.00,
  "totalFat": 26.50,
  "totalFiber": 8.00,
  "proteinInRange": true,
  "carbohydratesInRange": false,
  "balanced": false,
  "proteinMin": 20,
  "proteinMax": 30,
  "carbMin": 50,
  "carbMax": 80,
  "balanceScore": 75.5,
  "message": "Daily nutrition outside recommended ranges"
}
```

## Service Methods

### NutritionCalculatorService

#### calculateRecipeNutrition()
```java
public NutritionalInfo calculateRecipeNutrition(List<RecipeIngredient> recipeIngredients)
```
Calculates total nutrition for a recipe from its ingredients.

**Algorithm:**
1. For each ingredient, get nutritional info
2. Calculate quantity ratio (recipe quantity / serving size)
3. Multiply nutrition values by ratio
4. Sum all values across ingredients

#### calculateDailyNutrition()
```java
public NutritionalInfo calculateDailyNutrition(List<Recipe> recipes)
```
Calculates total daily nutrition from multiple recipes (per serving).

#### validateDailyNutrition()
```java
public boolean validateDailyNutrition(NutritionalInfo dailyNutrition)
```
Validates if daily nutrition meets requirements:
- Protein: 20-30g ✓
- Carbohydrates: 50-80g ✓

#### calculateNutritionBalanceScore()
```java
public double calculateNutritionBalanceScore(NutritionalInfo dailyNutrition)
```
Returns a score from 0-100 based on how well nutrition meets requirements.
- 100: Perfect balance within ranges
- 0-99: Proportional reduction based on deviation

#### calculateCaloriesFromMacros()
```java
public BigDecimal calculateCaloriesFromMacros(BigDecimal protein, BigDecimal carbs, BigDecimal fat)
```
Calculates total calories using standard conversions:
- Protein: 4 kcal/g
- Carbohydrates: 4 kcal/g
- Fat: 9 kcal/g

## Database Schema

### Migration V8: Ingredient Nutrition Data
Located at: `src/main/resources/db/migration/V8__Insert_ingredient_nutrition_data.sql`

**Populated Ingredients (30+ items):**

**Vegetables:**
- Tomato (Domates)
- Onion (Soğan)
- Pepper (Biber)
- Spinach (Ispanak)
- Carrot (Havuç)

**Meat & Poultry:**
- Chicken Breast (Tavuk Göğsü)
- Chicken Thigh (Tavuk But)
- Beef (Dana Eti)
- Ground Beef (Kıyma)

**Dairy:**
- Milk (Süt)
- Yogurt (Yoğurt)
- White Cheese (Beyaz Peynir)
- Butter (Tereyağı)

**Grains & Legumes:**
- Rice (Pirinç)
- Pasta (Makarna)
- Potato (Patates)
- Lentils (Mercimek)
- Chickpeas (Nohut)

**Oils & Fats:**
- Olive Oil (Zeytinyağı)
- Sunflower Oil (Ayçiçek Yağı)

**Others:**
- Eggs (Yumurta)
- Fish (Salmon, Sea Bass)
- Bread (Ekmek, Kepekli Ekmek)
- Fruits (Apple, Banana, Orange)
- Herbs & Spices (Garlic, Cumin, Salt, Pepper)

## Integration with Existing Services

### RecipeService Integration
The RecipeService now automatically calculates nutrition from ingredients when:
1. Creating a new recipe (if nutrition not provided)
2. Updating a recipe with new ingredients

**Code Location:** `src/main/java/com/greenmono/mealplanner/service/RecipeService.java:75-95`

### MenuPlannerService Integration
The MenuPlannerService now validates daily nutrition during menu generation:
1. Calculates daily nutrition for each day
2. Logs nutrition validation status
3. Helps ensure balanced menu plans

**Code Location:** `src/main/java/com/greenmono/mealplanner/service/MenuPlannerService.java:180-191`

## Unit Conversions

The service supports automatic unit conversions:

| Unit | Conversion to Grams |
|------|-------------------|
| GRAM | 1g |
| KILOGRAM | 1000g |
| MILLILITER | 1ml ≈ 1g |
| LITER | 1000ml ≈ 1000g |
| PIECE | 100g (average) |
| TABLESPOON | 15g |
| TEASPOON | 5g |
| CUP | 240g |
| OUNCE | 28.35g |
| POUND | 453.59g |

## Testing

### Unit Tests
Location: `src/test/java/com/greenmono/mealplanner/service/NutritionCalculatorServiceTest.java`

**Test Coverage:**
- ✓ Recipe nutrition calculation
- ✓ Daily nutrition calculation
- ✓ Nutrition validation (within/outside ranges)
- ✓ Balance score calculation
- ✓ Calorie calculation from macros
- ✓ Get ingredient/meal nutrition
- ✓ Empty ingredients handling

### Integration Tests
Location: `src/test/java/com/greenmono/mealplanner/controller/NutritionControllerTest.java`

**Test Coverage:**
- ✓ GET /api/nutrition/ingredient/{id}
- ✓ GET /api/nutrition/meal/{id}
- ✓ GET /api/nutrition/recipe/{id}
- ✓ POST /api/nutrition/daily
- ✓ Error handling (404, 400)
- ✓ Out-of-range nutrition detection

### Running Tests
```bash
# Run all tests
mvn test

# Run only nutrition tests
mvn test -Dtest=NutritionCalculatorServiceTest
mvn test -Dtest=NutritionControllerTest

# Run with coverage
mvn clean test jacoco:report
```

## Usage Examples

### Example 1: Create Recipe with Auto-Calculated Nutrition
```java
RecipeRequest request = RecipeRequest.builder()
    .name("Chicken Rice Bowl")
    .category(Recipe.RecipeCategory.MAIN_COURSE)
    .servings(2)
    .ingredients(Arrays.asList(
        RecipeIngredientRequest.builder()
            .ingredientId(1L) // Chicken
            .quantity(200)
            .unit(Ingredient.Unit.GRAM)
            .build(),
        RecipeIngredientRequest.builder()
            .ingredientId(2L) // Rice
            .quantity(150)
            .unit(Ingredient.Unit.GRAM)
            .build()
    ))
    .build();

// Nutrition will be automatically calculated!
RecipeResponse recipe = recipeService.createRecipe(request);
```

### Example 2: Validate Daily Meal Plan
```java
List<Recipe> dailyMeals = Arrays.asList(breakfast, lunch, dinner);
NutritionalInfo dailyNutrition = nutritionCalculatorService.calculateDailyNutrition(dailyMeals);

boolean isBalanced = nutritionCalculatorService.validateDailyNutrition(dailyNutrition);
double score = nutritionCalculatorService.calculateNutritionBalanceScore(dailyNutrition);

System.out.println("Balanced: " + isBalanced);
System.out.println("Score: " + score);
System.out.println("Protein: " + dailyNutrition.getProtein() + "g");
System.out.println("Carbs: " + dailyNutrition.getCarbohydrates() + "g");
```

## Configuration

### Daily Nutrition Requirements
These constants can be found in `NutritionCalculatorService.java`:

```java
public static final BigDecimal DAILY_PROTEIN_MIN = new BigDecimal("20"); // 20g
public static final BigDecimal DAILY_PROTEIN_MAX = new BigDecimal("30"); // 30g
public static final BigDecimal DAILY_CARB_MIN = new BigDecimal("50");    // 50g
public static final BigDecimal DAILY_CARB_MAX = new BigDecimal("80");    // 80g
```

To modify requirements, update these constants and rebuild.

## Performance Considerations

1. **Caching**: Consider caching ingredient nutrition data
2. **Batch Processing**: Use batch queries when calculating multiple recipes
3. **Database Indexes**: Indexes exist on `ingredient_id` and `meal_id` in `nutritional_info` table
4. **Lazy Loading**: Recipe ingredients use lazy loading to avoid N+1 queries

## Future Enhancements

- [ ] Support for custom daily nutrition goals per user
- [ ] Micronutrient balance scoring
- [ ] Allergen tracking and warnings
- [ ] Nutrition history and trends
- [ ] Import nutrition data from external APIs (USDA, etc.)
- [ ] Meal timing recommendations based on nutrition
- [ ] Sports nutrition profiles (athlete, weight loss, etc.)

## Troubleshooting

### Issue: Nutrition not calculating for recipe
**Solution:** Ensure all ingredients have nutritional info in the database. Run migration V8.

### Issue: Balance score always 0
**Solution:** Check that recipes have valid nutrition values (not null or zero).

### Issue: Unit conversion errors
**Solution:** Verify ingredient units match supported unit types in `Ingredient.Unit` enum.

## License
Part of the Green Mono Meal Planner project.
