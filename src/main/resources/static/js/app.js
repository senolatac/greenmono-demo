// API Configuration
const API_BASE_URL = '/api/ingredients';

// State Management
let currentPage = 0;
let totalPages = 1;
let currentFilter = '';
let deleteIngredientId = null;

// Category Translations
const categoryTranslations = {
    'VEGETABLES': 'Sebzeler',
    'FRUITS': 'Meyveler',
    'MEAT': 'Et',
    'POULTRY': 'Kümes Hayvanları',
    'FISH': 'Balık',
    'SEAFOOD': 'Deniz Ürünleri',
    'DAIRY': 'Süt Ürünleri',
    'GRAINS': 'Tahıllar',
    'LEGUMES': 'Baklagiller',
    'NUTS_SEEDS': 'Kuruyemiş ve Tohumlar',
    'HERBS_SPICES': 'Otlar ve Baharatlar',
    'OILS_FATS': 'Yağlar',
    'CONDIMENTS': 'Soslar ve Çeşniler',
    'BEVERAGES': 'İçecekler',
    'OTHER': 'Diğer'
};

// Unit Translations
const unitTranslations = {
    'GRAM': 'gr',
    'KILOGRAM': 'kg',
    'MILLILITER': 'ml',
    'LITER': 'lt',
    'PIECE': 'adet',
    'TABLESPOON': 'yemek kaşığı',
    'TEASPOON': 'çay kaşığı',
    'CUP': 'fincan',
    'OUNCE': 'ons',
    'POUND': 'pound'
};

// Initialize app on page load
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Load ingredients on page load
    loadIngredients();

    // Setup form submission
    const form = document.getElementById('ingredientForm');
    form.addEventListener('submit', handleFormSubmit);

    // Setup filter
    const filterCategory = document.getElementById('filterCategory');
    filterCategory.addEventListener('change', handleFilterChange);

    // Setup pagination
    document.getElementById('prevPage').addEventListener('click', () => changePage(currentPage - 1));
    document.getElementById('nextPage').addEventListener('click', () => changePage(currentPage + 1));

    // Set minimum date to today for expiry date
    const expiryDateInput = document.getElementById('expiryDate');
    const today = new Date().toISOString().split('T')[0];
    expiryDateInput.setAttribute('min', today);
}

