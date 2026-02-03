# Weekly Meal Planner

An intelligent meal planning system that creates balanced 5-day menus based on available ingredients. The system uses a sophisticated algorithm to ensure nutritional balance, variety, and optimal use of available ingredients.

## Features

- Ingredient management with expiry tracking and categorization
- Comprehensive meal recipe database with nutritional information
- Nutritional information tracking (macros: protein, carbs, fat)
- Intelligent 5-day menu planning algorithm with:
  - Protein-carb balance optimization (20-35% protein, 45-65% carbs)
  - No consecutive day recipe repetition
  - Calorie range targeting (customizable per meal)
  - Variety scoring across the week
- Multi-user support with user-specific ingredients and recipes
- RESTful API with comprehensive Swagger documentation
- Frontend interface for easy ingredient and menu management

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use H2 for testing)
- Git

### Installation

```bash
# Clone the repository
git clone https://github.com/senolatac/greenmono-demo.git
cd weekly-meal-planner

# Build the project
mvn clean install

# Run the application (development profile)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or run tests
mvn test
```

The application will start on `http://localhost:8080`

### Quick Test

```bash
# Access the Swagger UI to test the API
open http://localhost:8080/swagger-ui.html

# Access the frontend interface
open http://localhost:8080/index.html
```

## Documentation

- [Project Setup Guide](PROJECT_SETUP.md) - Detailed setup and configuration instructions
- [Database Schema](DATABASE_SCHEMA.md) - Complete database schema documentation
- [API Examples](API_EXAMPLES.md) - Comprehensive API usage examples with cURL commands
- [API Documentation](#api-documentation) - API endpoints and examples (see below)

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.1
- **Database**: PostgreSQL (production), H2 (testing)
- **ORM**: Spring Data JPA, Hibernate
- **Migration**: Flyway
- **Code Generation**: Lombok, MapStruct
- **API Documentation**: Springdoc OpenAPI 3.x (Swagger)
- **Testing**: JUnit 5, Mockito, AssertJ
- **Build Tool**: Maven 3.x
- **Frontend**: Vanilla JavaScript, HTML5, CSS3

## Project Structure

The project follows a standard Spring Boot layered architecture:

```
weekly-meal-planner/
├── src/
│   ├── main/
│   │   ├── java/com/greenmono/mealplanner/
│   │   │   ├── entity/              # JPA entity classes
│   │   │   │   ├── Ingredient.java
│   │   │   │   ├── Meal.java
│   │   │   │   ├── NutritionalInfo.java
│   │   │   │   ├── MenuPlan.java
│   │   │   │   └── DailyMealPlan.java
│   │   │   ├── repository/          # Spring Data JPA repositories
│   │   │   │   ├── IngredientRepository.java
│   │   │   │   ├── MealRepository.java
│   │   │   │   ├── NutritionalInfoRepository.java
│   │   │   │   ├── MenuPlanRepository.java
│   │   │   │   └── DailyMealPlanRepository.java
│   │   │   └── MealPlannerApplication.java  # Main application class
│   │   └── resources/
│   │       ├── db/migration/        # Flyway database migrations
│   │       │   ├── V1__Create_ingredients_table.sql
│   │       │   ├── V2__Create_meals_table.sql
│   │       │   ├── V3__Create_nutritional_info_table.sql
│   │       │   └── V4__Create_menu_plans_tables.sql
│   │       ├── application.yml      # Main configuration
│   │       └── application-dev.yml  # Development profile configuration
│   └── test/
│       └── resources/
│           └── application-test.yml # Test profile configuration
├── pom.xml                          # Maven project configuration
├── PROJECT_SETUP.md                 # Detailed setup instructions
├── DATABASE_SCHEMA.md               # Database schema documentation
└── README.md                        # This file
```

### Key Components

- **Entity Layer**: JPA entities representing database tables with relationships and constraints
- **Repository Layer**: Spring Data JPA repositories with custom query methods for data access
- **Database Migrations**: Flyway versioned scripts for schema management and evolution
- **Configuration**: Profile-based configuration (dev, test) for different environments

## API Documentation

The application provides a comprehensive RESTful API with full Swagger/OpenAPI documentation.

### Accessing API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Available API Endpoints

#### Menu Plan Management (`/api/menu-plans`)

- `POST /api/menu-plans/generate` - Generate a balanced 5-day menu plan
- `GET /api/menu-plans/{id}` - Get menu plan by ID
- `GET /api/menu-plans/user/{userId}` - Get all menu plans for a user (paginated)
- `GET /api/menu-plans/user/{userId}/status/{status}` - Filter menu plans by status
- `GET /api/menu-plans/user/{userId}/active` - Get currently active menu plan
- `GET /api/menu-plans/user/{userId}/balanced` - Get all balanced menu plans
- `GET /api/menu-plans/user/{userId}/date-range` - Get menu plans by date range
- `PATCH /api/menu-plans/{id}/status` - Update menu plan status
- `POST /api/menu-plans/{id}/activate` - Activate a menu plan
- `DELETE /api/menu-plans/{id}` - Delete a menu plan

#### Simplified Menu API (`/api/menu`)

- `POST /api/menu/generate` - Generate menu (simplified format)
- `GET /api/menu/current` - Get current weekly menu (simplified format)

#### Ingredient Management (`/api/ingredients`)

- `POST /api/ingredients` - Create a new ingredient
- `GET /api/ingredients` - Get all ingredients (paginated, filterable by category)
- `DELETE /api/ingredients/{id}` - Delete an ingredient

### Example API Requests and Responses

#### 1. Generate a Balanced Menu Plan

**Request:**
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

**Response:**
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
        "calories": 550,
        "protein": 20,
        "carbohydrates": 75,
        "fat": 15
      },
      "lunchRecipe": {
        "id": 12,
        "name": "Grilled Chicken Salad",
        "category": "MAIN_COURSE",
        "calories": 600,
        "protein": 35,
        "carbohydrates": 50,
        "fat": 20
      },
      "dinnerRecipe": {
        "id": 23,
        "name": "Salmon with Vegetables",
        "category": "MAIN_COURSE",
        "calories": 650,
        "protein": 40,
        "carbohydrates": 55,
        "fat": 25
      },
      "totalCalories": 1800,
      "totalProtein": 95,
      "totalCarbohydrates": 180,
      "totalFat": 60
    }
    // ... 4 more daily meal plans
  ],
  "createdAt": "2024-03-15T10:30:00",
  "updatedAt": "2024-03-15T10:30:00"
}
```

#### 2. Create an Ingredient

**Request:**
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

**Response:**
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

#### 3. Get Current Active Menu (Simplified)

**Request:**
```bash
curl -X GET "http://localhost:8080/api/menu/current?userId=1"
```

**Response:**
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
  }
  // ... more meals for the week
]
```

