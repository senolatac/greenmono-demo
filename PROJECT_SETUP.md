# Weekly Meal Planner - Project Setup Guide

## Overview
This is a Spring Boot backend application for an intelligent weekly meal planner that creates balanced 5-day menus based on available ingredients.

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.1
- **Database**: PostgreSQL (Production), H2 (Testing)
- **ORM**: Spring Data JPA / Hibernate
- **Migration**: Flyway
- **Build Tool**: Maven
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Code Generation**: Lombok, MapStruct

## Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 14+ (for production)
- Docker (optional, for running PostgreSQL)

## Project Structure

```
weekly-meal-planner/
├── src/
│   ├── main/
│   │   ├── java/com/greenmono/mealplanner/
│   │   │   ├── entity/
│   │   │   │   ├── Ingredient.java
│   │   │   │   ├── Meal.java
│   │   │   │   ├── MenuPlan.java
│   │   │   │   ├── DailyMealPlan.java
│   │   │   │   └── NutritionalInfo.java
│   │   │   ├── repository/
│   │   │   │   ├── IngredientRepository.java
│   │   │   │   ├── MealRepository.java
│   │   │   │   ├── MenuPlanRepository.java
│   │   │   │   ├── DailyMealPlanRepository.java
│   │   │   │   └── NutritionalInfoRepository.java
│   │   │   └── MealPlannerApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── db/migration/
│   │           ├── V1__Create_ingredients_table.sql
│   │           ├── V2__Create_meals_table.sql
│   │           ├── V3__Create_nutritional_info_table.sql
│   │           └── V4__Create_menu_plans_tables.sql
│   └── test/
│       ├── java/com/greenmono/mealplanner/
│       └── resources/
│           └── application-test.yml
├── pom.xml
├── README.md
├── DATABASE_SCHEMA.md
└── PROJECT_SETUP.md
```

## Database Setup

### Option 1: Local PostgreSQL

1. Install PostgreSQL 14 or higher
2. Create a database:
```sql
CREATE DATABASE meal_planner_db;
CREATE USER mealplanner WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE meal_planner_db TO mealplanner;
```

3. Update `application.yml` with your credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/meal_planner_db
    username: mealplanner
    password: your_password
```

### Option 2: Docker PostgreSQL

Run PostgreSQL in a Docker container:

```bash
docker run -d \
  --name meal-planner-db \
  -e POSTGRES_DB=meal_planner_db \
  -e POSTGRES_USER=mealplanner \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 \
  postgres:14-alpine
```

## Build & Run

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
# Default profile (connects to PostgreSQL)
mvn spring-boot:run

# Development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# With environment variables for credentials
DB_USERNAME=mealplanner DB_PASSWORD=your_password mvn spring-boot:run
```

### Run tests

```bash
mvn test
```

## Database Migrations

Flyway will automatically run migrations on application startup. The migrations are located in `src/main/resources/db/migration/`.

To check migration status:
```bash
mvn flyway:info
```

To manually run migrations:
```bash
mvn flyway:migrate
```

## API Documentation

Once the application is running, access the Swagger UI at:
- http://localhost:8080/swagger-ui.html

API documentation in JSON format:
- http://localhost:8080/api-docs

## Configuration

### Application Profiles

- **default**: Production-ready configuration
- **dev**: Development configuration with detailed logging
- **test**: Test configuration using H2 in-memory database

### Environment Variables

Set these environment variables to override default configuration:

```bash
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### Key Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| server.port | 8080 | Application port |
| spring.datasource.url | jdbc:postgresql://localhost:5432/meal_planner_db | Database URL |
| spring.jpa.hibernate.ddl-auto | validate | Hibernate DDL mode |
| spring.flyway.enabled | true | Enable Flyway migrations |

## Database Schema

The application uses 5 main tables:

1. **ingredients** - Stores user ingredients
2. **meals** - Stores meal recipes
3. **nutritional_info** - Stores nutritional data
4. **menu_plans** - Stores weekly menu plans
5. **daily_meal_plans** - Stores daily meal assignments

For detailed schema documentation, see [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)

## Development Guidelines

### Entity Design

All entities include:
- Lombok annotations for boilerplate reduction
- Bean Validation annotations for input validation
- JPA auditing with @CreationTimestamp and @UpdateTimestamp
- Proper indexing for performance

### Repository Pattern

Repositories extend JpaRepository and include:
- Common CRUD operations
- Custom query methods
- JPQL queries for complex operations

### Code Style

- Use meaningful variable and method names
- Follow Java naming conventions
- Add Javadoc for public APIs
- Use Builder pattern for entity creation

## Testing

### Unit Tests

Test individual components in isolation:
```bash
mvn test -Dtest=*Test
```

### Integration Tests

Test with H2 in-memory database:
```bash
mvn test -Dtest=*IT
```

## Troubleshooting

### Database Connection Issues

1. Verify PostgreSQL is running:
```bash
psql -U mealplanner -d meal_planner_db
```

2. Check credentials in application.yml

3. Ensure database exists:
```sql
\l  -- list databases
```

### Migration Failures

1. Check Flyway history:
```bash
mvn flyway:info
```

2. Clean and retry (WARNING: drops all data):
```bash
mvn flyway:clean
mvn flyway:migrate
```

### Port Already in Use

Change the server port in application.yml:
```yaml
server:
  port: 8081
```

## Next Steps

1. **Service Layer**: Implement business logic services
2. **REST Controllers**: Create API endpoints
3. **DTO Layer**: Add DTOs and MapStruct mappers
4. **Security**: Implement Spring Security with JWT
5. **Testing**: Add comprehensive unit and integration tests
6. **Menu Generation Algorithm**: Implement intelligent meal planning logic
7. **Nutritional Analysis**: Add balance scoring algorithms
8. **API Rate Limiting**: Implement rate limiting for public APIs

## Contributing

1. Create a feature branch
2. Implement changes with tests
3. Ensure all tests pass
4. Submit a pull request

## License

[Add license information here]
