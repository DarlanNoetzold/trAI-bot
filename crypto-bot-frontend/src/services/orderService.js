// API de ordens 
import api from './api';

export const placeOrder = async (orderData) => {
  const response = await api.post('/order/place', orderData);
  return response.data;
};

export const placeOcoOrder = async (ocoOrderData) => {
  const response = await api.post('/order/place-oco', ocoOrderData);
  return response.data;
};

export const cancelOrder = async ({ symbol, orderId, origClientOrderId }) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (orderId) params.append('orderId', orderId);
  if (origClientOrderId) params.append('origClientOrderId', origClientOrderId);

  const response = await api.delete(`/order/cancel?${params.toString()}`);
  return response.data;
};

export const cancelOcoOrder = async ({ symbol, orderListId, listClientOrderId }) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (orderListId) params.append('orderListId', orderListId);
  if (listClientOrderId) params.append('listClientOrderId', listClientOrderId);

  const response = await api.delete(`/order/cancel-oco?${params.toString()}`);
  return response.data;
};

export const getOpenOrders = async (symbol) => {
  const response = await api.get(`/order/open?symbol=${symbol}`);
  return response.data;
};

export const getOrderHistory = async (symbol, startTime = null, endTime = null) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (startTime) params.append('startTime', startTime);
  if (endTime) params.append('endTime', endTime);

  const response = await api.get(`/order/history?${params.toString()}`);
  return response.data;
};
