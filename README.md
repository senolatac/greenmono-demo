# Weekly Meal Planner

An intelligent meal planning system that creates balanced 5-day menus based on available ingredients.

## Features

- Ingredient management with expiry tracking
- Comprehensive meal recipe database
- Nutritional information tracking (macros and micros)
- Intelligent 5-day menu planning
- Nutritional balance scoring
- Multi-user support
- Interactive weekly menu table UI

## Quick Start

### Backend

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend API will be available at http://localhost:8080

### Frontend

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Run the development server
npm run dev
```

The frontend will be available at http://localhost:3000

## Documentation

- [Project Setup Guide](PROJECT_SETUP.md) - Detailed setup and configuration instructions
- [Database Schema](DATABASE_SCHEMA.md) - Complete database schema documentation

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.2.1
- PostgreSQL
- Spring Data JPA
- Flyway
- Lombok
- Swagger/OpenAPI

### Frontend
- React 18
- Vite
- Axios
- CSS3 with modern design

## Project Structure

The project follows a standard Spring Boot layered architecture:

```
weekly-meal-planner/
├── src/                             # Backend source code
│   ├── main/
│   │   ├── java/com/greenmono/mealplanner/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   └── WebConfig.java   # CORS configuration
│   │   │   ├── controller/          # REST API controllers
│   │   │   │   ├── MenuController.java
│   │   │   │   └── IngredientController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── SimplifiedMenuItemResponse.java
│   │   │   │   └── MenuPlanResponse.java
│   │   │   ├── entity/              # JPA entity classes
│   │   │   │   ├── Ingredient.java
│   │   │   │   ├── Meal.java
│   │   │   │   ├── NutritionalInfo.java
│   │   │   │   ├── MenuPlan.java
│   │   │   │   └── DailyMealPlan.java
│   │   │   ├── repository/          # Spring Data JPA repositories
│   │   │   ├── service/             # Business logic services
│   │   │   └── MealPlannerApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway database migrations
│   │       ├── application.yml      # Main configuration
│   │       └── application-dev.yml  # Development profile configuration
│   └── test/                        # Test source code
├── frontend/                        # Frontend application
│   ├── src/
│   │   ├── components/              # React components
│   │   │   ├── WeeklyMenuTable.jsx  # Weekly menu table component
│   │   │   └── WeeklyMenuTable.css  # Component styles
│   │   ├── services/                # API services
│   │   │   └── menuService.js       # Menu API client
│   │   ├── App.jsx                  # Root component
│   │   ├── App.css                  # Global styles
│   │   └── main.jsx                 # Application entry point
│   ├── index.html                   # HTML template
│   ├── vite.config.js               # Vite configuration
│   ├── package.json                 # Node.js dependencies
│   └── README.md                    # Frontend documentation
├── pom.xml                          # Maven project configuration
├── PROJECT_SETUP.md                 # Detailed setup instructions
├── DATABASE_SCHEMA.md               # Database schema documentation
└── README.md                        # This file
```

### Key Components

#### Backend
- **Entity Layer**: JPA entities representing database tables with relationships and constraints
- **Repository Layer**: Spring Data JPA repositories with custom query methods for data access
- **Service Layer**: Business logic for menu planning and ingredient management
- **Controller Layer**: REST API endpoints for frontend communication
- **Database Migrations**: Flyway versioned scripts for schema management and evolution
- **Configuration**: Profile-based configuration (dev, test) for different environments

#### Frontend
- **WeeklyMenuTable Component**: Main UI component displaying 5-day menu in table format
- **API Service**: Axios-based service for backend communication
- **Responsive Design**: Mobile-first CSS with modern styling
- **State Management**: React hooks for local state and API data

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
- Service layer with menu generation algorithm
- REST API controllers for menu management
- Flyway database migrations
- API documentation setup
- React frontend with weekly menu table display
- CORS configuration for frontend-backend communication

## Features Implemented

### Backend
- 5-day balanced menu generation algorithm
- Menu plan storage and retrieval
- Recipe and ingredient management
- Nutritional tracking
- RESTful API endpoints

### Frontend
- Weekly menu table display (Monday-Friday)
- "Yeni Menü Oluştur" button for menu generation
- Date and meal name display for each day
- Error handling and loading states
- Responsive design for all devices
- Clean, modern UI with gradient styling

## Next Steps

- User authentication and authorization
- Ingredient inventory management UI
- Recipe detail views
- Nutritional information display
- Shopping list generation
- Comprehensive testing suite

## License

[Add license information]