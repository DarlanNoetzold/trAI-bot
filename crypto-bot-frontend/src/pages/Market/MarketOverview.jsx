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
`;

const LastPrice = styled.div`
  font-size: 1.2rem;
  color: #c5efb4;

  strong {
    color: #7fbb5e;
  }
`;

const BookWrapper = styled.div`
  background: #1b2d1b;
  border: 1px solid #3f5f3f;
  border-radius: 8px;
  padding: 1rem;
  font-size: 0.85rem;
  color: #cfe9c8;
  max-height: 400px;
  overflow-y: auto;

  table {
    width: 100%;
    border-collapse: collapse;

    th, td {
      padding: 0.5rem;
      border-bottom: 1px solid #2c3c2c;
    }

    th {
      color: #9ccc9c;
      text-align: left;
    }
  }
`;

const TradeTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  background-color: #1a261a;
  color: #d8f2d0;
  font-size: 0.85rem;
  border-radius: 8px;
  overflow: hidden;

  th, td {
    padding: 0.5rem;
    border-bottom: 1px solid #2f3f2f;
    text-align: left;
  }

  th {
    background-color: #233423;
    color: #a5e6a5;
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
              <option key={sym} value={sym}>{sym}</option>
            ))}
          </select>
        </div>

        <div>
          <label>Intervalo:</label>
          <select value={interval} onChange={(e) => setInterval(e.target.value)}>
            {['1m', '5m', '15m', '1h', '4h', '1d'].map((opt) => (
              <option key={opt} value={opt}>{opt}</option>
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
          <BookWrapper>
            <table>
              <thead>
                <tr>
                  <th>Preço</th>
                  <th>Quantidade</th>
                </tr>
              </thead>
              <tbody>
                {depth?.bids?.slice(0, 10).map((bid, idx) => (
                  <tr key={`bid-${idx}`}>
                    <td>{bid[0]}</td>
                    <td>{bid[1]}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </BookWrapper>
        </Section>

        <Section>
          <h3>Últimos Trades</h3>
          <BookWrapper>
            <TradeTable>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Preço</th>
                  <th>Qtd</th>
                  <th>Timestamp</th>
                </tr>
              </thead>
              <tbody>
                {trades?.map((t) => (
                  <tr key={t.id}>
                    <td>{t.id}</td>
                    <td>{t.price}</td>
                    <td>{t.qty}</td>
                    <td>{new Date(t.time).toLocaleTimeString()}</td>
                  </tr>
                ))}
              </tbody>
            </TradeTable>
          </BookWrapper>
        </Section>
      </div>

      <Section>
        <h3>Book Ticker</h3>
        <BookWrapper>
          <p><strong>Ask Price:</strong> {bookTicker?.askPrice}</p>
          <p><strong>Ask Qty:</strong> {bookTicker?.askQty}</p>
          <p><strong>Bid Price:</strong> {bookTicker?.bidPrice}</p>
          <p><strong>Bid Qty:</strong> {bookTicker?.bidQty}</p>
        </BookWrapper>
      </Section>
    </Container>
  );
};

export default MarketOverview;
