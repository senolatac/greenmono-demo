# API Examples and Usage Guide

This document provides comprehensive examples for using the Weekly Meal Planner API. All examples use `curl` but can be adapted to any HTTP client (Postman, Insomnia, etc.).

## Base URL

```
http://localhost:8080
```

## Authentication

Currently, the API does not require authentication. Future versions will implement JWT-based authentication.

---

## Table of Contents

1. [Ingredient Management](#ingredient-management)
2. [Menu Plan Management](#menu-plan-management)
3. [Simplified Menu API](#simplified-menu-api)
4. [Error Handling](#error-handling)

---

## Ingredient Management

### Create an Ingredient

Create a new ingredient in the user's inventory.

**Endpoint:** `POST /api/ingredients`

**Request Body:**
```json
{
  "name": "Chicken Breast",
  "category": "PROTEIN",
  "quantity": 1000,
  "unit": "GRAM",
  "expiryDate": "2024-03-25",
  "available": true,
  "userId": 1
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/ingredients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Chicken Breast",
    "category": "PROTEIN",
    "quantity": 1000,
    "unit": "GRAM",
    "expiryDate": "2024-03-25",
    "available": true,
    "userId": 1
  }'
```

**Response (201 Created):**
```json
{
  "id": 42,
  "name": "Chicken Breast",
  "category": "PROTEIN",
  "quantity": 1000,
  "unit": "GRAM",
  "expiryDate": "2024-03-25",
  "available": true,
  "userId": 1,
  "createdAt": "2024-03-15T10:35:00"
}
```

**Available Categories:**
- `VEGETABLE`
- `FRUIT`
- `GRAIN`
- `PROTEIN`
- `DAIRY`
- `SPICE`
- `OIL`
- `OTHER`

**Available Units:**
- `GRAM`, `KILOGRAM`, `LITER`, `MILLILITER`, `PIECE`, `TABLESPOON`, `TEASPOON`, `CUP`

---

### Get All Ingredients

Retrieve paginated list of ingredients with optional category filtering.

**Endpoint:** `GET /api/ingredients`

**Query Parameters:**
- `category` (optional) - Filter by ingredient category
- `page` (default: 0) - Page number (0-indexed)
- `size` (default: 20) - Page size
- `sortBy` (default: name) - Sort field
- `sortDirection` (default: ASC) - Sort direction (ASC/DESC)

**cURL Example:**
```bash
# Get all ingredients
curl -X GET "http://localhost:8080/api/ingredients?page=0&size=20"

# Filter by category
curl -X GET "http://localhost:8080/api/ingredients?category=PROTEIN&page=0&size=10"

# Sort by expiry date descending
curl -X GET "http://localhost:8080/api/ingredients?sortBy=expiryDate&sortDirection=DESC"
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 42,
      "name": "Chicken Breast",
      "category": "PROTEIN",
      "quantity": 1000,
      "unit": "GRAM",
      "expiryDate": "2024-03-25",
      "available": true,
      "userId": 1
    },
    {
      "id": 43,
      "name": "Tomato",
      "category": "VEGETABLE",
      "quantity": 500,
      "unit": "GRAM",
      "expiryDate": "2024-03-20",
      "available": true,
      "userId": 1
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

---

### Delete an Ingredient

Delete an ingredient from the inventory.

**Endpoint:** `DELETE /api/ingredients/{id}`

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/ingredients/42
```

**Response (204 No Content):**
No body returned on successful deletion.

---

## Menu Plan Management

### Generate a Balanced Menu Plan

Generate a 5-day balanced menu plan based on available ingredients.

**Endpoint:** `POST /api/menu-plans/generate`

**Request Body:**
```json
{
  "userId": 1,
  "startDate": "2024-03-15",
  "targetDailyCalories": 1800,
  "caloriesPerMealMin": 500,
  "caloriesPerMealMax": 700,
  "notes": "Weekly meal plan for balanced nutrition"
}
```

**Field Descriptions:**
- `userId` - User ID for whom to generate the plan
- `startDate` - Start date of the menu plan (format: yyyy-MM-dd)
- `targetDailyCalories` - Target total calories per day
- `caloriesPerMealMin` - Minimum calories per meal
- `caloriesPerMealMax` - Maximum calories per meal
- `notes` - Optional notes about the menu plan

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/menu-plans/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "startDate": "2024-03-15",
    "targetDailyCalories": 1800,
    "caloriesPerMealMin": 500,
    "caloriesPerMealMax": 700,
    "notes": "Weekly meal plan for balanced nutrition"
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "5-Day Balanced Menu Plan",
  "userId": 1,
  "startDate": "2024-03-15",
  "endDate": "2024-03-19",
  "status": "DRAFT",
  "isBalanced": true,
  "balanceScore": 87.5,
  "totalCalories": 9000,
  "averageDailyCalories": 1800,
  "notes": "Weekly meal plan for balanced nutrition",
  "dailyMealPlans": [
    {
      "id": 101,
      "dayNumber": 1,
      "mealDate": "2024-03-15",
      "breakfastRecipe": {
        "id": 5,
        "name": "Oatmeal with Berries",
        "category": "BREAKFAST",
        "description": "Healthy oatmeal with fresh berries",
        "calories": 550,
        "protein": 20,
        "carbohydrates": 75,
        "fat": 15,
        "servings": 1,
        "cookingTimeMinutes": 15
      },
      "lunchRecipe": {
        "id": 12,
        "name": "Grilled Chicken Salad",
        "category": "MAIN_COURSE",
        "description": "Fresh salad with grilled chicken breast",
        "calories": 600,
        "protein": 35,
        "carbohydrates": 50,
        "fat": 20,
        "servings": 1,
        "cookingTimeMinutes": 25
      },
      "dinnerRecipe": {
        "id": 23,
        "name": "Salmon with Vegetables",
        "category": "MAIN_COURSE",
        "description": "Baked salmon with steamed vegetables",
        "calories": 650,
        "protein": 40,
        "carbohydrates": 55,
        "fat": 25,
        "servings": 1,
        "cookingTimeMinutes": 30
      },
      "totalCalories": 1800,
      "totalProtein": 95,
      "totalCarbohydrates": 180,
      "totalFat": 60
    }
    // ... 4 more daily meal plans (days 2-5)
  ],
  "createdAt": "2024-03-15T10:30:00",
  "updatedAt": "2024-03-15T10:30:00"
}
```

**Balance Score Explanation:**
- Score range: 0-100
- Factors: Macro balance (40%), Calorie consistency (30%), Variety (30%)
- Threshold: Plans with score ≥ 70 are marked as "balanced"
- Higher scores indicate better nutritional balance

---

### Get Menu Plan by ID

Retrieve a specific menu plan by its ID.

**Endpoint:** `GET /api/menu-plans/{id}`

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/menu-plans/1
```

**Response (200 OK):**
Same structure as the generate response above.

---

### Get All Menu Plans for User

Retrieve all menu plans for a specific user with pagination.

**Endpoint:** `GET /api/menu-plans/user/{userId}`

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 20) - Page size
- `sortBy` (default: createdAt) - Sort field
- `sortDirection` (default: desc) - Sort direction

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/menu-plans/user/1?page=0&size=10&sortBy=createdAt&sortDirection=desc"
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "5-Day Balanced Menu Plan",
      "userId": 1,
      "startDate": "2024-03-15",
      "endDate": "2024-03-19",
      "status": "ACTIVE",
      "isBalanced": true,
      "balanceScore": 87.5,
      "totalCalories": 9000,
      "averageDailyCalories": 1800
    },
    {
      "id": 2,
      "name": "5-Day Balanced Menu Plan",
      "userId": 1,
      "startDate": "2024-03-08",
      "endDate": "2024-03-12",
      "status": "COMPLETED",
      "isBalanced": true,
      "balanceScore": 82.3,
      "totalCalories": 8950,
      "averageDailyCalories": 1790
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

---

### Get Menu Plans by Status

Filter menu plans by status (DRAFT, ACTIVE, COMPLETED, ARCHIVED).

**Endpoint:** `GET /api/menu-plans/user/{userId}/status/{status}`

**Available Statuses:**
- `DRAFT` - Newly created, not yet activated
- `ACTIVE` - Currently active menu plan
- `COMPLETED` - Finished menu plan
- `ARCHIVED` - Archived for historical reference

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/menu-plans/user/1/status/ACTIVE
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "5-Day Balanced Menu Plan",
    "userId": 1,
    "startDate": "2024-03-15",
    "endDate": "2024-03-19",
    "status": "ACTIVE",
    "isBalanced": true,
    "balanceScore": 87.5
  }
]
```

---

### Get Active Menu Plan

Retrieve the currently active menu plan for a user.

**Endpoint:** `GET /api/menu-plans/user/{userId}/active`

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/menu-plans/user/1/active
```