// Load ingredients from API
async function loadIngredients(page = 0, category = '') {
    showLoading(true);
    hideMessages();

    try {
        let url = `${API_BASE_URL}?page=${page}&size=20&sortBy=name&sortDirection=ASC`;
        if (category) {
            url += `&category=${category}`;
        }

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        currentPage = data.currentPage;
        totalPages = data.totalPages;

        displayIngredients(data.content);
        updatePagination();

    } catch (error) {
        console.error('Error loading ingredients:', error);
        showError('Malzemeler yüklenirken bir hata oluştu: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Display ingredients in table
function displayIngredients(ingredients) {
    const tbody = document.getElementById('ingredientsTableBody');

    if (!ingredients || ingredients.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="no-data">Henüz malzeme eklenmedi</td></tr>';
        return;
    }

    tbody.innerHTML = ingredients.map(ingredient => `
        <tr>
            <td data-label="Malzeme Adı">${escapeHtml(ingredient.name)}</td>
            <td data-label="Kategori">${categoryTranslations[ingredient.category] || ingredient.category}</td>
            <td data-label="Miktar">${ingredient.quantity}</td>
            <td data-label="Birim">${unitTranslations[ingredient.unit] || ingredient.unit}</td>
            <td data-label="Son Kullanma">${ingredient.expiryDate ? formatDate(ingredient.expiryDate) : '-'}</td>
            <td data-label="Durum">${getStatusBadge(ingredient)}</td>
            <td data-label="İşlemler">
                <button class="btn btn-danger action-btn" onclick="openDeleteModal(${ingredient.id})">
                    Sil
                </button>
            </td>
        </tr>
    `).join('');
}

// Get status badge HTML
function getStatusBadge(ingredient) {
    if (!ingredient.available) {
        return '<span class="status-badge status-unavailable">Mevcut Değil</span>';
    }

    if (ingredient.expiryDate) {
        const today = new Date();
        const expiryDate = new Date(ingredient.expiryDate);
        const daysUntilExpiry = Math.ceil((expiryDate - today) / (1000 * 60 * 60 * 24));

        if (daysUntilExpiry < 0) {
            return '<span class="status-badge status-unavailable">Tarihi Geçmiş</span>';
        } else if (daysUntilExpiry <= 3) {
            return '<span class="status-badge status-expiring">Yakında Bitecek</span>';
        }
    }

    return '<span class="status-badge status-available">Mevcut</span>';
}

// Handle form submission
async function handleFormSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);

    const ingredientData = {
        name: formData.get('name').trim(),
        category: formData.get('category'),
        quantity: parseFloat(formData.get('quantity')),
        unit: formData.get('unit'),
        expiryDate: formData.get('expiryDate') || null,
        notes: formData.get('notes')?.trim() || null,
        available: formData.get('available') === 'on',
        userId: 1 // Default user ID - you can change this based on authentication
    };

    // Validation
    if (!ingredientData.name) {
        showError('Malzeme adı zorunludur');
        return;
    }
    if (!ingredientData.category) {
        showError('Kategori seçimi zorunludur');
        return;
    }
    if (!ingredientData.quantity || ingredientData.quantity <= 0) {
        showError('Geçerli bir miktar giriniz');
        return;
    }
    if (!ingredientData.unit) {
        showError('Birim seçimi zorunludur');
        return;
    }

    try {
        showLoading(true);
        hideMessages();

        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(ingredientData)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        showSuccess(`"${data.name}" başarıyla eklendi!`);
        form.reset();

        // Reload ingredients list
        await loadIngredients(currentPage, currentFilter);

    } catch (error) {
        console.error('Error creating ingredient:', error);
        showError('Malzeme eklenirken bir hata oluştu: ' + error.message);
    } finally {
        showLoading(false);
    }
}

// Handle filter change
function handleFilterChange(event) {
    currentFilter = event.target.value;
    currentPage = 0;
    loadIngredients(currentPage, currentFilter);
}

// Change page
function changePage(newPage) {
    if (newPage >= 0 && newPage < totalPages) {
        loadIngredients(newPage, currentFilter);
    }
}

// Update pagination controls
function updatePagination() {
    const paginationDiv = document.getElementById('pagination');
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');

    if (totalPages <= 1) {
        paginationDiv.style.display = 'none';
        return;
    }

    paginationDiv.style.display = 'flex';

    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage === totalPages - 1;

    pageInfo.textContent = `Sayfa ${currentPage + 1} / ${totalPages}`;
}

// Delete ingredient functions
function openDeleteModal(ingredientId) {
    deleteIngredientId = ingredientId;
    document.getElementById('deleteModal').style.display = 'flex';
}

function closeDeleteModal() {
    deleteIngredientId = null;
    document.getElementById('deleteModal').style.display = 'none';
}

async function confirmDelete() {
    if (!deleteIngredientId) return;

    try {
        showLoading(true);
        hideMessages();
        closeDeleteModal();

        const response = await fetch(`${API_BASE_URL}/${deleteIngredientId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        showSuccess('Malzeme başarıyla silindi');

        // Reload current page, or go back one page if current page is empty
        await loadIngredients(currentPage, currentFilter);

    } catch (error) {
        console.error('Error deleting ingredient:', error);
        showError('Malzeme silinirken bir hata oluştu: ' + error.message);
    } finally {
        showLoading(false);
        deleteIngredientId = null;
    }
}

// Refresh ingredients
function refreshIngredients() {
    loadIngredients(currentPage, currentFilter);
}

// UI Helper Functions
function showLoading(show) {
    const spinner = document.getElementById('loadingSpinner');
    spinner.style.display = show ? 'block' : 'none';
}

function showError(message) {
    const errorDiv = document.getElementById('errorMessage');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 5000);
}

function showSuccess(message) {
    const successDiv = document.getElementById('successMessage');
    successDiv.textContent = message;
    successDiv.style.display = 'block';

    // Auto-hide after 3 seconds
    setTimeout(() => {
        successDiv.style.display = 'none';
    }, 3000);
}

function hideMessages() {
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';
}

// Utility Functions
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('tr-TR');
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('deleteModal');
    if (event.target === modal) {
        closeDeleteModal();
    }
}
