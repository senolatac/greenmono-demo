import React, { useState } from 'react';

const CATEGORIES = [
  { value: 'VEGETABLES', label: 'Sebzeler' },
  { value: 'FRUITS', label: 'Meyveler' },
  { value: 'MEAT', label: 'Et' },
  { value: 'POULTRY', label: 'Kümes Hayvanları' },
  { value: 'FISH', label: 'Balık' },
  { value: 'SEAFOOD', label: 'Deniz Ürünleri' },
  { value: 'DAIRY', label: 'Süt Ürünleri' },
  { value: 'GRAINS', label: 'Tahıllar' },
  { value: 'LEGUMES', label: 'Baklagiller' },
  { value: 'NUTS_SEEDS', label: 'Kuruyemişler' },
  { value: 'HERBS_SPICES', label: 'Baharatlar' },
  { value: 'OILS_FATS', label: 'Yağlar' },
  { value: 'CONDIMENTS', label: 'Çeşniler' },
  { value: 'BEVERAGES', label: 'İçecekler' },
  { value: 'OTHER', label: 'Diğer' }
];

const UNITS = [
  { value: 'GRAM', label: 'Gram' },
  { value: 'KILOGRAM', label: 'Kilogram' },
  { value: 'MILLILITER', label: 'Mililitre' },
  { value: 'LITER', label: 'Litre' },
  { value: 'PIECE', label: 'Adet' },
  { value: 'TABLESPOON', label: 'Yemek Kaşığı' },
  { value: 'TEASPOON', label: 'Çay Kaşığı' },
  { value: 'CUP', label: 'Bardak' },
  { value: 'OUNCE', label: 'Ons' },
  { value: 'POUND', label: 'Pound' }
];

const INITIAL_FORM = {
  name: '',
  category: '',
  quantity: '',
  unit: '',
  expiryDate: '',
  notes: ''
};

const IngredientForm = ({ onIngredientAdded, loading }) => {
  const [form, setForm] = useState(INITIAL_FORM);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      name: form.name.trim(),
      category: form.category,
      quantity: parseFloat(form.quantity),
      unit: form.unit,
      expiryDate: form.expiryDate || null,
      notes: form.notes.trim() || null,
      available: true,
      userId: 1
    };
    const success = await onIngredientAdded(payload);
    if (success) {
      setForm(INITIAL_FORM);
    }
  };

  const isValid = form.name.trim() && form.category && form.quantity > 0 && form.unit;

  return (
    <form className="ingredient-form" onSubmit={handleSubmit}>
      <h2>Yeni Malzeme Ekle</h2>
      <div className="ingredient-form-grid">
        <div className="ingredient-form-group">
          <label htmlFor="ing-name">Malzeme Adı *</label>
          <input
            id="ing-name"
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="Örn: Domates"
            maxLength={100}
            required
          />
        </div>

        <div className="ingredient-form-group">
          <label htmlFor="ing-category">Kategori *</label>
          <select
            id="ing-category"
            name="category"
            value={form.category}
            onChange={handleChange}
            required
          >
            <option value="">Seçiniz</option>
            {CATEGORIES.map((c) => (
              <option key={c.value} value={c.value}>{c.label}</option>
            ))}
          </select>
        </div>

        <div className="ingredient-form-group">
          <label htmlFor="ing-quantity">Miktar *</label>
          <input
            id="ing-quantity"
            type="number"
            name="quantity"
            value={form.quantity}
            onChange={handleChange}
            placeholder="Örn: 500"
            min="0.01"
            step="0.01"
            required
          />
        </div>

        <div className="ingredient-form-group">
          <label htmlFor="ing-unit">Birim *</label>
          <select
            id="ing-unit"
            name="unit"
            value={form.unit}
            onChange={handleChange}
            required
          >
            <option value="">Seçiniz</option>
            {UNITS.map((u) => (
              <option key={u.value} value={u.value}>{u.label}</option>
            ))}
          </select>
        </div>

        <div className="ingredient-form-group">
          <label htmlFor="ing-expiry">Son Kullanma Tarihi</label>
          <input
            id="ing-expiry"
            type="date"
            name="expiryDate"
            value={form.expiryDate}
            onChange={handleChange}
          />
        </div>

        <div className="ingredient-form-group">
          <label htmlFor="ing-notes">Notlar</label>
          <input
            id="ing-notes"
            type="text"
            name="notes"
            value={form.notes}
            onChange={handleChange}
            placeholder="İsteğe bağlı"
            maxLength={500}
          />
        </div>
      </div>

      <button
        type="submit"
        className="ingredient-submit-btn"
        disabled={!isValid || loading}
      >
        {loading ? 'Ekleniyor...' : 'Malzeme Ekle'}
      </button>
    </form>
  );
};

export { CATEGORIES };
export default IngredientForm;
