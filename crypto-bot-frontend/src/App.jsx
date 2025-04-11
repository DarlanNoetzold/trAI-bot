import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRouter from './router/AppRouter';
import Sidebar from './components/common/Sidebar';

function App() {
  return (
    <Router>
      <div style={{ display: 'flex', background: '#121b12' }}>
        <Sidebar />
        <main style={{ flex: 1, padding: '1.5rem', overflowY: 'auto' }}>
          <AppRouter />
        </main>
      </div>
    </Router>
  );
}

export default App;
