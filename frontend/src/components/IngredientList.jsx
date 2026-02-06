import React, { useState } from 'react';
import { CATEGORIES } from './IngredientForm';

const UNIT_LABELS = {
  GRAM: 'g',
  KILOGRAM: 'kg',
  MILLILITER: 'ml',
  LITER: 'L',
  PIECE: 'Adet',
  TABLESPOON: 'Yemek K.',
  TEASPOON: 'Çay K.',
  CUP: 'Bardak',
  OUNCE: 'oz',
  POUND: 'lb'
};

const CATEGORY_LABELS = Object.fromEntries(
  CATEGORIES.map((c) => [c.value, c.label])
);

const IngredientList = ({
  ingredients,
  totalPages,
  currentPage,
  totalElements,
  categoryFilter,
  onCategoryChange,
  onPageChange,
  onDelete,
  loading
}) => {
  const [deleteTarget, setDeleteTarget] = useState(null);

  const handleDeleteClick = (ingredient) => {
    setDeleteTarget(ingredient);
  };

  const confirmDelete = async () => {
    if (deleteTarget) {
      await onDelete(deleteTarget.id);
      setDeleteTarget(null);
    }
  };

  const cancelDelete = () => {
    setDeleteTarget(null);
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleDateString('tr-TR');
  };

  return (
    <div className="ingredient-list">
      <div className="ingredient-list-header">
        <h2>Malzeme Listesi ({totalElements})</h2>
        <div className="ingredient-filter">
          <label htmlFor="category-filter">Kategori:</label>
          <select
            id="category-filter"
            value={categoryFilter}
            onChange={(e) => onCategoryChange(e.target.value)}
          >
            <option value="">Tümü</option>
            {CATEGORIES.map((c) => (
              <option key={c.value} value={c.value}>{c.label}</option>
            ))}
          </select>
        </div>
      </div>

      {loading && ingredients.length === 0 ? (
        <div className="ingredient-loading">Yükleniyor...</div>
      ) : ingredients.length === 0 ? (
        <div className="ingredient-empty">
          <p>Henüz malzeme eklenmemiş.</p>
          <p>Yukarıdaki formu kullanarak malzeme ekleyebilirsiniz.</p>
        </div>
      ) : (
        <>
          <div className="ingredient-table-wrapper">
            <table className="ingredient-table">
              <thead>
                <tr>
                  <th>Malzeme</th>
                  <th>Kategori</th>
                  <th>Miktar</th>
                  <th>Son Kullanma</th>
                  <th>Notlar</th>
                  <th>İşlem</th>
                </tr>
              </thead>
              <tbody>
                {ingredients.map((item) => (
                  <tr key={item.id}>
                    <td className="ingredient-name-cell">{item.name}</td>
                    <td>
                      <span className="ingredient-category-badge">
                        {CATEGORY_LABELS[item.category] || item.category}
                      </span>
                    </td>
                    <td>{item.quantity} {UNIT_LABELS[item.unit] || item.unit}</td>
                    <td>{formatDate(item.expiryDate)}</td>
                    <td className="ingredient-notes-cell">{item.notes || '-'}</td>
                    <td>
                      <button
                        className="ingredient-delete-btn"
                        onClick={() => handleDeleteClick(item)}
                        title="Sil"
                      >
                        Sil
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {totalPages > 1 && (
            <div className="ingredient-pagination">
              <button
                disabled={currentPage === 0}
                onClick={() => onPageChange(currentPage - 1)}
              >
                ← Önceki
              </button>
              <span className="ingredient-page-info">
                Sayfa {currentPage + 1} / {totalPages}
              </span>
              <button
                disabled={currentPage >= totalPages - 1}
                onClick={() => onPageChange(currentPage + 1)}
              >
                Sonraki →
              </button>
            </div>
          )}
        </>
      )}

      {deleteTarget && (
        <div className="ingredient-modal-overlay" onClick={cancelDelete}>
          <div className="ingredient-modal" onClick={(e) => e.stopPropagation()}>
            <h3>Malzeme Sil</h3>
            <p>
              <strong>{deleteTarget.name}</strong> malzemesini silmek istediğinizden emin misiniz?
            </p>
            <div className="ingredient-modal-actions">
              <button className="ingredient-modal-cancel" onClick={cancelDelete}>
                İptal
              </button>
              <button className="ingredient-modal-confirm" onClick={confirmDelete}>
                Sil
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default IngredientList;
