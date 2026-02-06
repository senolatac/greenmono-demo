import React from 'react';
import { ThemeProvider } from './context/ThemeContext';
import WeeklyMenuTable from './components/WeeklyMenuTable';
import ThemeToggle from './components/ThemeToggle';
import './App.css';

function App() {
  return (
    <ThemeProvider>
      <div className="App">
        <ThemeToggle />
        <WeeklyMenuTable />
      </div>
    </ThemeProvider>
  );
}

export default App;