#### 4. Get User's Menu Plans with Pagination

**Request:**
```bash
curl -X GET "http://localhost:8080/api/menu-plans/user/1?page=0&size=10&sortBy=createdAt&sortDirection=desc"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "5-Day Balanced Menu Plan",
      "startDate": "2024-03-15",
      "endDate": "2024-03-19",
      "status": "ACTIVE",
      "isBalanced": true,
      "balanceScore": 87.5
    },
    {
      "id": 2,
      "name": "5-Day Balanced Menu Plan",
      "startDate": "2024-03-08",
      "endDate": "2024-03-12",
      "status": "COMPLETED",
      "isBalanced": true,
      "balanceScore": 82.3
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

## Database Schema

The application includes 7 core tables:
- **ingredients** - User ingredient inventory with expiry tracking
- **recipes** - Recipe database with cooking instructions
- **recipe_ingredients** - Many-to-many relationship between recipes and ingredients
- **nutritional_info** - Nutritional data (deprecated, migrated to recipes table)
- **menu_plans** - Weekly menu plans with balance scoring
- **daily_meal_plans** - Daily meal assignments (breakfast, lunch, dinner)
- **meals** - Legacy table for meal tracking

For detailed schema information, see [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md).

## How the Menu Planning Algorithm Works

The intelligent menu planning algorithm follows these key principles:

1. **Ingredient Feasibility**: Only uses recipes that can be made with available ingredients
2. **Nutritional Balance**:
   - Protein: 20-35% of daily calories
   - Carbohydrates: 45-65% of daily calories
   - Fat: Remaining percentage
3. **Calorie Targeting**: Each meal stays within specified calorie range (default: 500-700 kcal)
4. **Variety**: No recipe is repeated on consecutive days
5. **Balance Scoring**: Multi-factor score (40% macro balance, 30% calorie consistency, 30% variety)
6. **Minimum Quality**: Only plans with balance score ≥ 70.0 are marked as "balanced"

## Configuration

### Application Profiles

The application supports multiple profiles:

- **dev** - Development profile (H2 in-memory database)
- **test** - Test profile (H2 in-memory database)
- **default** - Production profile (PostgreSQL)

### Environment Variables

Configure the following environment variables for production:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=meal_planner
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Application Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=default
```

### Configuration Files

