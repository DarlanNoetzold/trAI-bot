import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import {
  listStrategies,
  runStrategyOnce,
  runStrategy,
  stopStrategy,
  runCustomStrategy,
} from '../../services/strategyService';

const Container = styled.div`
  font-family: 'Courier New', monospace;
  background: #0e150e;
  color: #d0e6c5;
  padding: 2rem;
`;

const Label = styled.label`
  font-weight: bold;
  display: block;
  margin-bottom: 0.4rem;
  color: #a8c4a4;
`;

const Select = styled.select`
  width: 100%;
  background: #1e2d1e;
  border: 1px solid #4d6b3c;
  color: #e1ffe1;
  padding: 0.6rem;
  border-radius: 4px;
  margin-bottom: 1.5rem;
`;

const Input = styled.input`
  background: #1e2d1e;
  border: 1px solid #4d6b3c;
  color: #d3ffcb;
  padding: 0.5rem;
  border-radius: 4px;
  width: 100%;
  margin-bottom: 1rem;
`;

const Button = styled.button`
  background: #4d6b3c;
  border: none;
  color: #f2fff2;
  padding: 0.5rem 1.2rem;
  margin-right: 1rem;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;

  &:hover {
    background: #628d50;
  }
`;

const TextArea = styled.textarea`
  background: #111b11;
  border: 1px solid #355232;
  color: #d7fcd7;
  width: 100%;
  padding: 1rem;
  height: 200px;
  border-radius: 6px;
  font-size: 0.95rem;
`;

const OutputBox = styled.pre`
  background: #1a261a;
  border: 1px solid #375937;
  padding: 1rem;
  color: #cdeacc;
  margin-top: 1rem;
  border-radius: 6px;
  font-size: 0.85rem;
`;

const Status = styled.div`
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  border-left: 4px solid;
  border-left-color: ${(props) => (props.$active ? '#8fd48f' : '#bd5d5d')};
  background: ${(props) => (props.$active ? '#1d2e1d' : '#2a1818')};
  color: ${(props) => (props.$active ? '#b7ffb7' : '#ffbaba')};
  font-size: 0.9rem;
  font-weight: bold;
`;

