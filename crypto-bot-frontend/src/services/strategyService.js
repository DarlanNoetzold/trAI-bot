import api from './api'; // importa o axios com interceptors

export const listStrategies = async () => {
  const response = await api.get('/strategies/list');
  return response.data;
};

export const runStrategy = async (name, params) => {
  const response = await api.post(`/strategies/run/${name}`, params);
  return response.data;
};

export const runStrategyOnce = async (name, params) => {
  const response = await api.post(`/strategies/run-once/${name}`, params);
  return response.data;
};

export const stopStrategy = async (name) => {
  const response = await api.post(`/strategies/stop/${name}`);
  return response.data;
};

export const runCustomStrategy = async (code) => {
  const response = await api.post('/custom/strategy', { code });
  return response.data;
};

export const fetchStrategyLogs = async (strategyName, page = 0, size = 20) => {
  const response = await api.get(`/logs`, {
    params: {
      strategyName: strategyName,
      page,
      size
    }
  });
  return response.data;
};