- `application.yml` - Production configuration
- `application-dev.yml` - Development configuration
- `application-test.yml` - Test configuration

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MenuPlannerServiceTest

# Run with coverage report
mvn test jacoco:report
```

### Test Coverage

The application includes comprehensive test coverage:

- **Unit Tests**: Service layer tests with Mockito mocks
- **Integration Tests**: Controller tests with MockMvc
- **Repository Tests**: JPA repository tests with test data

Key test classes:
- `MenuPlannerServiceTest` - 14 test cases covering menu generation algorithm
- `MenuPlanServiceTest` - CRUD operations and business logic
- `RecipeServiceTest` - Recipe management operations
- `IngredientServiceTest` - Ingredient management operations

## Project Structure

```
weekly-meal-planner/
├── src/
│   ├── main/
│   │   ├── java/com/greenmono/mealplanner/
│   │   │   ├── controller/          # REST API controllers
│   │   │   │   ├── MenuPlanController.java
│   │   │   │   ├── MenuController.java
│   │   │   │   └── IngredientController.java
│   │   │   ├── service/             # Business logic layer
│   │   │   │   ├── MenuPlannerService.java
│   │   │   │   ├── MenuPlanService.java
│   │   │   │   ├── RecipeService.java
│   │   │   │   └── IngredientService.java
│   │   │   ├── repository/          # Data access layer
│   │   │   │   ├── MenuPlanRepository.java
│   │   │   │   ├── RecipeRepository.java
│   │   │   │   └── IngredientRepository.java
│   │   │   ├── entity/              # JPA entities
│   │   │   │   ├── MenuPlan.java
│   │   │   │   ├── DailyMealPlan.java
│   │   │   │   ├── Recipe.java
│   │   │   │   ├── Ingredient.java
│   │   │   │   └── RecipeIngredient.java
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── mapper/              # MapStruct mappers
│   │   │   ├── exception/           # Exception handling
│   │   │   ├── config/              # Application configuration
│   │   │   └── MealPlannerApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway migrations
│   │       ├── static/              # Frontend assets
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-test.yml
│   └── test/
│       ├── java/com/greenmono/mealplanner/
│       │   ├── service/             # Service tests
│       │   ├── controller/          # Controller tests
│       │   └── repository/          # Repository tests
│       └── resources/
│           └── application-test.yml
├── pom.xml
├── README.md
├── PROJECT_SETUP.md
└── DATABASE_SCHEMA.md
```

## Project Status

### Completed Features

✅ Complete database schema with 7 core tables
✅ JPA entity classes with proper relationships
✅ Repository layer with custom query methods
✅ Service layer with business logic
✅ REST API controllers with validation
✅ Menu generation algorithm with balance scoring
✅ Nutritional balance calculator
✅ Flyway database migrations
✅ Swagger/OpenAPI documentation
✅ Comprehensive unit test suite (14+ test cases)
✅ Frontend interface for ingredient and menu management
✅ Multi-user support
✅ CORS configuration

### Future Enhancements

- User authentication and authorization (Spring Security + JWT)
- Recipe recommendation system based on preferences
- Meal history tracking and analytics
- Grocery list generation from menu plans
- Nutritional goal tracking and progress reports
- Recipe rating and review system
- Social features (share recipes, menu plans)
- Mobile application (React Native/Flutter)
- Email notifications for meal reminders
- Integration with fitness tracking apps

## Troubleshooting

### Common Issues

**Issue**: Application fails to start with database connection error
**Solution**: Ensure PostgreSQL is running and credentials in `application.yml` are correct

**Issue**: Tests fail with "Table not found" error
**Solution**: Run `mvn clean install` to ensure Flyway migrations are executed

**Issue**: Swagger UI shows 404
**Solution**: Ensure you're accessing `http://localhost:8080/swagger-ui.html` (note: includes `.html`)

**Issue**: Menu generation fails with "No feasible recipes"
**Solution**: Ensure you have recipes that use available ingredients and cover all meal categories (breakfast, lunch, dinner)

For more detailed troubleshooting, see [PROJECT_SETUP.md](PROJECT_SETUP.md).

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow standard Java conventions
- Use Lombok annotations to reduce boilerplate
- Write comprehensive unit tests for new features
- Add Swagger documentation for new endpoints
- Update README if adding new major features

## License

This project is part of the GreenMono demonstration repository.

## Contact

For questions or support, please open an issue on GitHub.

## Acknowledgments

- Spring Boot framework
- PostgreSQL database
- Springdoc OpenAPI for API documentation
- MapStruct for object mapping
- AssertJ for fluent assertions in tests