**Response (200 OK):**
Full menu plan object (same structure as get by ID).

---

### Get Balanced Menu Plans

Retrieve all menu plans marked as balanced (balance score ≥ 70).

**Endpoint:** `GET /api/menu-plans/user/{userId}/balanced`

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/menu-plans/user/1/balanced
```

**Response (200 OK):**
Array of menu plan summaries.

---

### Get Menu Plans by Date Range

Retrieve menu plans within a specific date range.

**Endpoint:** `GET /api/menu-plans/user/{userId}/date-range`

**Query Parameters:**
- `startDate` (required) - Start date (yyyy-MM-dd)
- `endDate` (required) - End date (yyyy-MM-dd)

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/menu-plans/user/1/date-range?startDate=2024-03-01&endDate=2024-03-31"
```

**Response (200 OK):**
Array of menu plans within the date range.

---

### Update Menu Plan Status

Update the status of a menu plan.

**Endpoint:** `PATCH /api/menu-plans/{id}/status`

**Query Parameters:**
- `status` (required) - New status (DRAFT, ACTIVE, COMPLETED, ARCHIVED)

**cURL Example:**
```bash
curl -X PATCH "http://localhost:8080/api/menu-plans/1/status?status=COMPLETED"
```

**Response (200 OK):**
Updated menu plan object.

