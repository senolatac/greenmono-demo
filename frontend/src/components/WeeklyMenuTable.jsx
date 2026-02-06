import React, { useState, useEffect } from 'react';
import menuService from '../services/menuService';
import './WeeklyMenuTable.css';

const DAY_ORDER = ['Pazartesi', 'Salı', 'Çarşamba', 'Perşembe', 'Cuma'];

const WeeklyMenuTable = () => {
  const [menuData, setMenuData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const userId = 1; // Default user ID for demo purposes

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

  // Sort menu items by day order (Mon-Fri)
  const sortedData = [...menuData].sort((a, b) => {
    return DAY_ORDER.indexOf(a.day) - DAY_ORDER.indexOf(b.day);
  });

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
      ) : sortedData.length === 0 && !error ? (
        <div className="empty-state">
          <p>Henüz bir menü planınız yok.</p>
          <p>Yeni bir menü oluşturmak için yukarıdaki butona tıklayın.</p>
        </div>
      ) : (
        <div className="table-wrapper">
          <table className="menu-table">
            <thead>
              <tr>
                <th className="row-header-cell"></th>
                {sortedData.map((item) => (
                  <th key={item.day}>
                    <div className="day-header">
                      <div className="day-name">{item.day}</div>
                      <div className="day-date">{item.date}</div>
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              <tr>
                <td className="row-header">Çorba</td>
                {sortedData.map((item) => (
                  <td key={`soup-${item.day}`}>
                    <div className="meal-item soup-item">
                      {item.soup || '-'}
                    </div>
                  </td>
                ))}
              </tr>
              <tr>
                <td className="row-header">Ana Yemek</td>
                {sortedData.map((item) => (
                  <td key={`main-${item.day}`}>
                    <div className="meal-item main-item">
                      {item.mainCourse || '-'}
                    </div>
                  </td>
                ))}
              </tr>
              <tr>
                <td className="row-header">Yan Yemek</td>
                {sortedData.map((item) => (
                  <td key={`side-${item.day}`}>
                    <div className="meal-item side-item">
                      {item.sideDish || '-'}
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
