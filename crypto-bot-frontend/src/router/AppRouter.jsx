import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import MarketOverview from '../pages/Market/MarketOverview';
import StandardOrders from '../pages/Orders/StandardOrders';
import OcoOrders from '../pages/Orders/OcoOrders';
import StrategyManager from '../pages/Strategies/StrategyManager';
import StrategyRunner from '../pages/Strategies/StrategyRunner';
import AccountInfo from '../pages/Account/AccountInfo';
import Home from '../pages/Dashboard/Home';

const AppRouter = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/market" element={<MarketOverview />} />
      <Route path="/orders/standard" element={<StandardOrders />} />
      <Route path="/orders/oco" element={<OcoOrders />} />
      <Route path="/strategies" element={<StrategyManager />} />
      <Route path="/strategies/running" element={<StrategyRunner />} />
      <Route path="/account" element={<AccountInfo />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default AppRouter;
