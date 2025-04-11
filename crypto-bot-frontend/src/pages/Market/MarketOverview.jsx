import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import CandlestickChart from '../../components/charts/CandlestickChart';
import DataTable from '../../components/tables/DataTable';
import Loader from '../../components/common/Loader';
import {
  getPrice,
  getCandles,
  getDepth,
  getTrades,
  getBookTicker,
} from '../../services/marketService';

const symbolsList = [
  'BTCUSDT', 'ETHUSDT', 'BNBUSDT', 'SOLUSDT', 'ADAUSDT',
  'XRPUSDT', 'DOGEUSDT', 'AVAXUSDT', 'DOTUSDT', 'MATICUSDT',
  'TRXUSDT', 'SHIBUSDT', 'LINKUSDT', 'LTCUSDT', 'ATOMUSDT',
  'ETCUSDT', 'FILUSDT', 'NEARUSDT', 'APTUSDT', 'ARBUSDT',
  'OPUSDT', 'VETUSDT', 'ALGOUSDT', 'EGLDUSDT', 'SANDUSDT',
];

const Container = styled.div`
  padding: 2rem;
  font-family: 'Courier New', monospace;
  background: #0e150e;
  color: #d3e6cc;
`;

const TitleRow = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 2rem;

  label {
    font-weight: bold;
    margin-right: 0.5rem;
  }

  select {
    background: #1f2a1f;
    color: #d3e6cc;
    border: 1px solid #4d6b3c;
    border-radius: 4px;
    padding: 0.4rem 0.8rem;
    font-size: 0.9rem;
    outline: none;
    transition: border 0.2s;

    &:hover {
      border-color: #7fbb5e;
    }
  }
`;

const Section = styled.div`
  margin-top: 2rem;

  h3 {
    color: #a5c8a2;
    margin-bottom: 0.5rem;
  }

  pre {
    background: #151d15;
    color: #cfe9c8;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: inset 0 0 10px #30463060;
    font-size: 0.85rem;
    overflow-x: auto;
  }
`;

const LastPrice = styled.div`
  font-size: 1.2rem;
  color: #c5efb4;

  strong {
    color: #7fbb5e;
  }
`;

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
          getPrice(symbol),
          getCandles(symbol, interval, 50),
          getDepth(symbol, 10),
          getTrades(symbol, 20),
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
    <Container>
      <TitleRow>
        <div>
          <label>Ativo:</label>
          <select value={symbol} onChange={(e) => setSymbol(e.target.value)}>
            {symbolsList.map((sym) => (
              <option key={sym} value={sym}>
                {sym}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>Intervalo:</label>
          <select value={interval} onChange={(e) => setInterval(e.target.value)}>
            {['1m', '5m', '15m', '1h', '4h', '1d'].map((opt) => (
              <option key={opt} value={opt}>
                {opt}
              </option>
            ))}
          </select>
        </div>
      </TitleRow>

      <LastPrice>
        <strong>Último preço:</strong> ${price}
      </LastPrice>

      <div style={{ marginTop: '2rem' }}>
        <CandlestickChart data={candles} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
        <Section>
          <h3>Livro de Ordens</h3>
          <pre>{JSON.stringify(depth, null, 2)}</pre>
        </Section>

        <Section>
          <h3>Últimos Trades</h3>
          <DataTable data={trades} columns={['id', 'price', 'qty', 'time']} />
        </Section>
      </div>

      <Section>
        <h3>Book Ticker</h3>
        <pre>{JSON.stringify(bookTicker, null, 2)}</pre>
      </Section>
    </Container>
  );
};

export default MarketOverview;
