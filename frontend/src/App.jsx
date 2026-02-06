import React, { useState } from 'react';
import WeeklyMenuTable from './components/WeeklyMenuTable';
import IngredientManager from './components/IngredientManager';
import ThemeToggle from './components/ThemeToggle';
import './App.css';

const TABS = [
  { key: 'ingredients', label: 'Malzemeler' },
  { key: 'menu', label: 'Haftalık Menü' }
];

function App() {
  const [activeTab, setActiveTab] = useState('ingredients');

  return (
    <div className="App">
      <ThemeToggle />
      <header className="app-header">
        <h1>Yemek Planlayıcı</h1>
        <p className="app-subtitle">Malzemelerinizi yönetin ve haftalık menünüzü oluşturun</p>
      </header>

      <nav className="app-tabs">
        {TABS.map((tab) => (
          <button
            key={tab.key}
            className={`app-tab ${activeTab === tab.key ? 'app-tab-active' : ''}`}
            onClick={() => setActiveTab(tab.key)}
          >
            {tab.label}
          </button>
        ))}
      </nav>

      <main className="app-content">
        {activeTab === 'ingredients' && <IngredientManager />}
        {activeTab === 'menu' && <WeeklyMenuTable />}
      </main>
    </div>
  );
}

export default App;