---

### Activate Menu Plan

Activate a menu plan and automatically deactivate any other active plans for the user.

**Endpoint:** `POST /api/menu-plans/{id}/activate`

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/menu-plans/1/activate
```

**Response (200 OK):**
Activated menu plan object with status changed to ACTIVE.

---

### Delete Menu Plan

Delete a menu plan and all its associated daily meal plans.

**Endpoint:** `DELETE /api/menu-plans/{id}`

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/menu-plans/1
```

**Response (204 No Content):**
No body returned on successful deletion.

---

## Simplified Menu API

The simplified menu API provides a streamlined format for displaying weekly menus, particularly useful for frontend applications.

### Generate Menu (Simplified Format)

Generate a menu plan and return it in simplified format.

**Endpoint:** `POST /api/menu/generate`

**Request Body:**
Same as `/api/menu-plans/generate`

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/menu/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "startDate": "2024-03-15",
    "targetDailyCalories": 1800,
    "caloriesPerMealMin": 500,
    "caloriesPerMealMax": 700
  }'
```

**Response (201 Created):**
```json
[
  {
    "day": "Pazartesi",
    "date": "15.03.2024",
    "meal": "Oatmeal with Berries"
  },
  {
    "day": "Pazartesi",
    "date": "15.03.2024",
    "meal": "Grilled Chicken Salad"
  },
  {
    "day": "Pazartesi",
    "date": "15.03.2024",
    "meal": "Salmon with Vegetables"
  },
  {
    "day": "Salı",
    "date": "16.03.2024",
    "meal": "Greek Yogurt Parfait"
  },
  {
    "day": "Salı",
    "date": "16.03.2024",
    "meal": "Lentil Soup"
  },
  {
    "day": "Salı",
    "date": "16.03.2024",
    "meal": "Chicken Stir Fry"
  }
  // ... 9 more meals (days 3-5)
]
```

**Format Details:**
- `day` - Day name in Turkish (Pazartesi, Salı, Çarşamba, Perşembe, Cuma)
- `date` - Date formatted as dd.MM.yyyy
- `meal` - Recipe name

---

### Get Current Menu (Simplified Format)

Retrieve the currently active menu in simplified format.

**Endpoint:** `GET /api/menu/current`

**Query Parameters:**
- `userId` (required) - User ID

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/menu/current?userId=1"
```

