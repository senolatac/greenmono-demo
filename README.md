# Weekly Meal Planner

An intelligent meal planning system that creates balanced 5-day menus based on available ingredients.

## Features

- Ingredient management with expiry tracking
- Comprehensive meal recipe database
- Nutritional information tracking (macros and micros)
- Intelligent 5-day menu planning
- Nutritional balance scoring
- Multi-user support

## Quick Start

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## Documentation

- [Project Setup Guide](PROJECT_SETUP.md) - Detailed setup and configuration instructions
- [Database Schema](DATABASE_SCHEMA.md) - Complete database schema documentation

## Technology Stack

- Java 17
- Spring Boot 3.2.1
- PostgreSQL
- Spring Data JPA
- Flyway
- Lombok
- Swagger/OpenAPI

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

Once running, access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Database Schema

The application includes 5 core tables:
- **ingredients** - User ingredient inventory
- **meals** - Meal recipes with instructions
- **nutritional_info** - Nutritional data for ingredients and meals
- **menu_plans** - Weekly menu plans
- **daily_meal_plans** - Daily meal assignments

## Project Status

Current implementation includes:
- Complete database schema design
- JPA entity classes with relationships
- Repository layer with custom queries
- Flyway database migrations
- API documentation setup

## Next Steps

- Service layer implementation
- REST API controllers
- Menu generation algorithm
- Nutritional balance calculator
- Authentication and authorization
- Comprehensive testing suite

## License

[Add license information]