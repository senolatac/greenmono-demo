/**
 * Unit Tests for ThemeContext
 *
 * Tests the theme context provider, theme state management,
 * localStorage persistence, and theme toggle functionality.
 */

import { describe, it, expect, beforeEach, vi } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { ThemeProvider, useTheme } from '../context/ThemeContext';

// Mock localStorage
const localStorageMock = (() => {
  let store = {};

  return {
    getItem: (key) => store[key] || null,
    setItem: (key, value) => {
      store[key] = value.toString();
    },
    removeItem: (key) => {
      delete store[key];
    },
    clear: () => {
      store = {};
    }
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock
});

describe('ThemeContext', () => {
  beforeEach(() => {
    localStorage.clear();
    document.documentElement.classList.remove('dark-mode');
  });

  describe('ThemeProvider', () => {
    it('should provide theme context to children', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current).toBeDefined();
      expect(result.current.isDarkMode).toBeDefined();
      expect(result.current.toggleTheme).toBeDefined();
    });

    it('should throw error when useTheme is used outside ThemeProvider', () => {
      // Suppress console.error for this test
      const originalError = console.error;
      console.error = vi.fn();

      expect(() => {
        renderHook(() => useTheme());
      }).toThrow('useTheme must be used within a ThemeProvider');

      console.error = originalError;
    });
  });

  describe('Initial State', () => {
    it('should default to light mode when no localStorage value exists', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);
      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
    });

    it('should initialize with dark mode when localStorage has "dark"', () => {
      localStorage.setItem('theme', 'dark');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(true);
      expect(document.documentElement.classList.contains('dark-mode')).toBe(true);
    });

    it('should initialize with light mode when localStorage has "light"', () => {
      localStorage.setItem('theme', 'light');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);
      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
    });

    it('should default to light mode with invalid localStorage value', () => {
      localStorage.setItem('theme', 'invalid');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);
    });
  });

  describe('toggleTheme', () => {
    it('should toggle from light to dark mode', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);

      act(() => {
        result.current.toggleTheme();
      });

      expect(result.current.isDarkMode).toBe(true);
      expect(document.documentElement.classList.contains('dark-mode')).toBe(true);
      expect(localStorage.getItem('theme')).toBe('dark');
    });

    it('should toggle from dark to light mode', () => {
      localStorage.setItem('theme', 'dark');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(true);

      act(() => {
        result.current.toggleTheme();
      });

      expect(result.current.isDarkMode).toBe(false);
      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
      expect(localStorage.getItem('theme')).toBe('light');
    });

    it('should toggle multiple times correctly', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      // Light -> Dark
      act(() => {
        result.current.toggleTheme();
      });
      expect(result.current.isDarkMode).toBe(true);

      // Dark -> Light
      act(() => {
        result.current.toggleTheme();
      });
      expect(result.current.isDarkMode).toBe(false);

      // Light -> Dark
      act(() => {
        result.current.toggleTheme();
      });
      expect(result.current.isDarkMode).toBe(true);
    });
  });

  describe('localStorage Persistence', () => {
    it('should persist dark mode to localStorage', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      act(() => {
        result.current.toggleTheme();
      });

      expect(localStorage.getItem('theme')).toBe('dark');
    });

    it('should persist light mode to localStorage', () => {
      localStorage.setItem('theme', 'dark');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      act(() => {
        result.current.toggleTheme();
      });

      expect(localStorage.getItem('theme')).toBe('light');
    });

    it('should maintain theme across remounts', () => {
      // First mount - toggle to dark
      let wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      let { result, unmount } = renderHook(() => useTheme(), { wrapper });

      act(() => {
        result.current.toggleTheme();
      });

      expect(result.current.isDarkMode).toBe(true);
      unmount();

      // Second mount - should still be dark
      wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      ({ result } = renderHook(() => useTheme(), { wrapper }));

      expect(result.current.isDarkMode).toBe(true);
    });
  });

  describe('DOM Class Manipulation', () => {
    it('should add dark-mode class when toggled to dark', () => {
      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      act(() => {
        result.current.toggleTheme();
      });

      expect(document.documentElement.classList.contains('dark-mode')).toBe(true);
    });

    it('should remove dark-mode class when toggled to light', () => {
      localStorage.setItem('theme', 'dark');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      act(() => {
        result.current.toggleTheme();
      });

      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
    });

    it('should apply dark-mode class on mount if theme is dark', () => {
      localStorage.setItem('theme', 'dark');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      renderHook(() => useTheme(), { wrapper });

      expect(document.documentElement.classList.contains('dark-mode')).toBe(true);
    });

    it('should not apply dark-mode class on mount if theme is light', () => {
      localStorage.setItem('theme', 'light');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      renderHook(() => useTheme(), { wrapper });

      expect(document.documentElement.classList.contains('dark-mode')).toBe(false);
    });
  });

  describe('Edge Cases', () => {
    it('should handle localStorage being unavailable', () => {
      // Mock localStorage to throw errors
      const originalGetItem = localStorage.getItem;
      const originalSetItem = localStorage.setItem;

      localStorage.getItem = vi.fn(() => {
        throw new Error('localStorage unavailable');
      });
      localStorage.setItem = vi.fn(() => {
        throw new Error('localStorage unavailable');
      });

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;

      // Should not throw error
      expect(() => {
        renderHook(() => useTheme(), { wrapper });
      }).not.toThrow();

      // Restore
      localStorage.getItem = originalGetItem;
      localStorage.setItem = originalSetItem;
    });

    it('should handle empty string in localStorage', () => {
      localStorage.setItem('theme', '');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);
    });

    it('should handle null in localStorage', () => {
      localStorage.removeItem('theme');

      const wrapper = ({ children }) => <ThemeProvider>{children}</ThemeProvider>;
      const { result } = renderHook(() => useTheme(), { wrapper });

      expect(result.current.isDarkMode).toBe(false);
    });
  });
});
