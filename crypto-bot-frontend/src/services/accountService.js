import api from './api';

export const getAccountInfo = async () => {
  const response = await api.get('/account/info');
  return response.data;
};

export const getAccountTrades = async (symbol, limit = 50) => {
  const response = await api.get(`/account/trades?symbol=${symbol}&limit=${limit}`);
  return response.data;
};

export const getUserInfo = async () => {
  const response = await api.get('/auth/me');
  return response.data;
};

export const updateUserInfo = async (userData) => {
  const response = await api.put('/auth/update', userData);
  return response.data;
};