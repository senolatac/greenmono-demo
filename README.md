# Weekly Meal Planner

An intelligent meal planning system that creates balanced 5-day menus based on available ingredients. The system uses a sophisticated algorithm to ensure nutritional balance, variety, and optimal use of available ingredients.

## Features

### Backend Features
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
- CORS configuration for frontend integration

### Frontend Features
- Modern React 18 application with Vite build tool
- **Dark Mode** - Toggle between light and dark themes with persistent preference
- Interactive weekly menu table (5-day view: Monday to Friday)
- Real-time menu generation with loading states
- Ingredient management interface with:
  - Add new ingredients with category selection
  - Track expiry dates
  - View ingredient inventory
  - Delete ingredients
- Responsive design for mobile, tablet, and desktop
- Smooth animations and transitions
- Professional gradient design with theme support
- Error handling with user-friendly Turkish messages

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

Features available in the frontend:
- View weekly menu plan (5-day table view)
- Generate new menu with one click
- Add and manage ingredients
- Toggle dark mode (theme persists across sessions)

## Documentation

### Backend Documentation
- [Project Setup Guide](PROJECT_SETUP.md) - Detailed setup and configuration instructions
- [Database Schema](DATABASE_SCHEMA.md) - Complete database schema documentation
- [API Examples](API_EXAMPLES.md) - Comprehensive API usage examples with cURL commands
- [API Documentation](#api-documentation) - API endpoints and examples (see below)
- [Implementation Summary](IMPLEMENTATION_SUMMARY.md) - Complete project implementation details

### Frontend Documentation
- [Frontend README](frontend/README.md) - Frontend-specific setup and usage guide
- [Dark Mode Feature](frontend/DARK_MODE_FEATURE.md) - Dark mode implementation details
- [Dark Mode Testing](frontend/DARK_MODE_TESTING.md) - Testing guide for dark mode
- [Dark Mode Implementation Summary](DARK_MODE_IMPLEMENTATION_SUMMARY.md) - Dark mode feature overview

## Technology Stack

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.1
- **Database**: PostgreSQL (production), H2 (testing)
- **ORM**: Spring Data JPA, Hibernate
- **Migration**: Flyway
- **Code Generation**: Lombok, MapStruct
- **API Documentation**: Springdoc OpenAPI 3.x (Swagger)
- **Testing**: JUnit 5, Mockito, AssertJ
- **Build Tool**: Maven 3.x

### Frontend
- **Framework**: React 18.2.0
- **Build Tool**: Vite 5.0.8
- **HTTP Client**: Axios 1.6.5
- **Styling**: CSS3 with custom properties (CSS variables)
- **Features**: Dark mode, responsive design, smooth animations
- **State Management**: React Context API (for theme)
- **Code Quality**: ESLint with React plugin

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
│   │   │   ├── WeeklyMenuTable.css  # Component styles
│   │   │   ├── ThemeToggle.jsx      # Dark mode toggle button
│   │   │   ├── ThemeToggle.css      # Toggle button styles
│   │   │   ├── IngredientManager.jsx # Ingredient management component
│   │   │   ├── IngredientManager.css # Ingredient manager styles
│   │   │   ├── IngredientForm.jsx   # Add ingredient form
│   │   │   └── IngredientList.jsx   # Ingredient list display
│   │   ├── context/                 # React Context
│   │   │   └── ThemeContext.jsx     # Theme state management
│   │   ├── services/                # API services
│   │   │   ├── menuService.js       # Menu API client
│   │   │   └── ingredientService.js # Ingredient API client
│   │   ├── App.jsx                  # Root component
│   │   ├── App.css                  # Global styles with dark mode
│   │   └── main.jsx                 # Application entry point
│   ├── index.html                   # HTML template
│   ├── vite.config.js               # Vite configuration
│   ├── package.json                 # Node.js dependencies
│   ├── README.md                    # Frontend documentation
│   ├── DARK_MODE_FEATURE.md         # Dark mode documentation
│   └── DARK_MODE_TESTING.md         # Dark mode testing guide
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
- **IngredientManager Component**: Complete ingredient management with add/view/delete functionality
- **ThemeToggle Component**: Dark mode toggle with persistent theme preference
- **ThemeContext**: React Context API for global theme state management
- **API Services**: Axios-based services for menu and ingredient operations
- **Responsive Design**: Mobile-first CSS with modern styling and dark mode support
- **State Management**: React hooks and Context API for local and global state

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

#### Backend
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
✅ Multi-user support
✅ CORS configuration for frontend integration

#### Frontend
✅ React 18 application with Vite build tool
✅ **Dark mode** with persistent theme preference (localStorage)
✅ Weekly menu table component (5-day view)
✅ Ingredient management interface (add, view, delete)
✅ Real-time menu generation
✅ Responsive design (mobile, tablet, desktop)
✅ Loading states and error handling
✅ Smooth animations and transitions
✅ Turkish language support
✅ Professional UI/UX with gradient design
✅ Context API for theme state management
✅ Comprehensive documentation

### Future Enhancements

#### Backend Enhancements
- User authentication and authorization (Spring Security + JWT)
- Recipe recommendation system based on preferences
- Meal history tracking and analytics
- Nutritional goal tracking and progress reports
- Recipe rating and review system
- Email notifications for meal reminders
- Integration with fitness tracking apps
- API rate limiting and caching

#### Frontend Enhancements
- User authentication UI (login, register, profile)
- Recipe detail modal with nutritional information
- Grocery list generation from menu plans
- Print menu functionality
- Export to PDF/Calendar
- Drag-and-drop meal reordering
- Custom date range selection for menu plans
- Meal notes and comments
- Shopping list feature
- System preference detection for auto dark mode
- Multiple theme variants (blue, green, purple)
- Progressive Web App (PWA) support
- Offline support with service workers

#### Mobile Development
- Mobile application (React Native/Flutter)
- Push notifications for meal reminders
- Barcode scanning for ingredients
- Photo upload for meals

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

**Issue**: Dark mode not working or not persisting
**Solution**: Check browser localStorage is enabled. Theme preference is stored in localStorage under the key 'theme'

**Issue**: Frontend not connecting to backend
**Solution**: Ensure backend is running on port 8080 and check CORS configuration in WebConfig.java

For more detailed troubleshooting, see [PROJECT_SETUP.md](PROJECT_SETUP.md) and [frontend/README.md](frontend/README.md).

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

#### Backend
- Follow standard Java conventions
- Use Lombok annotations to reduce boilerplate
- Write comprehensive unit tests for new features
- Add Swagger documentation for new endpoints
- Document all public APIs with Javadoc

#### Frontend
- Use functional components with React hooks
- Follow React best practices
- Use meaningful variable and component names
- Add comments for complex logic
- Test thoroughly before committing
- Update documentation for new features

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