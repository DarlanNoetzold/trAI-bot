// Ordens OCO 
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import OrderForm from '../../components/forms/OrderForm';
import DataTable from '../../components/tables/DataTable';

const OcoOrders = () => {
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [orders, setOrders] = useState([]);
  const [history, setHistory] = useState([]);
  const [formData, setFormData] = useState({
    quantity: '',
    price: '',
    stopPrice: '',
    stopLimitPrice: '',
    timeInForce: 'GTC'
  });

  const fetchOpenOrders = async () => {
    try {
      const res = await axios.get(`/api/order/open?symbol=${symbol}`);
      setOrders(res.data);
    } catch (error) {
      console.error('Erro ao buscar ordens OCO abertas', error);
    }
  };

  const fetchOrderHistory = async () => {
    try {
      const res = await axios.get(`/api/order/history?symbol=${symbol}`);
      setHistory(res.data);
    } catch (error) {
      console.error('Erro ao buscar histórico de ordens OCO', error);
    }
  };

  const handleSubmit = async () => {
    try {
      const res = await axios.post(`/api/order/place-oco`, {
        symbol,
        side: 'SELL',
        ...formData
      });
      console.log(res.data);
      fetchOpenOrders();
    } catch (error) {
      console.error('Erro ao criar ordem OCO', error);
    }
  };

  const cancelOrder = async (orderId, clientOrderId) => {
    try {
      const res = await axios.delete(`/api/order/cancel-oco?symbol=${symbol}&orderListId=${orderId}&listClientOrderId=${clientOrderId}`);
      console.log(res.data);
      fetchOpenOrders();
    } catch (error) {
      console.error('Erro ao cancelar ordem OCO', error);
    }
  };

  useEffect(() => {
    fetchOpenOrders();
    fetchOrderHistory();
  }, [symbol]);

  return (
    <div className="space-y-6">
      <div>
        <label className="block font-medium">Símbolo:</label>
        <input
          className="border p-2 rounded w-48"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
        />
      </div>

      <OrderForm
        formData={formData}
        setFormData={setFormData}
        onSubmit={handleSubmit}
        fields={['quantity', 'price', 'stopPrice', 'stopLimitPrice', 'timeInForce']}
      />

      <h2 className="text-xl font-semibold">Ordens OCO Abertas</h2>
      <DataTable
        data={orders}
        onRowAction={(order) => cancelOrder(order.orderListId, order.listClientOrderId)}
        actionLabel="Cancelar"
      />

      <h2 className="text-xl font-semibold">Histórico de Ordens OCO</h2>
      <DataTable data={history} />
    </div>
  );
};

export default OcoOrders;