function StrategyManager() {
  const [strategies, setStrategies] = useState([]);
  const [selectedStrategy, setSelectedStrategy] = useState(localStorage.getItem('selected_strategy') || '');
  const [params, setParams] = useState({ symbol: 'BTCUSDT', interval: '1m' });
  const [customCode, setCustomCode] = useState(`# Exemplo de template Python\n\ndef main(symbol, interval):\n    # lógica da estratégia aqui\n    print(f"Rodando para {symbol} com intervalo {interval}")`);
  const [result, setResult] = useState('');
  const [isRunning, setIsRunning] = useState(localStorage.getItem('bot_running') === 'true');
  const [statusMessage, setStatusMessage] = useState('');
  const [liveLogs, setLiveLogs] = useState([]);
  const [ws, setWs] = useState(null);

  useEffect(() => {
    fetchStrategies();
  }, []);

  useEffect(() => {
    let interval;
    if (isRunning) {
      let dots = '';
      interval = setInterval(() => {
        dots = dots.length >= 3 ? '' : dots + '.';
        setStatusMessage(`Executando${dots}`);
      }, 1000);
    } else {
      setStatusMessage('');
    }
    return () => clearInterval(interval);
  }, [isRunning]);

  const fetchStrategies = async () => {
    try {
      const res = await listStrategies();
      setStrategies(res);
    } catch (err) {
      console.error('Erro ao carregar estratégias:', err);
      setResult('❌ Erro ao buscar estratégias');
    }
  };

  const handleRunOnce = async () => {
    try {
      const res = await runStrategyOnce(selectedStrategy, params);
      setResult(res);
    } catch (err) {
      console.error(err);
      setResult('❌ Erro ao executar uma vez');
    }
  };

  const handleRunBot = async () => {
    try {
      const res = await runStrategy(selectedStrategy, params);
      setResult(res);
      setIsRunning(true);
      localStorage.setItem('bot_running', 'true');
      localStorage.setItem('selected_strategy', selectedStrategy);
  
      const authUser = JSON.parse(localStorage.getItem('auth_user'));
      const token = authUser?.token;
  
      if (!token) {
        setResult('❌ Token não encontrado. Faça login novamente.');
        return;
      }
  
      const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
      const socket = new WebSocket(
        `${protocol}://localhost:8080/ws/strategy-logs?strategyName=${selectedStrategy}&token=${token}`
      );
  
      socket.onmessage = (event) => {
        setLiveLogs((prev) => [...prev.slice(-200), event.data]);
      };
  
      socket.onclose = () => {
        console.log('🔌 WebSocket fechado');
      };
  
      socket.onerror = (err) => {
        console.error('❌ WebSocket error', err);
      };
  
      setWs(socket);
    } catch (err) {
      console.error(err);
      setResult('❌ Erro ao iniciar o bot');
    }
  };
  

  const handleStopBot = async () => {
    try {
      const res = await stopStrategy(selectedStrategy);
      setResult(res);
      setIsRunning(false);
      localStorage.removeItem('bot_running');
      localStorage.removeItem('selected_strategy');
      if (ws) {
        ws.close();
        setWs(null);
      }
    } catch (err) {
      console.error(err);
      setResult('❌ Erro ao parar o bot');
    }
  };

  const handleRunCustom = async () => {
    try {
      const res = await runCustomStrategy(customCode);
      setResult(res);
    } catch (err) {
      console.error(err);
      setResult('❌ Erro ao executar código customizado');
    }
  };

  return (
    <Container>
      <h2 style={{ fontSize: '1.6rem', marginBottom: '1rem', color: '#9fe69f' }}>
        Gerenciador de Estratégias
      </h2>

      <div>
        <Label>Escolha uma estratégia:</Label>
        <Select value={selectedStrategy} onChange={(e) => setSelectedStrategy(e.target.value)}>
          <option value="">-- selecione --</option>
          {strategies.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </Select>
      </div>

      <div>
        <Label>Símbolo:</Label>
        <Input
          value={params.symbol}
          onChange={(e) => setParams({ ...params, symbol: e.target.value })}
        />

        <Label>Intervalo:</Label>
        <Input
          value={params.interval}
          onChange={(e) => setParams({ ...params, interval: e.target.value })}
        />
      </div>

      <div style={{ margin: '1.5rem 0' }}>
        <Button onClick={handleRunOnce}>Executar Uma Vez</Button>
        <Button onClick={handleRunBot}>Iniciar Bot</Button>
        <Button onClick={handleStopBot}>Parar Bot</Button>
      </div>

      <Status $active={isRunning} aria-live="polite" data-active={isRunning}>
        Bot {isRunning ? statusMessage : 'Parado'}
      </Status>

      <div style={{ marginTop: '2rem' }}>
        <Label>Código Python Customizado:</Label>
        <TextArea
          value={customCode}
          onChange={(e) => setCustomCode(e.target.value)}
        />
        <Button style={{ marginTop: '1rem' }} onClick={handleRunCustom}>
          Executar Código
        </Button>
      </div>

      {result && (
        <div>
          <Label>Saída:</Label>
          <OutputBox>{result}</OutputBox>
        </div>
      )}

      {liveLogs.length > 0 && (
        <div style={{ marginTop: '2rem' }}>
          <Label>Logs em Tempo Real:</Label>
          <OutputBox style={{ maxHeight: '300px', overflowY: 'auto' }}>
            {liveLogs.map((log, idx) => (
              <div key={idx}>{log}</div>
            ))}
          </OutputBox>
        </div>
      )}
    </Container>
  );
}

export default StrategyManager;