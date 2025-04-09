// Ordens padrão 
import React, { useEffect, useState } from 'react';
import marketService from '../../services/marketService';
import orderService from '../../services/orderService';
import OrderForm from '../../components/forms/OrderForm';
import DataTable from '../../components/tables/DataTable';

function StandardOrders() {
  const [openOrders, setOpenOrders] = useState([]);
  const [orderHistory, setOrderHistory] = useState([]);
  const [symbol, setSymbol] = useState('BTCUSDT');

  const fetchOrders = async () => {
    try {
      const open = await orderService.getOpenOrders(symbol);
      const history = await orderService.getOrderHistory(symbol);
      setOpenOrders(open);
      setOrderHistory(history);
    } catch (err) {
      console.error('Erro ao carregar ordens:', err);
    }
  };

  const handleCancelOrder = async (orderId) => {
    try {
      await orderService.cancelOrder(symbol, orderId);
      fetchOrders();
    } catch (err) {
      console.error('Erro ao cancelar ordem:', err);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [symbol]);

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Ordens Comuns</h2>

      <div className="mb-6">
        <label htmlFor="symbol">Símbolo:</label>
        <input
          type="text"
          id="symbol"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
          className="border p-2 rounded ml-2"
        />
      </div>

      <OrderForm symbol={symbol} onOrderPlaced={fetchOrders} />

      <div className="mt-6">
        <h3 className="text-lg font-semibold mb-2">Ordens Abertas</h3>
        <DataTable
          data={openOrders}
          columns={['orderId', 'symbol', 'price', 'origQty', 'status', 'side']}
          onAction={handleCancelOrder}
          actionLabel="Cancelar"
        />
      </div>

      <div className="mt-6">
        <h3 className="text-lg font-semibold mb-2">Histórico de Ordens</h3>
        <DataTable
          data={orderHistory}
          columns={['orderId', 'symbol', 'price', 'origQty', 'status', 'side']}
        />
      </div>
    </div>
  );
}

export default StandardOrders;
