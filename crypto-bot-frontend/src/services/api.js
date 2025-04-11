// axios.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Middleware para adicionar o header com o ambiente (TESTNET ou PRODUCTION)
api.interceptors.request.use((config) => {
  const env = localStorage.getItem('binanceEnv') || 'TESTNET'; // ou outra estratégia
  config.headers['X-BINANCE-ENV'] = env;
  return config;
});

export default api;
