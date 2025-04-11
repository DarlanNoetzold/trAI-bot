// API de conta 
import api from './api';

export const getAccountInfo = async () => {
  const response = await api.get('/account/info');
  return response.data;
};

export const getAccountTrades = async (symbol, limit = 50) => {
  const response = await api.get(`/account/trades?symbol=${symbol}&limit=${limit}`);
  return response.data;
};
