/**
 * Unit Tests for ThemeToggle Component
 *
 * Tests the theme toggle button component including rendering,
 * click behavior, accessibility, and visual states.
 */

import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import ThemeToggle from '../components/ThemeToggle';
import { ThemeProvider } from '../context/ThemeContext';

// Mock localStorage
const localStorageMock = (() => {
  let store = {};

  return {
    getItem: (key) => store[key] || null,
    setItem: (key, value) => {
      store[key] = value.toString();
    },
    clear: () => {
      store = {};
    }
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock
});

describe('ThemeToggle Component', () => {
  beforeEach(() => {
    localStorage.clear();
    document.documentElement.classList.remove('dark-mode');
  });

  const renderWithThemeProvider = (component) => {
    return render(<ThemeProvider>{component}</ThemeProvider>);
  };

  describe('Rendering', () => {
    it('should render the toggle button', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toBeInTheDocument();
    });

    it('should have theme-toggle class', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toHaveClass('theme-toggle');
    });

    it('should render moon icon in light mode', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');

      expect(svg).toBeInTheDocument();
      // Moon icon has a path element
      expect(svg.querySelector('path')).toBeInTheDocument();
    });

    it('should render sun icon in dark mode', () => {
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');

      expect(svg).toBeInTheDocument();
      // Sun icon has circle and line elements
      expect(svg.querySelector('circle')).toBeInTheDocument();
      expect(svg.querySelectorAll('line').length).toBeGreaterThan(0);
    });
  });

  describe('Accessibility', () => {
    it('should have appropriate aria-label in light mode', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toHaveAttribute('aria-label', 'Koyu moda geç');
    });

    it('should have appropriate aria-label in dark mode', () => {
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toHaveAttribute('aria-label', 'Açık moda geç');
    });

    it('should have appropriate title in light mode', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toHaveAttribute('title', 'Koyu mod');
    });

    it('should have appropriate title in dark mode', () => {
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      expect(button).toHaveAttribute('title', 'Açık mod');
    });

    it('should be keyboard accessible', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Should be focusable
      button.focus();
      expect(document.activeElement).toBe(button);
    });
  });

  describe('Click Behavior', () => {
    it('should toggle theme when clicked', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Initially light mode
      expect(localStorage.getItem('theme')).toBe('light');

      // Click to toggle
      fireEvent.click(button);

      // Should be dark mode now
      expect(localStorage.getItem('theme')).toBe('dark');
      expect(document.documentElement.classList.contains('dark-mode')).toBe(true);
    });

    it('should toggle back to light mode when clicked again', () => {
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Initially dark mode
      expect(localStorage.getItem('theme')).toBe('dark');

      // Click to toggle
      fireEvent.click(button);

      // Should be light mode now
      expect(localStorage.getItem('theme')).toBe('light');
      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
    });

    it('should update icon when clicked', () => {
      const { rerender } = renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      let svg = button.querySelector('svg');

      // Initially shows moon icon (light mode)
      expect(svg.querySelector('path')).toBeInTheDocument();

      // Click to toggle
      fireEvent.click(button);

      // Re-render to get updated DOM
      rerender(
        <ThemeProvider>
          <ThemeToggle />
        </ThemeProvider>
      );

      svg = button.querySelector('svg');

      // Should now show sun icon (dark mode)
      expect(svg.querySelector('circle')).toBeInTheDocument();
    });

    it('should update aria-label when clicked', () => {
      const { rerender } = renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Initially light mode
      expect(button).toHaveAttribute('aria-label', 'Koyu moda geç');

      // Click to toggle
      fireEvent.click(button);

      // Re-render to get updated DOM
      rerender(
        <ThemeProvider>
          <ThemeToggle />
        </ThemeProvider>
      );

      // Should now be dark mode
      expect(button).toHaveAttribute('aria-label', 'Açık moda geç');
    });
  });

  describe('Multiple Toggles', () => {
    it('should handle multiple clicks correctly', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Click 1: Light -> Dark
      fireEvent.click(button);
      expect(localStorage.getItem('theme')).toBe('dark');

      // Click 2: Dark -> Light
      fireEvent.click(button);
      expect(localStorage.getItem('theme')).toBe('light');

      // Click 3: Light -> Dark
      fireEvent.click(button);
      expect(localStorage.getItem('theme')).toBe('dark');

      // Click 4: Dark -> Light
      fireEvent.click(button);
      expect(localStorage.getItem('theme')).toBe('light');
    });
  });

  describe('Icon Rendering', () => {
    it('should render SVG with correct viewBox', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');

      expect(svg).toHaveAttribute('viewBox', '0 0 24 24');
    });

    it('should render SVG with theme-icon class', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');

      expect(svg).toHaveClass('theme-icon');
    });

    it('should render moon icon with correct path', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');
      const path = svg.querySelector('path');

      expect(path).toHaveAttribute(
        'd',
        'M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z'
      );
    });

    it('should render sun icon with correct elements in dark mode', () => {
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');
      const svg = button.querySelector('svg');

      // Sun icon should have:
      // 1 circle
      expect(svg.querySelectorAll('circle').length).toBe(1);

      // 8 lines (for sun rays)
      expect(svg.querySelectorAll('line').length).toBe(8);
    });
  });

  describe('Integration with ThemeContext', () => {
    it('should use theme from context', () => {
      // Set dark mode in localStorage
      localStorage.setItem('theme', 'dark');

      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Should reflect dark mode from context
      expect(button).toHaveAttribute('aria-label', 'Açık moda geç');
    });

    it('should call toggleTheme from context when clicked', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Initial state
      expect(localStorage.getItem('theme')).toBe('light');

      // Click should trigger toggleTheme
      fireEvent.click(button);

      // Theme should be toggled
      expect(localStorage.getItem('theme')).toBe('dark');
    });
  });

  describe('Edge Cases', () => {
    it('should handle rapid clicks', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      // Rapid clicks
      for (let i = 0; i < 10; i++) {
        fireEvent.click(button);
      }

      // Should end up in light mode (even number of clicks)
      expect(localStorage.getItem('theme')).toBe('light');
    });

    it('should not throw error when clicked', () => {
      renderWithThemeProvider(<ThemeToggle />);

      const button = screen.getByRole('button');

      expect(() => {
        fireEvent.click(button);
      }).not.toThrow();
    });
  });
});
