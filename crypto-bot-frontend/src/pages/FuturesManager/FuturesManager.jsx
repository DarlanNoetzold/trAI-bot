import React, { useEffect, useState } from "react";
import styled from "styled-components";
import {
  getFuturesAccountInfo,
  getFuturesPositions,
  createFuturesOrder,
  cancelFuturesOrder,
} from "../../services/futuresService";

const Container = styled.div`
  background: #101a10;
  color: #d0e6c5;
  padding: 2rem;
  min-height: 100vh;
  font-family: 'Courier New', monospace;
`;

const Title = styled.h2`
  color: #b6efb6;
  margin-bottom: 2rem;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  background: #1a261a;
  color: #d8f2d0;
  margin-bottom: 2rem;
`;

const Th = styled.th`
  border: 1px solid #4a754a;
  padding: 0.75rem;
  background: #233423;
`;

const Td = styled.td`
  border: 1px solid #3f5f3f;
  padding: 0.6rem;
`;

const Button = styled.button`
  background: #7fbb5e;
  color: #121b12;
  border: none;
  border-radius: 4px;
  padding: 0.4rem 1rem;
  cursor: pointer;
  margin-right: 0.5rem;
  font-family: inherit;
`;

export default function FuturesManager() {
  const [account, setAccount] = useState(null);
  const [positions, setPositions] = useState([]);
  const [order, setOrder] = useState({
    symbol: "BTCUSDT",
    side: "BUY",
    type: "MARKET",
    quantity: 0.001,
    price: "",
    timeInForce: "GTC",
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      setLoading(true);
      try {
        const [acc, pos] = await Promise.all([
          getFuturesAccountInfo(),
          getFuturesPositions(),
        ]);
        setAccount(acc);
        setPositions(pos);
      } catch (e) {
        alert("Erro ao buscar dados de futuros");
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  const handleOrderChange = (e) => {
    const { name, value } = e.target;
    setOrder((prev) => ({ ...prev, [name]: value }));
  };

  const handleOrderSubmit = async (e) => {
    e.preventDefault();
    try {
      await createFuturesOrder({
        ...order,
        quantity: parseFloat(order.quantity),
        price: order.price ? parseFloat(order.price) : undefined,
      });
      alert("Ordem enviada!");
    } catch {
      alert("Erro ao enviar ordem");
    }
  };

  const handleCancelOrder = async (symbol, orderId) => {
    try {
      await cancelFuturesOrder(symbol, orderId);
      alert("Ordem cancelada!");
    } catch {
      alert("Erro ao cancelar ordem");
    }
  };

  return (
    <Container>
      <Title>Gerenciamento de Futuros</Title>

      <h3>Nova Ordem</h3>
      <form onSubmit={handleOrderSubmit}>
        <label>
          Symbol:
          <input name="symbol" value={order.symbol} onChange={handleOrderChange} />
        </label>
        <label>
          Side:
          <select name="side" value={order.side} onChange={handleOrderChange}>
            <option value="BUY">BUY</option>
            <option value="SELL">SELL</option>
          </select>
        </label>
        <label>
          Type:
          <select name="type" value={order.type} onChange={handleOrderChange}>
            <option value="MARKET">MARKET</option>
            <option value="LIMIT">LIMIT</option>
          </select>
        </label>
        <label>
          Quantity:
          <input name="quantity" type="number" step="0.0001" value={order.quantity} onChange={handleOrderChange} />
        </label>
        {order.type === "LIMIT" && (
          <>
            <label>
              Price:
              <input name="price" type="number" step="0.01" value={order.price} onChange={handleOrderChange} />
            </label>
            <label>
              Time in Force:
              <select name="timeInForce" value={order.timeInForce} onChange={handleOrderChange}>
                <option value="GTC">GTC</option>
                <option value="IOC">IOC</option>
                <option value="FOK">FOK</option>
              </select>
            </label>
          </>
        )}
        <Button type="submit">Enviar Ordem</Button>
      </form>

      <h3>Posições Abertas</h3>
      <Table>
        <thead>
          <tr>
            <Th>Symbol</Th>
            <Th>Position Amt</Th>
            <Th>Entry Price</Th>
            <Th>Unrealized PnL</Th>
            <Th>Action</Th>
          </tr>
        </thead>
        <tbody>
          {positions && positions.length > 0 ? positions.map((pos, idx) => (
            <tr key={idx}>
              <Td>{pos.symbol}</Td>
              <Td>{pos.positionAmt}</Td>
              <Td>{pos.entryPrice}</Td>
              <Td>{pos.unRealizedProfit}</Td>
              <Td>
                {/* Exemplo: cancelar ordem aberta (se você tiver orderId) */}
                {/* <Button onClick={() => handleCancelOrder(pos.symbol, pos.orderId)}>Cancelar</Button> */}
              </Td>
            </tr>
          )) : (
            <tr>
              <Td colSpan={5}>Nenhuma posição aberta</Td>
            </tr>
          )}
        </tbody>
      </Table>

      <h3>Resumo da Conta</h3>
      {account && (
        <pre style={{ background: "#222", padding: "1rem", borderRadius: 8 }}>
          {JSON.stringify(account, null, 2)}
        </pre>
      )}
    </Container>
  );
}   