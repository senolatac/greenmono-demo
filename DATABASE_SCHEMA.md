# Database Schema Documentation

## Overview
This document describes the database schema for the Weekly Meal Planner application. The schema is designed to support intelligent meal planning based on available ingredients, nutritional tracking, and 5-day balanced menu generation.

## Entity Relationship Diagram

```
┌─────────────────┐       ┌──────────────────┐       ┌─────────────────┐
│   Ingredients   │───────│ Nutritional Info │───────│      Meals      │
│                 │ 1   1 │                  │ 1   1 │                 │
│ - id            │       │ - id             │       │ - id            │
│ - name          │       │ - calories       │       │ - name          │
│ - category      │       │ - protein        │       │ - meal_type     │
│ - quantity      │       │ - carbohydrates  │       │ - prep_time     │
│ - unit          │       │ - fat            │       │ - cook_time     │
│ - expiry_date   │       │ - fiber          │       │ - servings      │
│ - available     │       │ - vitamins       │       │ - difficulty    │
│ - user_id       │       │ - minerals       │       │ - instructions  │
└─────────────────┘       └──────────────────┘       └─────────────────┘
                                                               │
                                                               │ M:N
                                                               │
                          ┌─────────────────┐         ┌───────▼────────┐
                          │ Daily Meal Plan │◄────────│   Menu Plans   │
                          │                 │ 1     M │                │
                          │ - id            │         │ - id           │
                          │ - day_number    │         │ - name         │
                          │ - meal_date     │         │ - start_date   │
                          │ - breakfast_id  │         │ - end_date     │
                          │ - lunch_id      │         │ - status       │
                          │ - dinner_id     │         │ - is_balanced  │
                          │ - snack_id      │         │ - balance_score│
                          │ - total_calories│         │ - user_id      │
                          └─────────────────┘         └────────────────┘
```

## Tables

### 1. ingredients

Stores all ingredients available to users for meal planning.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Unique identifier |
| name | VARCHAR(100) | NOT NULL | Ingredient name |
| category | VARCHAR(50) | NOT NULL | Category (VEGETABLES, FRUITS, MEAT, etc.) |
| quantity | DECIMAL(10,2) | NOT NULL | Available quantity |
| unit | VARCHAR(20) | NOT NULL | Unit of measurement |
| expiry_date | DATE | NULLABLE | Expiration date |
| notes | VARCHAR(500) | NULLABLE | Additional notes |
| available | BOOLEAN | NOT NULL, DEFAULT TRUE | Availability status |
| user_id | BIGINT | NULLABLE | Owner user ID |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_ingredient_name` on name
- `idx_ingredient_category` on category
- `idx_ingredient_expiry` on expiry_date
- `idx_ingredient_user` on user_id

**Categories:**
- VEGETABLES
- FRUITS
- MEAT
- POULTRY
- FISH
- SEAFOOD
- DAIRY
- GRAINS
- LEGUMES
- NUTS_SEEDS
- HERBS_SPICES
- OILS_FATS
- CONDIMENTS
- BEVERAGES
- OTHER

**Units:**
- GRAM, KILOGRAM
- MILLILITER, LITER
- PIECE
- TABLESPOON, TEASPOON, CUP
- OUNCE, POUND

### 2. meals

Stores meal recipes with cooking instructions and metadata.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Unique identifier |
| name | VARCHAR(200) | NOT NULL | Meal name |
| description | VARCHAR(2000) | NULLABLE | Meal description |
| meal_type | VARCHAR(20) | NOT NULL | Type (BREAKFAST, LUNCH, etc.) |
| prep_time_minutes | INTEGER | NOT NULL | Preparation time |
| cook_time_minutes | INTEGER | NOT NULL | Cooking time |
| servings | INTEGER | NOT NULL | Number of servings |
| difficulty_level | VARCHAR(20) | NOT NULL | Difficulty (EASY, MEDIUM, HARD) |
| image_url | VARCHAR(500) | NULLABLE | Image URL |
| user_id | BIGINT | NULLABLE | Creator user ID |
| active | BOOLEAN | NOT NULL, DEFAULT TRUE | Active status |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_meal_name` on name
- `idx_meal_type` on meal_type
- `idx_meal_difficulty` on difficulty_level
- `idx_meal_user` on user_id

