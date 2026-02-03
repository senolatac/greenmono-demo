import React, { useState, useEffect } from 'react';
import menuService from '../services/menuService';
import './WeeklyMenuTable.css';

const WeeklyMenuTable = () => {
  const [menuData, setMenuData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const userId = 1; // Default user ID for demo purposes

  // Group menu items by day
  const groupByDay = (items) => {
    const grouped = {};
    items.forEach(item => {
      if (!grouped[item.day]) {
        grouped[item.day] = {
          day: item.day,
          date: item.date,
          meals: []
        };
      }
      grouped[item.day].meals.push(item.meal);
    });
    return Object.values(grouped);
  };

  // Order days from Monday to Friday
  const orderDays = (groupedData) => {
    const dayOrder = ['Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma'];
    return groupedData.sort((a, b) => {
      return dayOrder.indexOf(a.day) - dayOrder.indexOf(b.day);
    });
  };

  // Fetch current menu on component mount
  useEffect(() => {
    fetchCurrentMenu();
  }, []);

  const fetchCurrentMenu = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await menuService.getCurrentMenu(userId);
      setMenuData(data);
    } catch (err) {
      console.error('Error loading menu:', err);
      setError('Menü yüklenirken bir hata oluştu. Lütfen yeni bir menü oluşturun.');
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateMenu = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await menuService.generateMenu(userId);
      setMenuData(data);
    } catch (err) {
      console.error('Error generating menu:', err);
      if (err.response && err.response.status === 404) {
        setError('Yeterli malzeme bulunamadı. Lütfen dolabınıza malzeme ekleyin.');
      } else if (err.response && err.response.status === 400) {
        setError('Menü oluşturulamadı. Yeterli tarif bulunamadı.');
      } else {
        setError('Menü oluştururken bir hata oluştu. Lütfen tekrar deneyin.');
      }
    } finally {
      setLoading(false);
    }
  };

  // Prepare data for table display
  const groupedData = menuData.length > 0 ? orderDays(groupByDay(menuData)) : [];

  return (
    <div className="weekly-menu-container">
      <div className="header">
        <h1>Haftalık Menü Planı</h1>
        <button
          className="generate-button"
          onClick={handleGenerateMenu}
          disabled={loading}
        >
          {loading ? 'Oluşturuluyor...' : 'Yeni Menü Oluştur'}
        </button>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {loading && menuData.length === 0 ? (
        <div className="loading">Yükleniyor...</div>
      ) : groupedData.length === 0 && !error ? (
        <div className="empty-state">
          <p>Henüz bir menü planınız yok.</p>
          <p>Yeni bir menü oluşturmak için yukarıdaki butona tıklayın.</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="menu-table">
            <thead>
              <tr>
                {groupedData.map((dayData) => (
                  <th key={dayData.day}>
                    <div className="day-header">
                      <div className="day-name">{dayData.day}</div>
                      <div className="day-date">{dayData.date}</div>
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              <tr>
                {groupedData.map((dayData) => (
                  <td key={dayData.day}>
                    <div className="meal-list">
                      {dayData.meals.map((meal, index) => (
                        <div key={index} className="meal-item">
                          {meal}
                        </div>
                      ))}
                    </div>
                  </td>
                ))}
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default WeeklyMenuTable;
