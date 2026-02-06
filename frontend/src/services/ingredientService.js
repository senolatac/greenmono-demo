import axios from 'axios';

const API_BASE_URL = '/api/ingredients';

const ingredientService = {
  /**
   * Get all ingredients with pagination and optional category filter
   * @param {Object} params - Query parameters
   * @param {string} [params.category] - Filter by category
   * @param {number} [params.page] - Page number (0-indexed)
   * @param {number} [params.size] - Page size
   * @param {string} [params.sortBy] - Sort field
   * @param {string} [params.sortDirection] - Sort direction (ASC/DESC)
   * @returns {Promise<Object>} Paginated ingredient list
   */
  getAll: async (params = {}) => {
    try {
      const response = await axios.get(API_BASE_URL, { params });
      return response.data;
    } catch (error) {
      console.error('Error fetching ingredients:', error);
      throw error;
    }
  },

  /**
   * Create a new ingredient
   * @param {Object} ingredient - Ingredient data
   * @returns {Promise<Object>} Created ingredient
   */
  create: async (ingredient) => {
    try {
      const response = await axios.post(API_BASE_URL, ingredient);
      return response.data;
    } catch (error) {
      console.error('Error creating ingredient:', error);
      throw error;
    }
  },

  /**
   * Delete an ingredient by ID
   * @param {number} id - Ingredient ID
   * @returns {Promise<void>}
   */
  delete: async (id) => {
    try {
      await axios.delete(`${API_BASE_URL}/${id}`);
    } catch (error) {
      console.error('Error deleting ingredient:', error);
      throw error;
    }
  }
};

export default ingredientService;
