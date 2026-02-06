# Dark Mode Testing Guide

This document provides comprehensive testing procedures for the dark mode feature in the Weekly Meal Planner application.

## Overview

The dark mode feature allows users to toggle between light and dark themes. The user's preference is persisted in localStorage and applied across all components.

## Testing Checklist

### 1. Theme Toggle Functionality

#### Test: Toggle Button Visibility
- [ ] Open the application
- [ ] Verify the theme toggle button is visible in the top-right corner
- [ ] Button should display a moon icon in light mode
- [ ] Button should display a sun icon in dark mode

#### Test: Theme Switching
- [ ] Click the theme toggle button
- [ ] Verify the page transitions to dark mode
- [ ] All colors should invert appropriately
- [ ] Click the button again
- [ ] Verify the page returns to light mode

#### Test: Theme Persistence
- [ ] Toggle to dark mode
- [ ] Refresh the browser
- [ ] Verify dark mode is still active
- [ ] Open the developer console
- [ ] Check localStorage for `theme` key
- [ ] Value should be 'dark' when in dark mode
- [ ] Value should be 'light' when in light mode

### 2. Component-Specific Testing

#### Header and Navigation
- [ ] **Light Mode**: Header should have gradient background (purple tones)
- [ ] **Dark Mode**: Header should have darker gradient background
- [ ] Tab buttons should be clearly visible in both modes
- [ ] Active tab should have appropriate background color
- [ ] Text should be readable in both modes

#### Ingredient Manager

##### Form Section
- [ ] Form background adapts to theme
- [ ] Input fields have appropriate background and text colors
- [ ] Labels are clearly visible
- [ ] Form inputs maintain usability in both modes
- [ ] Dropdown selects are readable
- [ ] Submit button is visible and clickable

##### Ingredient List
- [ ] Table background adapts to theme
- [ ] Table header remains readable
- [ ] Row hover effects work in both modes
- [ ] Category badges adapt colors appropriately
- [ ] Delete buttons are visible
- [ ] Pagination controls are readable

##### Error Messages
- [ ] Error messages display correctly in light mode (red tones)
- [ ] Error messages display correctly in dark mode (lighter red tones)
- [ ] Error text is readable in both modes

##### Delete Modal
- [ ] Modal background adapts to theme
- [ ] Modal text is readable
- [ ] Button colors are appropriate
- [ ] Modal overlay is visible in both modes

#### Weekly Menu Table
- [ ] Table background adapts to theme
- [ ] Table header gradient is visible
- [ ] Day headers are readable
- [ ] Meal items adapt their background colors
- [ ] Hover effects work correctly
- [ ] Empty state message is readable
- [ ] Loading state is visible in both modes
- [ ] Generate button is clearly visible

### 3. Visual Consistency Tests

#### Color Variables
Verify these CSS variables are applied correctly:

**Light Mode:**
- Background: white (#ffffff)
- Primary text: dark gray (#2c3e50)
- Secondary text: medium gray (#7f8c8d)
- Borders: light gray (#ecf0f1)

**Dark Mode:**
- Background: dark gray (#27272a)
- Primary text: light gray (#e4e4e7)
- Secondary text: medium gray (#a1a1aa)
- Borders: darker gray (#3f3f46)

#### Gradients
- [ ] App header gradient adapts to theme
- [ ] Table header gradients remain visible
- [ ] Meal item gradients adapt appropriately

### 4. Responsive Design Tests

#### Desktop (1200px+)
- [ ] Theme toggle button positioned correctly
- [ ] All components adapt to theme
- [ ] No layout shifts during theme change

#### Tablet (768px - 1199px)
- [ ] Theme toggle button remains accessible
- [ ] Components maintain theme consistency
- [ ] Layout remains functional

#### Mobile (< 768px)
- [ ] Theme toggle button is smaller but visible
- [ ] All text remains readable
- [ ] Touch targets are appropriately sized
- [ ] Tables scroll horizontally if needed

### 5. Accessibility Tests

#### Keyboard Navigation
- [ ] Theme toggle button is keyboard accessible
- [ ] Press Tab to focus on toggle button
- [ ] Press Enter or Space to toggle theme
- [ ] All form inputs are accessible in both modes

#### Screen Reader
- [ ] Toggle button has appropriate aria-label
- [ ] Label updates based on current theme
- [ ] "Açık moda geç" (Switch to light mode) in dark mode
- [ ] "Koyu moda geç" (Switch to dark mode) in light mode

#### Contrast
- [ ] Text has sufficient contrast in light mode (WCAG AA)
- [ ] Text has sufficient contrast in dark mode (WCAG AA)
- [ ] Interactive elements are clearly distinguishable

### 6. Performance Tests

#### Animation Smoothness
- [ ] Theme transition is smooth (0.3s)
- [ ] No flickering during theme change
- [ ] CSS transitions work correctly
- [ ] Button hover effects are smooth

#### Initial Load
- [ ] Theme loads correctly on first visit (defaults to light)
- [ ] Theme loads from localStorage on subsequent visits
- [ ] No flash of unstyled content
- [ ] Theme applies before page render

### 7. Browser Compatibility

Test the following browsers:

#### Chrome/Edge
- [ ] Theme toggle works
- [ ] localStorage persists
- [ ] All styles apply correctly
- [ ] Transitions are smooth

#### Firefox
- [ ] Theme toggle works
- [ ] localStorage persists
- [ ] All styles apply correctly
- [ ] Transitions are smooth

#### Safari
- [ ] Theme toggle works
- [ ] localStorage persists
- [ ] All styles apply correctly
- [ ] Backdrop filters work (theme toggle)

### 8. Edge Cases

#### localStorage Disabled
- [ ] App still works if localStorage is disabled
- [ ] Theme defaults to light mode
- [ ] No JavaScript errors in console

#### Invalid localStorage Value
- [ ] App handles invalid theme values gracefully
- [ ] Defaults to light mode if value is corrupted

#### Multiple Tabs
- [ ] Open app in two browser tabs
- [ ] Change theme in one tab
- [ ] Refresh second tab
- [ ] Theme should match (both tabs use same localStorage)

## Known Issues

None at this time.

## Test Results Template

```
Test Date: ____________________
Tester: ____________________
Browser: ____________________
OS: ____________________

Pass/Fail Summary:
- Theme Toggle: [ ] Pass [ ] Fail
- Theme Persistence: [ ] Pass [ ] Fail
- Component Styling: [ ] Pass [ ] Fail
- Responsive Design: [ ] Pass [ ] Fail
- Accessibility: [ ] Pass [ ] Fail
- Performance: [ ] Pass [ ] Fail

Notes:
_________________________________
_________________________________
_________________________________
```

## Automated Testing Recommendations

For future implementation, consider adding:

1. **Unit Tests** (Jest + React Testing Library)
   - ThemeContext state management
   - ThemeToggle click behavior
   - localStorage interactions

2. **Integration Tests**
   - Component rendering in both themes
   - Theme persistence across navigation

3. **Visual Regression Tests** (Percy, Chromatic)
   - Screenshot comparisons
   - Theme transition validation

4. **E2E Tests** (Cypress, Playwright)
   - Full user flow testing
   - Cross-browser validation
   - localStorage behavior

## Conclusion

This testing guide ensures comprehensive coverage of the dark mode feature. All tests should pass before considering the feature production-ready.
