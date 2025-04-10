// Tela de mercado 
import React, { useEffect, useState } from 'react';
import CandlestickChart from 'components/charts/CandlestickChart';
import DataTable from 'components/tables/DataTable';
import Loader from 'components/common/Loader';
import { getLastPrice, getCandles, getDepth, getRecentTrades, getBookTicker } from 'services/marketService';

const MarketOverview = () => {
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [interval, setInterval] = useState('1m');
  const [candles, setCandles] = useState([]);
  const [price, setPrice] = useState(null);
  const [depth, setDepth] = useState(null);
  const [trades, setTrades] = useState([]);
  const [bookTicker, setBookTicker] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMarketData = async () => {
      setLoading(true);
      try {
        const [priceData, candleData, depthData, tradesData, bookData] = await Promise.all([
          getLastPrice(symbol),
          getCandles(symbol, interval, 50),
          getDepth(symbol, 10),
          getRecentTrades(symbol, 20),
          getBookTicker(symbol),
        ]);
        setPrice(priceData?.price);
        setCandles(candleData);
        setDepth(depthData);
        setTrades(tradesData);
        setBookTicker(bookData);
      } catch (error) {
        console.error('Erro ao buscar dados de mercado:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMarketData();
  }, [symbol, interval]);

  if (loading) return <Loader />;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold">{symbol} Market Overview</h2>
        <div>
          <label className="mr-2">Intervalo:</label>
          <select
            className="border px-2 py-1 rounded"
            value={interval}
            onChange={e => setInterval(e.target.value)}
          >
            {['1m', '5m', '15m', '1h', '4h', '1d'].map(opt => (
              <option key={opt} value={opt}>{opt}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="text-lg">
        <strong>Último preço:</strong> ${price}
      </div>

      <CandlestickChart data={candles} />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <h3 className="font-semibold mb-2">Livro de Ordens</h3>
          <pre className="bg-white p-4 rounded shadow text-sm overflow-auto">
            {JSON.stringify(depth, null, 2)}
          </pre>
        </div>

        <div>
          <h3 className="font-semibold mb-2">Últimos Trades</h3>
          <DataTable data={trades} columns={['id', 'price', 'qty', 'time']} />
        </div>
      </div>

      <div>
        <h3 className="font-semibold mb-2">Book Ticker</h3>
        <pre className="bg-white p-4 rounded shadow text-sm overflow-auto">
          {JSON.stringify(bookTicker, null, 2)}
        </pre>
      </div>
    </div>
  );
};

export default MarketOverview;
