// Dashboard geral 
import React, { useEffect, useState } from 'react';
import { getPrice } from '../../services/marketService';
import { getAccountInfo } from '../../services/accountService';
import { getOpenOrders } from '../../services/orderService';
import { listStrategies } from '../../services/strategyService';

const favoriteSymbols = ['BTCUSDT', 'ETHUSDT', 'BNBUSDT'];

const Home = () => {
  const [prices, setPrices] = useState({});
  const [account, setAccount] = useState(null);
  const [openOrders, setOpenOrders] = useState([]);
  const [strategies, setStrategies] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const priceResults = await Promise.all(
        favoriteSymbols.map(async (symbol) => {
          const data = await getPrice(symbol);
          return [symbol, data?.price];
        })
      );
      setPrices(Object.fromEntries(priceResults));

      const accountInfo = await getAccountInfo();
      setAccount(accountInfo);

      const orders = await getOpenOrders('BTCUSDT'); // Exemplo
      setOpenOrders(orders);

      const strategyList = await listStrategies();
      setStrategies(strategyList);
    };

    fetchData();
  }, []);

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">Dashboard</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
        {favoriteSymbols.map((symbol) => (
          <div
            key={symbol}
            className="bg-white rounded-xl shadow p-4 flex flex-col items-center"
          >
            <span className="text-sm text-gray-500">{symbol}</span>
            <span className="text-lg font-bold">
              ${parseFloat(prices[symbol] || 0).toFixed(2)}
            </span>
          </div>
        ))}
      </div>

      <div className="mt-8">
        <h3 className="text-lg font-semibold mb-2">Estratégias Disponíveis</h3>
        <ul className="list-disc list-inside">
          {strategies.map((s) => (
            <li key={s}>{s}</li>
          ))}
        </ul>
      </div>

      <div className="mt-8">
        <h3 className="text-lg font-semibold mb-2">Ordens Abertas</h3>
        <ul className="text-sm text-gray-700">
          {openOrders.map((order) => (
            <li key={order.orderId}>
              {order.symbol} - {order.side} - {order.origQty} @ {order.price}
            </li>
          ))}
        </ul>
      </div>

      <div className="mt-8">
        <h3 className="text-lg font-semibold mb-2">Saldo da Conta</h3>
        {account?.balances?.length > 0 && (
          <ul className="text-sm">
            {account.balances
              .filter((b) => parseFloat(b.free) > 0)
              .map((b) => (
                <li key={b.asset}>
                  {b.asset}: {b.free}
                </li>
              ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Home;
