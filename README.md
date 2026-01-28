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