**Meal Types:**
- BREAKFAST
- LUNCH
- DINNER
- SNACK
- DESSERT

**Difficulty Levels:**
- EASY
- MEDIUM
- HARD

#### Related Tables:

**meal_ingredients** (Many-to-Many relationship)
- meal_id (FK to meals)
- ingredient_id (FK to ingredients)

**meal_instructions** (One-to-Many)
- meal_id (FK to meals)
- instruction (VARCHAR(1000))
- step_order (INTEGER)

**meal_tags** (One-to-Many)
- meal_id (FK to meals)
- tag (VARCHAR(50))

### 3. nutritional_info

Stores comprehensive nutritional information for ingredients or meals.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Unique identifier |
| ingredient_id | BIGINT | UNIQUE, FK | Reference to ingredient |
| meal_id | BIGINT | UNIQUE, FK | Reference to meal |
| serving_size | DECIMAL(10,2) | NOT NULL | Serving size |
| serving_unit | VARCHAR(20) | NOT NULL | Unit of measurement |
| calories | DECIMAL(10,2) | NOT NULL | Calories per serving |
| protein | DECIMAL(10,2) | NOT NULL | Protein (grams) |
| carbohydrates | DECIMAL(10,2) | NOT NULL | Carbs (grams) |
| fat | DECIMAL(10,2) | NOT NULL | Fat (grams) |
| fiber | DECIMAL(10,2) | NULLABLE | Fiber (grams) |
| sugar | DECIMAL(10,2) | NULLABLE | Sugar (grams) |
| sodium | DECIMAL(10,2) | NULLABLE | Sodium (mg) |
| cholesterol | DECIMAL(10,2) | NULLABLE | Cholesterol (mg) |
| saturated_fat | DECIMAL(10,2) | NULLABLE | Saturated fat (grams) |
| trans_fat | DECIMAL(10,2) | NULLABLE | Trans fat (grams) |
| vitamin_a | DECIMAL(10,2) | NULLABLE | Vitamin A (mcg) |
| vitamin_c | DECIMAL(10,2) | NULLABLE | Vitamin C (mg) |
| vitamin_d | DECIMAL(10,2) | NULLABLE | Vitamin D (mcg) |
| calcium | DECIMAL(10,2) | NULLABLE | Calcium (mg) |
| iron | DECIMAL(10,2) | NULLABLE | Iron (mg) |
| potassium | DECIMAL(10,2) | NULLABLE | Potassium (mg) |
| notes | VARCHAR(500) | NULLABLE | Additional notes |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Constraints:**
- Either ingredient_id OR meal_id must be set (not both)
- CHECK constraint ensures mutual exclusivity

**Indexes:**
- `idx_nutritional_ingredient` on ingredient_id
- `idx_nutritional_meal` on meal_id
- `idx_nutritional_calories` on calories

### 4. menu_plans

