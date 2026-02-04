# Weekly Meal Planner - Frontend

React frontend application for displaying weekly meal plans in a table format.

## Features

- Display 5-day weekly menu plan (Monday to Friday)
- Table view with columns for each weekday
- Shows date and meal names for each day
- "Yeni Menü Oluştur" (Create New Menu) button
- Responsive design for mobile and desktop
- Error handling and loading states

## Prerequisites

- Node.js (version 16 or higher)
- npm or yarn
- Backend API running on http://localhost:8080

## Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

## Running the Application

### Development Mode

Start the development server:
```bash
npm run dev
```

The application will be available at http://localhost:3000

### Build for Production

Create a production build:
```bash
npm run build
```

Preview the production build:
```bash
npm run preview
```

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── WeeklyMenuTable.jsx    # Main menu table component
│   │   └── WeeklyMenuTable.css    # Styling for the table
│   ├── services/
│   │   └── menuService.js         # API service for backend calls
│   ├── App.jsx                     # Root component
│   ├── App.css                     # Global styles
│   └── main.jsx                    # Application entry point
├── index.html                      # HTML template
├── vite.config.js                  # Vite configuration
├── package.json                    # Dependencies and scripts
└── README.md                       # This file
```

## API Integration

The frontend connects to the backend API at `http://localhost:8080/api/menu`.

### Endpoints Used

1. **GET /api/menu/current**
   - Fetches the current active menu plan for a user
   - Query param: `userId` (default: 1)

2. **POST /api/menu/generate**
   - Generates a new 5-day menu plan
   - Request body: `{ "userId": 1 }`

## Component Overview

### WeeklyMenuTable

Main component that displays the weekly menu in a table format.

Features:
- Automatic fetching of current menu on mount
- "Yeni Menü Oluştur" button to generate new menus
- Groups meals by day and displays in table columns
- Error handling for network issues
- Loading states during API calls
- Empty state when no menu exists

### menuService

API service that handles HTTP requests to the backend.

Methods:
- `getCurrentMenu(userId)` - Get current menu plan
- `generateMenu(userId)` - Generate new menu plan

## Styling

The application uses custom CSS with:
- Modern gradient design
- Responsive layout for mobile and tablet
- Smooth animations and transitions
- Professional color scheme
- Clean typography

## Configuration

### Backend API URL

The API base URL is configured in `vite.config.js` as a proxy:
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
  }
}
```

### User ID

The default user ID is set to `1` in `WeeklyMenuTable.jsx`. You can modify this or make it configurable:
```javascript
const userId = 1; // Change this or make it dynamic
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### API Connection Issues

1. Ensure the backend is running on http://localhost:8080
2. Check CORS configuration in the backend (WebConfig.java)
3. Verify the proxy configuration in vite.config.js

### Menu Not Loading

1. Check browser console for errors
2. Verify user has ingredients in the database
3. Ensure recipes exist in the database
4. Check backend logs for errors

### Build Issues

If you encounter build issues:
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

## Development

### Code Style

- Use functional components with hooks
- Follow React best practices
- Use meaningful variable names
- Add comments for complex logic

### Adding New Features

1. Create new components in `src/components/`
2. Add API methods to `src/services/menuService.js`
3. Update styling in corresponding `.css` files
4. Test thoroughly before committing

## License

[Add license information]
