import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getAccountInfo, getAccountTrades } from '../../services/accountService';
import Loader from '../../components/common/Loader';

const Container = styled.div`
  background-color: #0e150e;
  color: #d0e6c5;
  font-family: 'Courier New', monospace;
  padding: 2rem;
`;

const Title = styled.h2`
  font-size: 1.6rem;
  color: #b6efb6;
  margin-bottom: 2rem;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 0.5rem;
  color: #b9e6b9;
`;

const Input = styled.input`
  background-color: #1c2b1c;
  border: 1px solid #3f5f3f;
  color: #c7ffc7;
  padding: 0.6rem;
  border-radius: 4px;
  width: 200px;
  margin-bottom: 2rem;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  background-color: #1a261a;
  color: #d8f2d0;
  margin-bottom: 2rem;
`;

const Th = styled.th`
  border: 1px solid #4a754a;
  padding: 0.75rem;
  background-color: #233423;
`;

const Td = styled.td`
  border: 1px solid #3f5f3f;
  padding: 0.6rem;
  text-align: left;
`;

const SectionTitle = styled.h3`
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: #b6efb6;
`;

function AccountInfo() {
  const [info, setInfo] = useState(null);
  const [trades, setTrades] = useState([]);
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchAccount() {
      setLoading(true);
      try {
        const [infoRes, tradesRes] = await Promise.all([
          getAccountInfo(),
          getAccountTrades(symbol, 50),
        ]);
        setInfo(infoRes);
        setTrades(tradesRes);
      } catch (error) {
        console.error('Erro ao buscar informações da conta:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchAccount();
  }, [symbol]);

  if (loading) return <Loader />;

  return (
    <Container>
      <Title>Informações da Conta</Title>

      <div>
        <Label>Símbolo:</Label>
        <Input
          type="text"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
        />
      </div>

      {info && (
        <div>
          <SectionTitle>Saldos:</SectionTitle>
          <Table>
            <thead>
              <tr>
                <Th>Moeda</Th>
                <Th>Disponível</Th>
                <Th>Em Ordem</Th>
              </tr>
            </thead>
            <tbody>
              {info.balances
                .filter(
                  (b) => parseFloat(b.free) > 0 || parseFloat(b.locked) > 0
                )
                .map((balance) => (
                  <tr key={balance.asset}>
                    <Td>{balance.asset}</Td>
                    <Td>{balance.free}</Td>
                    <Td>{balance.locked}</Td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </div>
      )}

      {trades && (
        <div>
          <SectionTitle>Últimos Trades ({symbol}):</SectionTitle>
          <Table>
            <thead>
              <tr>
                <Th>ID</Th>
                <Th>Preço</Th>
                <Th>Quantidade</Th>
                <Th>Timestamp</Th>
              </tr>
            </thead>
            <tbody>
              {trades.map((trade) => (
                <tr key={trade.id}>
                  <Td>{trade.id}</Td>
                  <Td>{trade.price}</Td>
                  <Td>{trade.qty}</Td>
                  <Td>{new Date(trade.time).toLocaleString()}</Td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </Container>
  );
}

export default AccountInfo;
