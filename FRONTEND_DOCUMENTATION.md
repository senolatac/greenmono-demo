# Frontend Documentation - Malzeme Yönetimi Sayfası

## Overview

This document describes the ingredient management frontend page implementation for the Weekly Meal Planner application. The frontend is built using vanilla HTML, CSS, and JavaScript, and is served directly from Spring Boot's static resources.

## Architecture

### Technology Stack
- **HTML5**: Semantic markup for page structure
- **CSS3**: Modern styling with responsive design
- **Vanilla JavaScript**: Pure JavaScript for API integration (no frameworks)
- **Spring Boot Static Resources**: Served from `src/main/resources/static/`

### File Structure
```
src/main/resources/static/
├── index.html           # Main HTML page
├── css/
│   └── styles.css       # All styles including responsive design
└── js/
    └── app.js          # Application logic and API integration
```

## Features Implemented

### 1. Ingredient Form (Malzeme Ekleme Formu)
- **Required Fields**:
  - Malzeme Adı (Name): Text input, max 100 characters
  - Kategori (Category): Dropdown with 15 categories
  - Miktar (Quantity): Numeric input, positive values only
  - Birim (Unit): Dropdown with 10 units

- **Optional Fields**:
  - Son Kullanma Tarihi (Expiry Date): Date picker
  - Mevcut (Available): Checkbox (default: checked)
  - Notlar (Notes): Textarea, max 500 characters

### 2. Category Options
- Sebzeler (VEGETABLES)
- Meyveler (FRUITS)
- Et (MEAT)
- Kümes Hayvanları (POULTRY)
- Balık (FISH)
- Deniz Ürünleri (SEAFOOD)
- Süt Ürünleri (DAIRY)
- Tahıllar (GRAINS)
- Baklagiller (LEGUMES)
- Kuruyemiş ve Tohumlar (NUTS_SEEDS)
- Otlar ve Baharatlar (HERBS_SPICES)
- Yağlar (OILS_FATS)
- Soslar ve Çeşniler (CONDIMENTS)
- İçecekler (BEVERAGES)
- Diğer (OTHER)

### 3. Unit Options
- Gram (GRAM)
- Kilogram (KILOGRAM)
- Mililitre (MILLILITER)
- Litre (LITER)
- Adet (PIECE)
- Yemek Kaşığı (TABLESPOON)
- Çay Kaşığı (TEASPOON)
- Fincan (CUP)
- Ons (OUNCE)
- Pound (POUND)

### 4. Ingredient List (Malzeme Listesi)
- **Table View**: Displays all ingredients in a responsive table
- **Columns**:
  - Malzeme Adı (Name)
  - Kategori (Category)
  - Miktar (Quantity)
  - Birim (Unit)
  - Son Kullanma (Expiry Date)
  - Durum (Status)
  - İşlemler (Actions)

- **Status Badges**:
  - Mevcut (Available): Green badge
  - Mevcut Değil (Unavailable): Red badge
  - Yakında Bitecek (Expiring Soon): Yellow badge (3 days or less)
  - Tarihi Geçmiş (Expired): Red badge

### 5. Filtering & Pagination
- **Category Filter**: Filter ingredients by category
- **Pagination**: 20 items per page
- **Navigation**: Previous/Next buttons with page info

### 6. Delete Functionality
- **Modal Confirmation**: Confirmation dialog before deletion
- **Success/Error Messages**: User feedback for all operations

## API Integration

### Endpoints Used

#### 1. Get Ingredients (List)
```
GET /api/ingredients
```
**Query Parameters**:
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)
- `sortBy`: Sort field (default: name)
- `sortDirection`: ASC or DESC (default: ASC)
- `category`: Filter by category (optional)

**Response**: PageResponse<IngredientResponse>

#### 2. Create Ingredient
```
POST /api/ingredients
```
**Request Body**: IngredientRequest
```json
{
  "name": "Domates",
  "category": "VEGETABLES",
  "quantity": 500,
  "unit": "GRAM",
  "expiryDate": "2024-12-31",
  "notes": "Taze domates",
  "available": true,
  "userId": 1
}
```
**Response**: 201 Created with IngredientResponse

#### 3. Delete Ingredient
```
DELETE /api/ingredients/{id}
```
**Response**: 204 No Content

## User Interface Design

### Color Scheme
- **Primary Color**: `#2ecc71` (Green)
- **Primary Hover**: `#27ae60` (Dark Green)
- **Secondary Color**: `#95a5a6` (Gray)
- **Danger Color**: `#e74c3c` (Red)
- **Success Color**: `#27ae60` (Green)
- **Warning Color**: `#f39c12` (Orange)

