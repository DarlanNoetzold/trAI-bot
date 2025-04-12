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

api.interceptors.request.use((config) => {
  const env = localStorage.getItem('binanceEnv') || 'TESTNET';
  const user = JSON.parse(localStorage.getItem('auth_user'));

  if (user?.token) {
    config.headers['Authorization'] = `Bearer ${user.token}`;
  }

  config.headers['X-BINANCE-ENV'] = env;
  return config;
});


export default api;
