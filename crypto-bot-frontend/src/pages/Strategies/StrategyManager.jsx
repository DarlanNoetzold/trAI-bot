import React, { useEffect, useState, useCallback } from 'react';
import styled from 'styled-components';
import {
  listStrategies,
  runStrategyOnce,
  runStrategy,
  stopStrategy,
  runCustomStrategy,
  fetchStrategyLogs,
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

  &:disabled {
    background: #2c3f2c;
    cursor: not-allowed;
    opacity: 0.6;
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

const Pagination = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 1rem;
  gap: 0.5rem;
`;

function StrategyManager() {
  const [strategies, setStrategies] = useState([]);
  const [selectedStrategy, setSelectedStrategy] = useState(localStorage.getItem('selected_strategy') || '');
  const [params, setParams] = useState({ symbol: 'BTCUSDT', interval: '1m' });
  const [customCode, setCustomCode] = useState(`# Python template example\n\ndef main(symbol, interval):\n    # strategy logic here\n    print(f"Running for {symbol} with interval {interval}")`);
  const [result, setResult] = useState('');
  const [isRunning, setIsRunning] = useState(localStorage.getItem('bot_running') === 'true');
  const [statusMessage, setStatusMessage] = useState('');
  const [logs, setLogs] = useState([]);
  const [page, setPage] = useState(0);
  const [pageSize] = useState(20);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchStrategies();
  }, []);

  const fetchLogs = useCallback(async () => {
    try {
      const res = await fetchStrategyLogs(selectedStrategy, page, pageSize);
      setLogs(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error('Error fetching logs:', err);
    }
  }, [selectedStrategy, page, pageSize]);

  useEffect(() => {
    if (selectedStrategy) fetchLogs();
  }, [page, selectedStrategy, fetchLogs]);

  useEffect(() => {
    let dots = '';
    const interval = setInterval(() => {
      dots = dots.length >= 3 ? '' : dots + '.';
      setStatusMessage(`Running${dots}`);
    }, 1000);
    return () => clearInterval(interval);
  }, [isRunning]);

  const fetchStrategies = async () => {
    try {
      const res = await listStrategies();
      setStrategies(res);
    } catch (err) {
      console.error('Error fetching strategies:', err);
      setResult('❌ Failed to load strategies');
    }
  };

  const handleRunOnce = async () => {
    try {
      const res = await runStrategyOnce(selectedStrategy, params);
      setResult(res);
    } catch (err) {
      console.error(err);
      setResult('❌ Error running once');
    }
  };

  const handleRunBot = async () => {
    try {
      const res = await runStrategy(selectedStrategy, params);
      setResult(res);
      setIsRunning(true);
      localStorage.setItem('bot_running', 'true');
      localStorage.setItem('selected_strategy', selectedStrategy);
    } catch (err) {
      console.error(err);
      setResult('❌ Error starting bot');
    }
  };

  const handleStopBot = async () => {
    try {
      const res = await stopStrategy(selectedStrategy);
      setResult(res);
      setIsRunning(false);
      localStorage.removeItem('bot_running');
      localStorage.removeItem('selected_strategy');
    } catch (err) {
      console.error(err);
      setResult('❌ Error stopping bot');
    }
  };

  const handleRunCustom = async () => {
    try {
      const res = await runCustomStrategy(customCode);
      setResult(res);
    } catch (err) {
      console.error(err);
      setResult('❌ Error running custom code');
    }
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  return (
    <Container>
      <h2 style={{ fontSize: '1.6rem', marginBottom: '1rem', color: '#9fe69f' }}>
        Strategy Manager
      </h2>

      <div>
        <Label>Select a strategy:</Label>
        <Select value={selectedStrategy} onChange={(e) => setSelectedStrategy(e.target.value)}>
          <option value="">-- select --</option>
          {strategies.map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </Select>
      </div>

      <div>
        <Label>Symbol:</Label>
        <Input value={params.symbol} onChange={(e) => setParams({ ...params, symbol: e.target.value })} />
        <Label>Interval:</Label>
        <Input value={params.interval} onChange={(e) => setParams({ ...params, interval: e.target.value })} />
      </div>

      <div style={{ margin: '1.5rem 0' }}>
        <Button onClick={handleRunOnce}>Run Once</Button>
        <Button onClick={handleRunBot}>Start Bot</Button>
        <Button onClick={handleStopBot}>Stop Bot</Button>
      </div>

      <Status $active={isRunning}>{isRunning ? statusMessage : 'Bot Stopped'}</Status>

      <div style={{ marginTop: '2rem' }}>
        <Label>Custom Python Code:</Label>
        <TextArea value={customCode} onChange={(e) => setCustomCode(e.target.value)} />
        <Button style={{ marginTop: '1rem' }} onClick={handleRunCustom}>Run Code</Button>
      </div>

      {result && (
        <div>
          <Label>Output:</Label>
          <OutputBox>{result}</OutputBox>
        </div>
      )}

      <div style={{ marginTop: '2rem' }}>
        <Button onClick={fetchLogs}>🔄 Refresh Logs</Button>
      </div>

      {logs.length > 0 && (
        <div style={{ marginTop: '2rem' }}>
          <Label>Logs:</Label>
          <OutputBox style={{ maxHeight: '300px', overflowY: 'auto' }}>
            {logs.map((log, idx) => (
              <div key={idx}>[{new Date(log.timestamp).toLocaleTimeString()}] {log.message}</div>
            ))}
          </OutputBox>

          <Pagination>
            <Button onClick={() => handlePageChange(page - 1)} disabled={page === 0}>Prev</Button>
            {Array.from({ length: totalPages }, (_, i) => (
              <Button key={i} onClick={() => handlePageChange(i)} disabled={page === i}>
                {i + 1}
              </Button>
            ))}
            <Button onClick={() => handlePageChange(page + 1)} disabled={page >= totalPages - 1}>Next</Button>
          </Pagination>
        </div>
      )}
    </Container>
  );
}

export default StrategyManager;