Stores weekly meal plans for users.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Unique identifier |
| name | VARCHAR(200) | NOT NULL | Plan name |
| description | VARCHAR(1000) | NULLABLE | Plan description |
| user_id | BIGINT | NOT NULL | Owner user ID |
| start_date | DATE | NOT NULL | Plan start date |
| end_date | DATE | NOT NULL | Plan end date |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'DRAFT' | Plan status |
| total_calories | INTEGER | NULLABLE | Total calories for entire plan |
| average_daily_calories | INTEGER | NULLABLE | Average daily calories |
| notes | VARCHAR(2000) | NULLABLE | Additional notes |
| is_balanced | BOOLEAN | NOT NULL, DEFAULT FALSE | Nutritional balance flag |
| balance_score | DOUBLE PRECISION | NULLABLE | Balance score (0-100) |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_menu_plan_user` on user_id
- `idx_menu_plan_start_date` on start_date
- `idx_menu_plan_status` on status

**Statuses:**
- DRAFT - Being created
- ACTIVE - Currently in use
- COMPLETED - Finished
- ARCHIVED - Archived for reference

#### Related Tables:

**menu_plan_meals** (Many-to-Many relationship)
- menu_plan_id (FK to menu_plans)
- meal_id (FK to meals)

### 5. daily_meal_plans

Stores individual day assignments within a menu plan.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGSERIAL | PRIMARY KEY | Unique identifier |
| menu_plan_id | BIGINT | NOT NULL, FK | Reference to menu plan |
| day_number | INTEGER | NOT NULL | Day number (1-5 for 5-day plan) |
| meal_date | DATE | NOT NULL | Actual date for this day |
| breakfast_meal_id | BIGINT | NULLABLE, FK | Breakfast meal reference |
| lunch_meal_id | BIGINT | NULLABLE, FK | Lunch meal reference |
| dinner_meal_id | BIGINT | NULLABLE, FK | Dinner meal reference |
| snack_meal_id | BIGINT | NULLABLE, FK | Snack meal reference |
| total_calories | INTEGER | NULLABLE | Total calories for the day |
| notes | VARCHAR(1000) | NULLABLE | Additional notes |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Constraints:**
- UNIQUE (menu_plan_id, day_number)
- UNIQUE (menu_plan_id, meal_date)

**Indexes:**
- `idx_daily_meal_plan_menu` on menu_plan_id
- `idx_daily_meal_plan_date` on meal_date
- `idx_daily_meal_plan_day` on day_number

## Relationships

1. **Ingredient ↔ NutritionalInfo**: One-to-One
   - Each ingredient can have one nutritional info record

2. **Meal ↔ NutritionalInfo**: One-to-One
   - Each meal can have one nutritional info record

3. **Ingredient ↔ Meal**: Many-to-Many (via meal_ingredients)
   - Ingredients are used in multiple meals
   - Meals contain multiple ingredients

4. **MenuPlan ↔ Meal**: Many-to-Many (via menu_plan_meals)
   - Menu plans can include multiple meals
   - Meals can be part of multiple menu plans

5. **MenuPlan ↔ DailyMealPlan**: One-to-Many
   - Each menu plan has multiple daily meal plans (typically 5)
   - Each daily meal plan belongs to one menu plan

6. **Meal ↔ DailyMealPlan**: Many-to-One
   - Each meal can be assigned to multiple daily meal plans
   - Each daily meal plan references specific meals for each meal type

## Key Features

### 1. Nutritional Tracking
- Comprehensive macro and micronutrient tracking
- Support for both ingredient-level and meal-level nutrition
- Calculated fields for nutritional analysis

### 2. Intelligent Meal Planning
- Track ingredient availability and expiry dates
- Support for 5-day balanced menu generation
- Balance scoring for nutritional optimization

### 3. Multi-User Support
- User-specific ingredients and custom meals
- Shared meal recipes across users
- Personal menu plans

### 4. Flexible Meal Structure
- Support for multiple meal types per day
- Customizable serving sizes
- Recipe instructions with ordered steps
- Tagging system for categorization

## Database Initialization

The database schema is managed using Flyway migrations located in `src/main/resources/db/migration/`:

1. **V1__Create_ingredients_table.sql** - Ingredients table and indexes
2. **V2__Create_meals_table.sql** - Meals and related tables
3. **V3__Create_nutritional_info_table.sql** - Nutritional information
4. **V4__Create_menu_plans_tables.sql** - Menu plans and daily meal plans

## Performance Considerations

- All foreign keys are indexed for efficient joins
- Frequently queried columns (user_id, dates, status) have dedicated indexes
- Cascade deletes ensure referential integrity
- Timestamps are automatically managed via JPA auditing

## Security Considerations

- User isolation via user_id columns
- Soft deletes via 'active' flags where appropriate
- Input validation at entity level via Bean Validation annotations
