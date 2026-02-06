# Dark Mode Testing Guide

## Manual Testing Steps

### 1. Initial Load Test
1. Start the development server: `npm run dev`
2. Open http://localhost:3000 in your browser
3. **Expected**: Application loads in light mode (default)
4. **Verify**:
   - Purple gradient background
   - White table background
   - Dark text on light backgrounds
   - Theme toggle button visible in top-right corner showing moon icon

### 2. Toggle to Dark Mode
1. Click the theme toggle button (moon icon) in the top-right corner
2. **Expected**:
   - Smooth transition to dark mode (~300ms)
   - Background changes to dark gradient (dark blue tones)
   - Table background becomes dark gray (#27272a)
   - Text changes to light colors
   - Meal cards use darker gradients
   - Button icon changes to sun
3. **Verify**:
   - No sudden color flashes
   - All text is readable with good contrast
   - Icons and buttons are visible
   - Shadows are appropriately darker

### 3. Toggle Back to Light Mode
1. Click the theme toggle button (sun icon)
2. **Expected**:
   - Smooth transition back to light mode
   - All colors return to original light theme
   - Button icon changes to moon
3. **Verify**: Colors match the original light mode exactly

### 4. Persistence Test
1. Set theme to dark mode
2. Refresh the page (F5 or Ctrl+R)
3. **Expected**: Application loads in dark mode
4. **Verify**:
   - Theme is maintained after refresh
   - No flash of light mode before dark mode applies
   - localStorage contains: `theme: "dark"`

5. Set theme to light mode
6. Refresh the page
7. **Expected**: Application loads in light mode
8. **Verify**: localStorage contains: `theme: "light"`

### 5. LocalStorage Test
1. Open browser DevTools (F12)
2. Go to Application/Storage → Local Storage
3. Toggle theme and observe the `theme` key
4. **Expected**: Value changes between "light" and "dark"
5. Manually change the value to "dark" or "light"
6. Refresh the page
7. **Expected**: Theme matches the localStorage value

### 6. Component State Test

#### Error Message
1. Generate a menu when no ingredients exist (to trigger error)
2. **Expected (Light Mode)**: Pink/red error box with dark red text
3. Switch to dark mode
4. **Expected (Dark Mode)**: Dark red background with light red text
5. **Verify**: Error message is clearly visible in both modes

#### Loading State
1. Click "Yeni Menü Oluştur" button
2. Observe loading text: "Oluşturuluyor..."
3. **Expected**: Text color matches theme (dark gray in light, light gray in dark)
4. Toggle theme while loading (if possible)
5. **Verify**: Loading text color updates immediately

#### Empty State
1. Ensure no menu exists
2. **Expected (Light Mode)**: Gray text on gradient background
3. Switch to dark mode
4. **Expected (Dark Mode)**: Light gray text visible on dark gradient
5. **Verify**: Both paragraphs are readable in both modes

#### Table with Data
1. Generate a successful menu
2. **Expected (Light Mode)**:
   - White table background
   - Purple gradient headers
   - Light gray/blue meal cards
   - Dark text
3. Switch to dark mode
4. **Expected (Dark Mode)**:
   - Dark gray table background (#27272a)
   - Purple gradient headers (unchanged)
   - Dark gray meal cards
   - Light text (#e4e4e7)
5. **Verify**: All meal names are clearly readable

### 7. Button Interactions

#### Theme Toggle Button
1. **Hover Test**:
   - Hover over the toggle button
   - **Expected**: Button scales up slightly (1.1x), icon rotates 20deg
   - **Verify**: Smooth animation

2. **Click Test**:
   - Click the button
   - **Expected**: Button scales down briefly (0.95x), then returns
   - **Verify**: Satisfying tactile feedback

3. **Icon Test**:
   - **Light Mode**: Moon icon (crescent)
   - **Dark Mode**: Sun icon (circle with rays)
   - **Verify**: Icons are clearly visible and recognizable

#### Generate Menu Button
1. **Light Mode**: Green button with white text
2. **Dark Mode**: Green button with white text (unchanged)
3. **Verify**: Button looks good in both themes

### 8. Responsive Design Test

#### Desktop (1920px)
1. View application at full desktop width
2. Toggle theme
3. **Verify**:
   - Toggle button at 2rem from top and right
   - Toggle button size: 50px × 50px
   - All elements scale properly

#### Tablet (768px - 1024px)
1. Resize browser to ~800px width
2. Toggle theme
3. **Verify**:
   - Table remains scrollable
   - Toggle button still visible and accessible
   - All content readable

#### Mobile (< 768px)
1. Resize browser to ~375px width (iPhone size)
2. Toggle theme
3. **Verify**:
   - Toggle button at 1rem from top and right
   - Toggle button size: 45px × 45px
   - Icon size: 20px × 20px
   - Button doesn't overlap with header
   - Table scrolls horizontally if needed
   - All text readable at small size

### 9. Animation Performance Test
1. Open browser DevTools → Performance tab
2. Start recording
3. Click theme toggle 5 times rapidly
4. Stop recording
5. **Expected**:
   - No frame drops
   - Smooth 60fps transitions
   - No layout thrashing
6. **Verify**: GPU acceleration is being used for transforms

### 10. Accessibility Test

#### Keyboard Navigation
1. Press Tab key repeatedly
2. **Expected**: Toggle button receives focus
3. **Verify**: Focus outline is visible
4. Press Enter or Space while focused
5. **Expected**: Theme toggles
6. **Verify**: Works same as clicking

#### Screen Reader
1. Use screen reader (NVDA, JAWS, or VoiceOver)
2. Navigate to toggle button
3. **Expected Announcement**:
   - Light mode: "Koyu moda geç" (Switch to dark mode)
   - Dark mode: "Açık moda geç" (Switch to light mode)
4. **Verify**: Button purpose is clear

#### Tooltip
1. Hover over toggle button and wait
2. **Expected**:
   - Light mode: "Koyu mod" tooltip
   - Dark mode: "Açık mod" tooltip
3. **Verify**: Tooltip appears and is readable

### 11. Cross-Browser Test

Test the following in each browser:
- [ ] Chrome/Chromium
- [ ] Firefox
- [ ] Safari (Mac only)
- [ ] Edge

For each browser:
1. Load application
2. Toggle theme
3. Verify smooth transitions
4. Check backdrop-filter blur effect works
5. Refresh and verify persistence
6. Test responsive behavior

### 12. Edge Cases

#### Browser Without LocalStorage
1. Disable localStorage in browser settings
2. Load application
3. **Expected**: Defaults to light mode, theme toggles work but don't persist
4. **Verify**: No console errors

#### System Color Scheme
1. Change system dark mode preference (OS level)
2. **Expected**: Currently no auto-detection (future enhancement)
3. **Verify**: Application respects user's manual choice

#### Multiple Tabs
1. Open application in two tabs
2. Set theme to dark in Tab 1
3. Refresh Tab 2
4. **Expected**: Tab 2 loads in dark mode
5. Toggle in Tab 2 to light
6. **Expected**: Tab 1 remains dark (no sync between tabs)
7. Refresh Tab 1
8. **Expected**: Tab 1 now loads in light mode

## Visual Regression Checklist

Compare screenshots of light vs dark mode:

### Light Mode
- [ ] Background: Purple to violet gradient (#667eea → #764ba2)
- [ ] Table: White background (#ffffff)
- [ ] Headers: Purple gradient with white text
- [ ] Meal cards: Light gray-blue gradient
- [ ] Text: Dark gray (#2c3e50)
- [ ] Secondary text: Gray (#7f8c8d)
- [ ] Borders: Light gray (#ecf0f1)
- [ ] Toggle button: Light with moon icon

### Dark Mode
- [ ] Background: Dark blue gradient (#1a1a2e → #16213e)
- [ ] Table: Dark gray background (#27272a)
- [ ] Headers: Purple gradient with white text (same as light)
- [ ] Meal cards: Dark gray gradient (#3f3f46 → #52525b)
- [ ] Text: Light gray (#e4e4e7)
- [ ] Secondary text: Medium gray (#a1a1aa)
- [ ] Borders: Dark gray (#3f3f46)
- [ ] Toggle button: Dark with sun icon

## Test Results Template

```
Date: ___________
Tester: ___________
Browser: ___________
OS: ___________

Initial Load: ☐ PASS ☐ FAIL
Toggle to Dark: ☐ PASS ☐ FAIL
Toggle to Light: ☐ PASS ☐ FAIL
Persistence: ☐ PASS ☐ FAIL
LocalStorage: ☐ PASS ☐ FAIL
Error Message: ☐ PASS ☐ FAIL
Loading State: ☐ PASS ☐ FAIL
Empty State: ☐ PASS ☐ FAIL
Table with Data: ☐ PASS ☐ FAIL
Button Hover: ☐ PASS ☐ FAIL
Button Click: ☐ PASS ☐ FAIL
Icon Change: ☐ PASS ☐ FAIL
Responsive Desktop: ☐ PASS ☐ FAIL
Responsive Tablet: ☐ PASS ☐ FAIL
Responsive Mobile: ☐ PASS ☐ FAIL
Performance: ☐ PASS ☐ FAIL
Keyboard Navigation: ☐ PASS ☐ FAIL
Screen Reader: ☐ PASS ☐ FAIL
Tooltip: ☐ PASS ☐ FAIL

Notes:
_________________________________
_________________________________
_________________________________
```

## Automated Testing (Future)

Consider adding these automated tests:

```javascript
// Example test structure (Jest + React Testing Library)

describe('Dark Mode Feature', () => {
  test('toggles theme when button is clicked', () => {
    // Render app
    // Find toggle button
    // Click button
    // Assert dark-mode class is added to html
  });

  test('persists theme to localStorage', () => {
    // Render app
    // Toggle to dark mode
    // Assert localStorage.getItem('theme') === 'dark'
  });

  test('loads saved theme on mount', () => {
    // Set localStorage theme to 'dark'
    // Render app
    // Assert dark-mode class exists on html
  });

  test('displays correct icon for each mode', () => {
    // Render app in light mode
    // Assert moon icon is visible
    // Toggle to dark
    // Assert sun icon is visible
  });
});
```

## Performance Benchmarks

Target metrics:
- **Toggle latency**: < 16ms (one frame)
- **Transition duration**: 300ms
- **First paint after toggle**: < 50ms
- **CPU usage during transition**: < 20%
- **Memory impact**: < 1MB additional

## Known Issues

Currently no known issues. Report any bugs found during testing.

## Sign-off

All tests passing? Mark the feature as ready for production:

```
☐ All manual tests passed
☐ Cross-browser testing complete
☐ Responsive design verified
☐ Accessibility requirements met
☐ Performance benchmarks achieved
☐ Documentation updated
☐ Code reviewed
☐ Ready for deployment

Signed: ___________
Date: ___________
```
