import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import MarketOverview from '../pages/Market/MarketOverview';
import StandardOrders from '../pages/Orders/StandardOrders';
import OcoOrders from '../pages/Orders/OcoOrders';
import StrategyManager from '../pages/Strategies/StrategyManager';
import StrategyRunner from '../pages/Strategies/StrategyRunner';
import SchedulerPage from '../pages/Strategies/SchedulerPage';
import AccountInfo from '../pages/Account/AccountInfo';
import Home from '../pages/Dashboard/Home';
import AuthPage from '../pages/Auth/AuthPage';
import CreateCustomStrategy from '../pages/Strategies/CreateCustomStrategy';
import LandingPage from '../pages/Landing/LandingPage';


import { useAuth } from '../contexts/AuthContext';

const PrivateRoute = ({ children }) => {
  const { user } = useAuth();
  return user ? children : <Navigate to="/auth" replace />;
};

const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/auth" element={<AuthPage />} />
      <Route path="/landing" element={<LandingPage />} />
      <Route
        path="/market"
        element={
          <PrivateRoute>
            <MarketOverview />
          </PrivateRoute>
        }
      />
      <Route
        path="/orders/standard"
        element={
          <PrivateRoute>
            <StandardOrders />
          </PrivateRoute>
        }
      />
      <Route
        path="/orders/oco"
        element={
          <PrivateRoute>
            <OcoOrders />
          </PrivateRoute>
        }
      />
      <Route
        path="/strategies"
        element={
          <PrivateRoute>
            <StrategyManager />
          </PrivateRoute>
        }
      />
      <Route
        path="/strategies/create"
        element={
          <PrivateRoute>
            <CreateCustomStrategy />
          </PrivateRoute>
        }
      />
      <Route
        path="/strategies/running"
        element={
          <PrivateRoute>
            <StrategyRunner />
          </PrivateRoute>
        }
      />
      <Route path="/scheduler" element={<SchedulerPage />} />
      <Route
        path="/account"
        element={
          <PrivateRoute>
            <AccountInfo />
          </PrivateRoute>
        }
      />

      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default AppRouter;
