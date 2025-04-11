import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import {
  listStrategies,
  runStrategy,
  stopStrategy,
} from '../../services/strategyService';

const Container = styled.div`
  background-color: #0e150e;
  color: #d0e6c5;
  font-family: 'Courier New', monospace;
  padding: 2rem;
`;

const Title = styled.h2`
  font-size: 1.6rem;
  color: #b6efb6;
  margin-bottom: 1.5rem;
`;

const Select = styled.select`
  background-color: #1c2b1c;
  border: 1px solid #3f5f3f;
  color: #c7ffc7;
  padding: 0.6rem;
  border-radius: 4px;
  width: 100%;
`;

const Input = styled.input`
  background-color: #1c2b1c;
  border: 1px solid #3f5f3f;
  color: #e4ffe4;
  padding: 0.6rem;
  border-radius: 4px;
  width: 100%;
`;

const Button = styled.button`
  background-color: ${(props) => (props.variant === 'stop' ? '#b74d4d' : '#4d6b3c')};
  color: #fff;
  padding: 0.5rem 1.2rem;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
  margin-right: 1rem;

  &:hover {
    background-color: ${(props) => (props.variant === 'stop' ? '#d65d5d' : '#5c8e4e')};
  }
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
  margin-bottom: 1.5rem;

  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
`;

const Output = styled.pre`
  background: #1a261a;
  border: 1px solid #375937;
  padding: 1rem;
  color: #cdeacc;
  border-radius: 6px;
  font-size: 0.85rem;
  height: 300px;
  overflow-y: auto;
`;

const StrategyRunner = () => {
  const [strategies, setStrategies] = useState([]);
  const [selectedStrategy, setSelectedStrategy] = useState('');
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [interval, setInterval] = useState('1m');
  const [logs, setLogs] = useState('');

  useEffect(() => {
    const fetchStrategies = async () => {
      try {
        const list = await listStrategies();
        setStrategies(list);
      } catch (err) {
        console.error('Erro ao carregar estratégias:', err);
      }
    };

    fetchStrategies();
  }, []);

  const handleRunBot = async () => {
    try {
      const res = await runStrategy(selectedStrategy, { symbol, interval });
      setLogs(res);
    } catch (err) {
      setLogs(`Erro ao rodar: ${err.message}`);
    }
  };

  const handleStopBot = async () => {
    try {
      const res = await stopStrategy(selectedStrategy);
      setLogs(res);
    } catch (err) {
      setLogs(`Erro ao parar: ${err.message}`);
    }
  };

  return (
    <Container>
      <Title>Monitoramento de Estratégias</Title>

      <Grid>
        <Select
          value={selectedStrategy}
          onChange={(e) => setSelectedStrategy(e.target.value)}
        >
          <option value="">Selecione uma estratégia</option>
          {strategies.map((strategy) => (
            <option key={strategy} value={strategy}>
              {strategy}
            </option>
          ))}
        </Select>

        <Input
          type="text"
          placeholder="Símbolo (ex: BTCUSDT)"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
        />

        <Input
          type="text"
          placeholder="Intervalo (ex: 1m)"
          value={interval}
          onChange={(e) => setInterval(e.target.value)}
        />
      </Grid>

      <div style={{ marginBottom: '1.5rem' }}>
        <Button onClick={handleRunBot}>Iniciar</Button>
        <Button variant="stop" onClick={handleStopBot}>
          Parar
        </Button>
      </div>

      <Output>{logs}</Output>
    </Container>
  );
};

export default StrategyRunner;
