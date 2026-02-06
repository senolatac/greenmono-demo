# Dark Mode Feature Documentation

## Overview

The Weekly Meal Planner application now includes a comprehensive dark mode feature that allows users to toggle between light and dark themes. The theme preference is persisted in localStorage and automatically applied when the user returns to the application.

## Features

- **Theme Toggle Button**: Fixed position toggle button in the top-right corner with smooth animations
- **Persistent Theme**: User preference is saved to localStorage and restored on page load
- **Smooth Transitions**: All color changes animate smoothly (0.3s ease)
- **Responsive Design**: Theme toggle adapts to mobile screens
- **Accessible**: Includes ARIA labels and title attributes for screen readers
- **CSS Variables**: Theme colors are managed through CSS custom properties for easy maintenance

## Implementation Details

### Files Added

1. **`src/context/ThemeContext.jsx`** - React Context for theme state management
   - Provides `useTheme` hook for consuming components
   - Manages dark mode state with localStorage persistence
   - Applies/removes `dark-mode` class on document root

2. **`src/components/ThemeToggle.jsx`** - Toggle button component
   - Displays sun icon in dark mode, moon icon in light mode
   - Fixed position in top-right corner
   - Smooth icon rotation on hover

3. **`src/components/ThemeToggle.css`** - Theme toggle styles
   - Glass-morphism effect with backdrop blur
   - Responsive sizing for mobile devices
   - Hover and active state animations

### Files Modified

1. **`src/App.css`**
   - Added CSS custom properties (variables) for theming
   - Light mode variables (default)
   - Dark mode variables (applied when `.dark-mode` class is present)
   - Smooth background gradient transitions

2. **`src/components/WeeklyMenuTable.css`**
   - Updated to use CSS variables instead of hardcoded colors
   - Added dark mode specific styles for meal items
   - Error message dark mode styling
   - Enhanced box shadows for dark mode

3. **`src/App.jsx`**
   - Wrapped application with `ThemeProvider`
   - Added `ThemeToggle` component to the layout

## CSS Variables

### Light Mode (Default)
```css
--bg-gradient-start: #667eea
--bg-gradient-end: #764ba2
--text-primary: #2c3e50
--text-secondary: #7f8c8d
--bg-white: #ffffff
--border-color: #ecf0f1
--shadow-sm: rgba(0, 0, 0, 0.1)
--shadow-md: rgba(0, 0, 0, 0.15)
--shadow-lg: rgba(0, 0, 0, 0.2)
```

### Dark Mode
```css
--bg-gradient-start: #1a1a2e
--bg-gradient-end: #16213e
--text-primary: #e4e4e7
--text-secondary: #a1a1aa
--bg-white: #27272a
--border-color: #3f3f46
--shadow-sm: rgba(0, 0, 0, 0.3)
--shadow-md: rgba(0, 0, 0, 0.4)
--shadow-lg: rgba(0, 0, 0, 0.5)
```

## Usage

### Using the Theme Toggle

Users can toggle between light and dark modes by clicking the floating button in the top-right corner:
- **Light Mode**: Shows a moon icon
- **Dark Mode**: Shows a sun icon

The button includes:
- Hover effects (scale and rotation)
- Active press feedback
- Smooth color transitions
- Backdrop blur glass effect

### Accessing Theme State in Components

Components can access the theme state using the `useTheme` hook:

```jsx
import { useTheme } from '../context/ThemeContext';

function MyComponent() {
  const { isDarkMode, toggleTheme } = useTheme();

  return (
    <div>
      <p>Current mode: {isDarkMode ? 'Dark' : 'Light'}</p>
      <button onClick={toggleTheme}>Toggle Theme</button>
    </div>
  );
}
```

## Browser Compatibility

The dark mode feature uses modern CSS features:
- CSS Custom Properties (CSS Variables)
- `backdrop-filter` for blur effects
- CSS Transitions

Supported browsers:
- Chrome 88+
- Firefox 94+
- Safari 15.4+
- Edge 88+

For older browsers, the application will fall back to the light theme without breaking.

## Testing

### Manual Testing Checklist

- [x] Theme toggle button is visible and clickable
- [x] Clicking toggle switches between light and dark modes
- [x] Theme preference persists after page reload
- [x] All UI elements update correctly in dark mode
- [x] Transitions are smooth and not jarring
- [x] Button animations work on hover and click
- [x] Responsive design works on mobile devices
- [x] Error messages are visible in both modes
- [x] Table headers and cells have proper contrast
- [x] Meal item cards are readable in both modes

### Testing in Development

1. Start the development server:
```bash
cd frontend
npm run dev
```

2. Open http://localhost:3000 in your browser

3. Click the theme toggle button to switch modes

4. Refresh the page to verify persistence

5. Test on different screen sizes using browser dev tools

## Performance Considerations

- **Minimal Re-renders**: Theme context only causes re-renders when theme changes
- **CSS Transitions**: All theme changes use CSS transitions (GPU-accelerated)
- **LocalStorage**: Theme preference is saved efficiently with minimal overhead
- **No Flash**: Initial theme is applied before React hydration using localStorage check

## Future Enhancements

Potential improvements for the dark mode feature:

1. **System Preference Detection**: Automatically detect user's system dark mode preference
2. **Auto Dark Mode**: Switch to dark mode based on time of day
3. **Custom Themes**: Allow users to create custom color schemes
4. **High Contrast Mode**: Add accessibility option for high contrast
5. **Theme Preview**: Show preview before applying theme
6. **Keyboard Shortcut**: Add keyboard shortcut to toggle theme (e.g., Ctrl+Shift+D)

## Troubleshooting

### Theme not persisting
- Check browser localStorage is enabled
- Verify localStorage key 'theme' is set
- Clear localStorage and try again

### Colors not updating
- Ensure CSS custom properties are defined in App.css
- Check browser dev tools for CSS variable values
- Verify `.dark-mode` class is added to `<html>` element

### Toggle button not visible
- Check z-index conflicts with other fixed elements
- Verify ThemeToggle component is rendered in App.jsx
- Check console for any React errors

## Accessibility

The theme toggle includes accessibility features:
- **ARIA Labels**: Button has descriptive aria-label
- **Title Attribute**: Hover tooltip explains function
- **Visual Feedback**: Clear visual states for hover and active
- **Color Contrast**: Both themes meet WCAG AA standards
- **Keyboard Navigation**: Button is keyboard accessible (Tab to focus, Enter/Space to activate)

## Code Quality

The implementation follows best practices:
- **Separation of Concerns**: Theme logic separated into dedicated context
- **Reusable Components**: ThemeToggle can be easily moved or duplicated
- **CSS Variables**: Centralized theme management
- **Transitions**: Smooth UX with CSS transitions
- **Responsive**: Mobile-first responsive design
- **Performance**: Minimal re-renders with React Context
- **Persistence**: User preference saved across sessions

## License

This feature is part of the Weekly Meal Planner application and follows the same license.