**Response (200 OK):**
Same array format as generate (simplified format).

---

## Error Handling

The API uses standard HTTP status codes and returns detailed error messages.

### Error Response Format

```json
{
  "timestamp": "2024-03-15T10:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='menuPlanRequest'",
  "path": "/api/menu-plans/generate"
}
```

### Common Error Codes

#### 400 Bad Request
- Invalid input data
- Validation errors
- Missing required fields

**Example:**
```bash
curl -X POST http://localhost:8080/api/menu-plans/generate \
  -H "Content-Type: application/json" \
  -d '{"userId": 1}'  # Missing required fields
```

**Response:**
```json
{
  "timestamp": "2024-03-15T10:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "startDate is required; targetDailyCalories is required"
}
```

#### 404 Not Found
- Resource not found
- Menu plan doesn't exist
- No active menu plan

**Example:**
```bash
curl -X GET http://localhost:8080/api/menu-plans/999
```

**Response:**
```json
{
  "timestamp": "2024-03-15T10:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "Menu plan not found with id: 999"
}
```

#### 409 Conflict
- Ingredient already exists
- Data integrity violation

#### 500 Internal Server Error
- No available ingredients found
- No feasible recipes found
- Insufficient recipes in required categories
- Algorithm failure

**Example - No Feasible Recipes:**
```json
{
  "timestamp": "2024-03-15T10:45:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "No feasible recipes found with available ingredients"
}
```

---

## Testing with Postman

### Import Collection

1. Open Postman
2. Click "Import"
3. Paste the OpenAPI JSON URL: `http://localhost:8080/api-docs`
4. Postman will automatically create a collection with all endpoints

### Environment Variables

Create a Postman environment with:
```
BASE_URL = http://localhost:8080
USER_ID = 1
```

---

## Testing with cURL Scripts

### Complete Workflow Example

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"
USER_ID=1

# Step 1: Create ingredients
echo "Creating ingredients..."
curl -X POST $BASE_URL/api/ingredients -H "Content-Type: application/json" -d '{
  "name": "Chicken Breast", "category": "PROTEIN", "quantity": 1000,
  "unit": "GRAM", "expiryDate": "2024-03-25", "available": true, "userId": '$USER_ID'
}'

curl -X POST $BASE_URL/api/ingredients -H "Content-Type: application/json" -d '{
  "name": "Rice", "category": "GRAIN", "quantity": 2000,
  "unit": "GRAM", "expiryDate": "2024-12-31", "available": true, "userId": '$USER_ID'
}'

# Step 2: List all ingredients
echo "\nListing all ingredients..."
curl -X GET "$BASE_URL/api/ingredients?userId=$USER_ID"

# Step 3: Generate menu plan
echo "\nGenerating menu plan..."
curl -X POST $BASE_URL/api/menu-plans/generate -H "Content-Type: application/json" -d '{
  "userId": '$USER_ID',
  "startDate": "2024-03-15",
  "targetDailyCalories": 1800,
  "caloriesPerMealMin": 500,
  "caloriesPerMealMax": 700,
  "notes": "Test menu plan"
}'

# Step 4: Get current menu (simplified)
echo "\nGetting current menu..."
curl -X GET "$BASE_URL/api/menu/current?userId=$USER_ID"

# Step 5: List all menu plans
echo "\nListing all menu plans..."
curl -X GET "$BASE_URL/api/menu-plans/user/$USER_ID"
```

---

## Rate Limiting and Performance

Currently, the API does not implement rate limiting. For production use, consider:
- Implementing rate limiting (e.g., 100 requests per minute per user)
- Caching frequently accessed menu plans
- Optimizing database queries for large datasets

---

## Support

For questions or issues:
- Check Swagger UI: http://localhost:8080/swagger-ui.html
- Refer to main README.md
- Open an issue on GitHub

---

## Changelog

### Version 1.0.0 (Current)
- Initial API release
- Menu plan generation with balance scoring
- Ingredient management
- Simplified menu format
- Full Swagger documentation
