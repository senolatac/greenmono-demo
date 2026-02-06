import axios from 'axios';

const API_BASE_URL = '/api/menu';

const menuService = {
  /**
   * Get current weekly menu for a user
   * @param {number} userId - The user ID
   * @returns {Promise<Array>} Array of menu items with day, date, and meal
   */
  getCurrentMenu: async (userId) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/current`, {
        params: { userId }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching current menu:', error);
      throw error;
    }
  },

  /**
   * Generate a new 5-day menu plan
   * @param {number} userId - The user ID
   * @returns {Promise<Array>} Array of menu items with day, date, and meal
   */
  generateMenu: async (userId) => {
    try {
      const today = new Date().toISOString().split('T')[0];
      const response = await axios.post(`${API_BASE_URL}/generate`, {
        userId: userId,
        startDate: today,
        targetDailyCalories: 2000
      });
      return response.data;
    } catch (error) {
      console.error('Error generating menu:', error);
      throw error;
    }
  }
};

export default menuService;
