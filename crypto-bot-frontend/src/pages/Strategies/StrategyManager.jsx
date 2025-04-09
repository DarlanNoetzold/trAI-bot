// Gerenciador de estratégias 
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Button from '../../components/common/Button';
import DataTable from '../../components/tables/DataTable';

function StrategyManager() {
  const [strategies, setStrategies] = useState([]);
  const [selectedStrategy, setSelectedStrategy] = useState('');
  const [params, setParams] = useState({ symbol: 'BTCUSDT', interval: '1m' });
  const [customCode, setCustomCode] = useState('');
  const [result, setResult] = useState('');

  const fetchStrategies = async () => {
    try {
      const res = await axios.get('/api/strategies/list');
      setStrategies(res.data);
    } catch (err) {
      console.error('Erro ao carregar estratégias:', err);
    }
  };

  const handleRunOnce = async () => {
    try {
      const res = await axios.post(`/api/strategies/run-once/${selectedStrategy}`, params);
      setResult(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleRunBot = async () => {
    try {
      const res = await axios.post(`/api/strategies/run/${selectedStrategy}`, params);
      setResult(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleStopBot = async () => {
    try {
      const res = await axios.post(`/api/strategies/stop/${selectedStrategy}`);
      setResult(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleRunCustom = async () => {
    try {
      const res = await axios.post('/api/strategies/custom', customCode, {
        headers: { 'Content-Type': 'text/plain' },
      });
      setResult(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchStrategies();
  }, []);

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Gerenciador de Estratégias</h2>

      <div className="mb-4">
        <label className="block mb-2">Escolha uma estratégia:</label>
        <select
          className="border p-2 rounded"
          value={selectedStrategy}
          onChange={(e) => setSelectedStrategy(e.target.value)}
        >
          <option value="">-- selecione --</option>
          {strategies.map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
      </div>

      <div className="mb-4">
        <label className="block mb-1">Símbolo:</label>
        <input
          className="border p-2 w-full"
          value={params.symbol}
          onChange={(e) => setParams({ ...params, symbol: e.target.value })}
        />
        <label className="block mt-2 mb-1">Intervalo:</label>
        <input
          className="border p-2 w-full"
          value={params.interval}
          onChange={(e) => setParams({ ...params, interval: e.target.value })}
        />
      </div>

      <div className="flex gap-2 mb-6">
        <Button onClick={handleRunOnce}>Executar Uma Vez</Button>
        <Button onClick={handleRunBot}>Iniciar Bot</Button>
        <Button onClick={handleStopBot}>Parar Bot</Button>
      </div>

      <div className="mb-6">
        <h3 className="font-semibold mb-1">Executar código Python customizado:</h3>
        <textarea
          className="w-full border p-2 h-40"
          placeholder="Digite seu código Python aqui"
          value={customCode}
          onChange={(e) => setCustomCode(e.target.value)}
        />
        <Button className="mt-2" onClick={handleRunCustom}>Executar Código</Button>
      </div>

      <div className="bg-gray-100 p-4 border rounded">
        <strong>Saída:</strong>
        <pre className="whitespace-pre-wrap mt-2 text-sm">{result}</pre>
      </div>
    </div>
  );
}

export default StrategyManager;