### Responsive Design
The application is fully responsive and works on all screen sizes:

#### Desktop (>768px)
- Two-column form layout
- Full table view
- Side-by-side buttons

#### Tablet (768px)
- Single-column form layout
- Horizontal table with scroll
- Stacked buttons

#### Mobile (<480px)
- Vertical card layout for table rows
- Full-width buttons
- Optimized touch targets

## Validation

### Client-Side Validation
- Required field validation
- Maximum length validation (name: 100, notes: 500)
- Positive number validation for quantity
- Date validation for expiry date

### Server-Side Validation
- All validations enforced by backend
- Error messages displayed to user
- Validation errors mapped to Turkish

## Error Handling

### Display Mechanisms
- **Loading Spinner**: Shows during API calls
- **Error Messages**: Red banner with error details
- **Success Messages**: Green banner with success confirmation
- **Auto-Hide**: Messages auto-hide after 3-5 seconds

### Error Types
- Network errors
- Validation errors
- Duplicate ingredient errors
- Not found errors
- Server errors

## Accessibility Features

- Semantic HTML5 elements
- ARIA labels where needed
- Keyboard navigation support
- Focus indicators
- Color contrast compliance
- Screen reader friendly

## Browser Compatibility

Tested and working on:
- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Security Features

1. **XSS Prevention**: HTML escaping for all user input
2. **CSRF Protection**: Spring Boot default CSRF protection
3. **Input Validation**: Both client and server-side
4. **Content Security**: Served over HTTPS in production

## Configuration

### Backend Configuration
CORS is configured in `WebConfig.java`:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
```

### Static Resources
Spring Boot automatically serves files from `src/main/resources/static/` at the root URL.

## Usage Instructions

### Running the Application

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Access the frontend**:
   - Open browser to: `http://localhost:8080`
   - Or: `http://localhost:8080/index.html`

### Adding a New Ingredient

1. Fill in all required fields:
   - Enter ingredient name
   - Select category
   - Enter quantity
   - Select unit

2. Optionally fill:
   - Expiry date
   - Notes
   - Available status

3. Click "Malzeme Ekle" button

4. View success message and see ingredient in list

### Filtering Ingredients

1. Select a category from the filter dropdown
2. List automatically updates
3. Select "Tümü" to clear filter

### Deleting an Ingredient

1. Click "Sil" button in the ingredient row
2. Confirm deletion in modal dialog
3. View success message
4. List automatically updates

## Performance Optimizations

- Pagination for large datasets
- Debounced API calls
- Lazy loading of data
- Minimal DOM manipulation
- CSS animations via GPU acceleration

## Future Enhancements

Potential improvements:
- Edit functionality for existing ingredients
- Bulk operations (delete multiple)
- Export to CSV/Excel
- Search functionality
- Advanced filtering (by date, status, etc.)
- Ingredient images
- Barcode scanning
- Integration with recipe suggestions
- Multi-user support with authentication
- Real-time updates via WebSocket
- Offline support with Service Workers

## Troubleshooting

### Issue: Frontend not loading
**Solution**: Ensure Spring Boot is running and access `http://localhost:8080`

### Issue: API calls failing
**Solution**: Check backend logs, ensure PostgreSQL is running

### Issue: CORS errors
**Solution**: Verify `WebConfig.java` is properly configured

### Issue: Styles not applying
**Solution**: Clear browser cache, check browser console for errors

### Issue: Data not displaying
**Solution**: Check browser console for JavaScript errors, verify API responses

## Testing

### Manual Testing Checklist
- [ ] Form validation works
- [ ] Ingredient creation successful
- [ ] List displays correctly
- [ ] Pagination works
- [ ] Filtering works
- [ ] Delete confirmation works
- [ ] Error messages display
- [ ] Success messages display
- [ ] Responsive design works
- [ ] All browsers compatible

### Backend Tests
Run existing controller tests:
```bash
mvn test -Dtest=IngredientControllerTest
```

## Maintenance

### Adding New Categories
1. Update `Ingredient.IngredientCategory` enum in backend
2. Update dropdown in `index.html` (2 places)
3. Update `categoryTranslations` in `app.js`

### Adding New Units
1. Update `Ingredient.Unit` enum in backend
2. Update dropdown in `index.html` (2 places)
3. Update `unitTranslations` in `app.js`

### Modifying Styles
All styles are in `css/styles.css`:
- CSS variables at the top for easy theming
- Organized by component
- Responsive rules at the bottom

## Support

For issues or questions:
1. Check this documentation
2. Review backend API documentation at `/swagger-ui.html`
3. Check application logs
4. Review browser console for errors

## License

Part of Weekly Meal Planner project. All rights reserved.
