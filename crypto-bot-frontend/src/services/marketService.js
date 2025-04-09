// API de mercado 
import api from './api';

export const getPrice = async (symbol) => {
  const response = await api.get(`/market/price?symbol=${symbol}`);
  return response.data;
};

export const getCandles = async (symbol, interval = '1m', limit = 50) => {
  const response = await api.get(`/market/candles?symbol=${symbol}&interval=${interval}&limit=${limit}`);
  return response.data;
};

export const getDepth = async (symbol, limit = 10) => {
  const response = await api.get(`/market/depth?symbol=${symbol}&limit=${limit}`);
  return response.data;
};

export const getTrades = async (symbol, limit = 10) => {
  const response = await api.get(`/market/trades?symbol=${symbol}&limit=${limit}`);
  return response.data;
};

export const getBookTicker = async (symbol) => {
  const response = await api.get(`/market/bookTicker?symbol=${symbol}`);
  return response.data;
};
