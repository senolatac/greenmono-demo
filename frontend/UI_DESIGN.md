# Weekly Menu Table - UI Design Documentation

## Visual Design Overview

This document describes the visual design and user interface of the Weekly Menu Table component.

## Color Scheme

### Primary Colors
- **Purple**: #667eea (Primary brand color)
- **Violet**: #764ba2 (Secondary brand color)
- **Success Green**: #27ae60 (Button, success states)
- **Dark Green**: #229954 (Button hover)

### Neutral Colors
- **Dark Text**: #2c3e50 (Headers, main text)
- **Gray Text**: #7f8c8d (Secondary text, empty states)
- **Light Gray**: #ecf0f1 (Borders)
- **White**: #ffffff (Card backgrounds)

### Gradients
- **Background**: linear-gradient(135deg, #667eea 0%, #764ba2 100%)
- **Header**: linear-gradient(135deg, #667eea 0%, #764ba2 100%)
- **Meal Cards**: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)

## Layout Structure

```
┌─────────────────────────────────────────────────────────────┐
│                    Gradient Background                       │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  ┌────────────────────────────────────────────────┐  │  │
│  │  │  Haftalık Menü Planı    [Yeni Menü Oluştur]   │  │  │
│  │  │                                                 │  │  │
│  │  └────────────────────────────────────────────────┘  │  │
│  │                                                        │  │
│  │  ┌────────────────────────────────────────────────┐  │  │
│  │  │ ┌──────┬──────┬──────┬──────┬──────┐          │  │  │
│  │  │ │ Pzt  │ Sal  │ Çar  │ Per  │ Cum  │          │  │  │
│  │  │ │ Date │ Date │ Date │ Date │ Date │          │  │  │
│  │  │ ├──────┼──────┼──────┼──────┼──────┤          │  │  │
│  │  │ │Meal1 │Meal1 │Meal1 │Meal1 │Meal1 │          │  │  │
│  │  │ │Meal2 │Meal2 │Meal2 │Meal2 │Meal2 │          │  │  │
│  │  │ │Meal3 │Meal3 │Meal3 │Meal3 │Meal3 │          │  │  │
│  │  │ └──────┴──────┴──────┴──────┴──────┘          │  │  │
│  │  └────────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Component Breakdown

### 1. Container (.weekly-menu-container)
- Max width: 1400px
- Centered with auto margins
- Padding: 2rem on all sides
- Background: Transparent (gradient from body)

### 2. Header Section (.header)
- Display: Flexbox (space-between)
- Responsive: Wraps on mobile
- Contains:
  - H1 title: "Haftalık Menü Planı"
  - "Yeni Menü Oluştur" button

### 3. Title (h1)
- Font size: 2rem (desktop), 1.5rem (tablet)
- Color: #2c3e50
- Weight: Bold

### 4. Generate Button (.generate-button)
- Background: Green gradient (#27ae60)
- Color: White
- Padding: 0.75rem × 1.5rem
- Border radius: 8px
- Box shadow: Subtle elevation
- Hover: Darker green, lift effect
- Disabled: Gray, no pointer

### 5. Table Wrapper (.table-wrapper)
- White background
- Border radius: 12px
- Box shadow: 0 2px 8px rgba(0,0,0,0.1)
- Overflow: Auto (horizontal scroll on mobile)

### 6. Table Header (thead)
- Purple gradient background
- White text
- Padding: 1.5rem × 1rem
- Border radius on corners

### 7. Day Header Cell (.day-header)
- Day name: Uppercase, bold, 1.1rem
- Date: Smaller, 0.9rem, slight opacity
- Centered alignment
- Vertical layout (flex column)

### 8. Table Body (tbody)
- White background
- Borders between cells
- Minimum height per day column

### 9. Meal Items (.meal-item)
- Gray gradient background
- Padding: 1rem
- Border radius: 8px
- Box shadow: Subtle
- Center aligned text
- Hover: Lift animation, darker shadow
- Gap between meals: 1rem

## States

### Loading State
```
┌──────────────────────────────┐
│                              │
│      Yükleniyor...           │
│                              │
└──────────────────────────────┘
```
- Centered text
- Gray color
- Padding: 3rem

### Empty State
```
┌──────────────────────────────┐
│                              │
│  Henüz bir menü planınız yok │
│  Yeni bir menü oluşturmak    │
│  için yukarıdaki butona      │
│  tıklayın.                   │
│                              │
└──────────────────────────────┘
```
- Centered text
- Gray color
- Two paragraphs
- Padding: 3rem

### Error State
```
┌──────────────────────────────┐
│ │ Error message here         │
└──────────────────────────────┘
```
- Red left border (4px)
- Pink background (#fee)
- Red text (#c0392b)
- Padding: 1rem
- Border radius: 4px

## Responsive Behavior

### Desktop (> 1024px)
- Full table width
- All 5 columns visible
- Generous padding
- Large fonts

### Tablet (768px - 1024px)
- Slightly reduced padding
- Smaller fonts
- Table still fully visible

### Mobile (< 768px)
- Horizontal scroll on table
- Table min-width: 600px
- Header stacks vertically
- Button full width
- Reduced padding

## Typography

### Font Family
- Primary: -apple-system, BlinkMacSystemFont
- Fallbacks: 'Segoe UI', 'Roboto', 'Oxygen', etc.

### Font Sizes
- Page title (h1): 2rem → 1.5rem (mobile)
- Day name: 1.1rem → 1rem (mobile)
- Date: 0.9rem → 0.85rem (mobile)
- Meal name: 0.95rem → 0.9rem (mobile)
- Button: 1rem

### Font Weights
- Title: Bold (700)
- Day name: Bold (700)
- Button: Semi-bold (600)
- Meal items: Medium (500)

## Spacing

### Padding
- Container: 2rem → 1rem (mobile)
- Header section: 2rem bottom
- Table cells: 1.5rem × 1rem → 1rem × 0.5rem (mobile)
- Meal items: 1rem → 0.75rem (mobile)

### Gaps
- Header items: 1rem
- Day header items: 0.5rem
- Meal items: 1rem

### Margins
- Title: 0 (flexbox handles spacing)
- Between sections: Handled by padding

## Animations & Transitions

### Button
- Property: background-color, transform
- Duration: 0.3s (background), 0.1s (transform)
- Easing: ease
- Hover: translateY(-1px)
- Active: translateY(0)

### Meal Items
- Property: transform, box-shadow
- Duration: 0.2s
- Easing: ease
- Hover: translateY(-2px), enhanced shadow

## Shadows

### Button
- Default: 0 2px 4px rgba(0,0,0,0.1)
- Hover: 0 4px 8px rgba(0,0,0,0.15)

### Table Wrapper
- 0 2px 8px rgba(0,0,0,0.1)

### Meal Items
- Default: 0 2px 4px rgba(0,0,0,0.05)
- Hover: 0 4px 8px rgba(0,0,0,0.1)

## Accessibility Features

### Semantic HTML
- Proper table structure (table, thead, tbody, tr, th, td)
- Headings hierarchy (h1)
- Button element for actions

### Visual Feedback
- Hover states on interactive elements
- Loading text for async operations
- Error messages for failures
- Disabled state styling

### Color Contrast
- White text on purple (AAA)
- Dark text on white (AAA)
- Red error text on pink (AA)

## Example Visual States

### Successful Menu Display
```
┌────────────────────────────────────────────────────────┐
│ Haftalık Menü Planı          [Yeni Menü Oluştur]      │
├────────────────────────────────────────────────────────┤
│ ┌────────┬────────┬────────┬────────┬────────┐        │
│ │PAZARTE │  SALI  │ÇARŞAMBA│PERŞEMBE│  CUMA  │        │
│ │03.02.26│04.02.26│05.02.26│06.02.26│07.02.26│        │
│ ├────────┼────────┼────────┼────────┼────────┤        │
│ │ Menemen│Simit   │Börek   │Omlet   │Gözleme │        │
│ │        │        │        │        │        │        │
│ │ Mercim.│Domates │Tavuk   │Köfte   │Balık   │        │
│ │ Çorbası│Çorbası │Güveç   │        │Izgara  │        │
│ │        │        │        │        │        │        │
│ │ Kuru   │Makarna │Pilav   │Mantı   │Pizza   │        │
│ │ Fasulye│        │        │        │        │        │
│ └────────┴────────┴────────┴────────┴────────┘        │
└────────────────────────────────────────────────────────┘
```

### Loading State
```
┌────────────────────────────────────────────────────────┐
│ Haftalık Menü Planı       [Oluşturuluyor...]          │
├────────────────────────────────────────────────────────┤
│                                                        │
│                   Yükleniyor...                        │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Empty State
```
┌────────────────────────────────────────────────────────┐
│ Haftalık Menü Planı          [Yeni Menü Oluştur]      │
├────────────────────────────────────────────────────────┤
│                                                        │
│            Henüz bir menü planınız yok.                │
│     Yeni bir menü oluşturmak için yukarıdaki          │
│              butona tıklayın.                          │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Error State
```
┌────────────────────────────────────────────────────────┐
│ Haftalık Menü Planı          [Yeni Menü Oluştur]      │
├────────────────────────────────────────────────────────┤
│ │ Yeterli malzeme bulunamadı. Lütfen dolabınıza      │
│ │ malzeme ekleyin.                                    │
└────────────────────────────────────────────────────────┘
```

## Design Principles

1. **Clarity**: Clean layout with clear visual hierarchy
2. **Consistency**: Uniform spacing, colors, and typography
3. **Feedback**: Visual feedback for all user actions
4. **Responsiveness**: Works on all screen sizes
5. **Accessibility**: High contrast, semantic HTML
6. **Modern**: Gradients, shadows, animations
7. **Professional**: Polished, production-ready appearance

## Browser Compatibility

- Chrome: ✅ Latest
- Firefox: ✅ Latest
- Safari: ✅ Latest
- Edge: ✅ Latest
- Mobile Safari: ✅ iOS 12+
- Chrome Mobile: ✅ Android 8+

## Performance Considerations

- CSS animations use transform (GPU accelerated)
- No layout thrashing
- Minimal repaints
- Efficient hover effects
- Lazy loading via Vite

## Future Design Enhancements

1. Dark mode support
2. Custom color themes
3. Print-friendly stylesheet
4. Calendar view option
5. Drag-and-drop reordering
6. Meal preview tooltips
7. Nutritional info badges
8. Category color coding
