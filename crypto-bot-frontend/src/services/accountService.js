// API de conta 
import api from './api';

export const getAccountInfo = async () => {
  const response = await api.get('/account/info');
  return response.data;
};

export const getAccountStatus = async () => {
  const response = await api.get('/account/status');
  return response.data;
};
