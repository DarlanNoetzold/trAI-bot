import api from './api';

export const placeOrder = async (orderData) => {
  const response = await api.post('/trade/order', orderData);
  return response.data;
};

export const placeOcoOrder = async (ocoOrderData) => {
  const response = await api.post('/trade/order/oco', ocoOrderData);
  return response.data;
};

export const cancelOrder = async (symbol, orderId = null, origClientOrderId = null) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (orderId) params.append('orderId', orderId);
  if (origClientOrderId) params.append('origClientOrderId', origClientOrderId);

  const response = await api.delete(`/trade/order?${params.toString()}`);
  return response.data;
};

export const cancelOcoOrder = async (symbol, orderListId = null, listClientOrderId = null) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (orderListId) params.append('orderListId', orderListId);
  if (listClientOrderId) params.append('listClientOrderId', listClientOrderId);

  const response = await api.delete(`/trade/order/oco?${params.toString()}`);
  return response.data;
};

export const getOpenOrders = async (symbol) => {
  const response = await api.get(`/trade/orders/open?symbol=${symbol}`);
  return response.data;
};

export const getOrderHistory = async (symbol, startTime = null, endTime = null) => {
  const params = new URLSearchParams();
  params.append('symbol', symbol);
  if (startTime) params.append('startTime', startTime);
  if (endTime) params.append('endTime', endTime);

  const response = await api.get(`/trade/orders/all?${params.toString()}`);
  return response.data;
};
