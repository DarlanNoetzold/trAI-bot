// API de estratégias 
import api from './api';

export const listStrategies = async () => {
  const response = await api.get('/strategies/list');
  return response.data;
};

export const runStrategyOnce = async (strategyName, params = {}) => {
  const response = await api.post(`/strategies/run-once/${strategyName}`, params);
  return response.data;
};

export const runStrategy = async (strategyName, params = {}) => {
  const response = await api.post(`/strategies/run/${strategyName}`, params);
  return response.data;
};

export const stopStrategy = async (strategyName) => {
  const response = await api.post(`/strategies/stop/${strategyName}`);
  return response.data;
};

export const runCustomStrategy = async (pythonCode) => {
  const response = await api.post('/strategies/custom', pythonCode, {
    headers: {
      'Content-Type': 'text/plain',
    },
  });
  return response.data;
};
