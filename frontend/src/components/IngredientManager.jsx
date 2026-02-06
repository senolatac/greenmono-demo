import React, { useState, useEffect, useCallback } from 'react';
import ingredientService from '../services/ingredientService';
import IngredientForm from './IngredientForm';
import IngredientList from './IngredientList';
import './IngredientManager.css';

const PAGE_SIZE = 10;

const IngredientManager = () => {
  const [ingredients, setIngredients] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [categoryFilter, setCategoryFilter] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchIngredients = useCallback(async (page = 0, category = '') => {
    setLoading(true);
    setError(null);
    try {
      const params = {
        page,
        size: PAGE_SIZE,
        sortBy: 'name',
        sortDirection: 'ASC'
      };
      if (category) {
        params.category = category;
      }
      const data = await ingredientService.getAll(params);
      setIngredients(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(data.pageNumber);
    } catch (err) {
      console.error('Error loading ingredients:', err);
      setError('Malzemeler yüklenirken bir hata oluştu.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchIngredients(currentPage, categoryFilter);
  }, [fetchIngredients, currentPage, categoryFilter]);

  const handleIngredientAdded = async (ingredient) => {
    setLoading(true);
    setError(null);
    try {
      await ingredientService.create(ingredient);
      await fetchIngredients(0, categoryFilter);
      setCurrentPage(0);
      return true;
    } catch (err) {
      console.error('Error adding ingredient:', err);
      if (err.response && err.response.status === 409) {
        setError('Bu malzeme zaten mevcut.');
      } else {
        setError('Malzeme eklenirken bir hata oluştu.');
      }
      setLoading(false);
      return false;
    }
  };

  const handleDelete = async (id) => {
    setLoading(true);
    setError(null);
    try {
      await ingredientService.delete(id);
      await fetchIngredients(currentPage, categoryFilter);
    } catch (err) {
      console.error('Error deleting ingredient:', err);
      if (err.response && err.response.status === 404) {
        setError('Malzeme bulunamadı.');
      } else {
        setError('Malzeme silinirken bir hata oluştu.');
      }
      setLoading(false);
    }
  };

  const handleCategoryChange = (category) => {
    setCategoryFilter(category);
    setCurrentPage(0);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="ingredient-manager">
      {error && (
        <div className="ingredient-error">
          {error}
        </div>
      )}

      <IngredientForm
        onIngredientAdded={handleIngredientAdded}
        loading={loading}
      />

      <IngredientList
        ingredients={ingredients}
        totalPages={totalPages}
        currentPage={currentPage}
        totalElements={totalElements}
        categoryFilter={categoryFilter}
        onCategoryChange={handleCategoryChange}
        onPageChange={handlePageChange}
        onDelete={handleDelete}
        loading={loading}
      />
    </div>
  );
};

export default IngredientManager;
