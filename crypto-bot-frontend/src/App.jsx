import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRouter from './router/AppRouter';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-100 text-gray-800">
        <header className="bg-white shadow p-4 mb-6">
          <h1 className="text-2xl font-bold">Crypto Bot Dashboard</h1>
        </header>

        <main className="p-4">
          <AppRouter />
        </main>

        <footer className="text-center text-sm text-gray-500 mt-10 p-4 border-t">
          &copy; {new Date().getFullYear()} Crypto Bot. All rights reserved.
        </footer>
      </div>
    </Router>
  );
}

export default App;
