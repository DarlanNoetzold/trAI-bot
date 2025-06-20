import api from './api';

// Account info
export const getFuturesAccountInfo = async () => {
  const res = await api.get('/futures/account/info');
  return res.data;
};

// Balances
export const getFuturesBalance = async (asset) => {
  const res = await api.get(`/futures/account/balance?asset=${asset}`);
  return res.data;
};

// Open positions
export const getFuturesPositions = async () => {
  const res = await api.get('/futures/account/positions');
  return res.data;
};

// Create order
export const createFuturesOrder = async (order) => {
  const res = await api.post('/futures/trade/order', order);
  return res.data;
};

// Cancel order
export const cancelFuturesOrder = async (symbol, orderId) => {
  const res = await api.delete(`/futures/trade/order?symbol=${symbol}&orderId=${orderId}`);
  return res.data;
};