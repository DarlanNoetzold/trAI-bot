// Execução de bot 
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const StrategyRunner = () => {
  const [strategies, setStrategies] = useState([]);
  const [selectedStrategy, setSelectedStrategy] = useState('');
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [interval, setInterval] = useState('1m');
  const [logs, setLogs] = useState('');

  useEffect(() => {
    axios.get('/api/strategies/list')
      .then(res => setStrategies(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleRunBot = () => {
    axios.post(`/api/strategies/run/${selectedStrategy}`, {
      symbol,
      interval
    })
      .then(res => setLogs(res.data))
      .catch(err => setLogs(`Erro ao rodar: ${err.message}`));
  };

  const handleStopBot = () => {
    axios.post(`/api/strategies/stop/${selectedStrategy}`)
      .then(res => setLogs(res.data))
      .catch(err => setLogs(`Erro ao parar: ${err.message}`));
  };

  return (
    <div>
      <h2 className="text-xl font-semibold mb-4">Monitoramento de Estratégias</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
        <select 
          value={selectedStrategy} 
          onChange={e => setSelectedStrategy(e.target.value)}
          className="border p-2 rounded w-full"
        >
          <option value="">Selecione uma estratégia</option>
          {strategies.map(strategy => (
            <option key={strategy} value={strategy}>{strategy}</option>
          ))}
        </select>

        <input
          type="text"
          placeholder="Símbolo (ex: BTCUSDT)"
          value={symbol}
          onChange={e => setSymbol(e.target.value)}
          className="border p-2 rounded w-full"
        />

        <input
          type="text"
          placeholder="Intervalo (ex: 1m)"
          value={interval}
          onChange={e => setInterval(e.target.value)}
          className="border p-2 rounded w-full"
        />
      </div>

      <div className="flex gap-4 mb-4">
        <button onClick={handleRunBot} className="bg-green-500 text-white px-4 py-2 rounded">Iniciar</button>
        <button onClick={handleStopBot} className="bg-red-500 text-white px-4 py-2 rounded">Parar</button>
      </div>

      <div className="bg-gray-900 text-white p-4 rounded overflow-auto h-80">
        <pre>{logs}</pre>
      </div>
    </div>
  );
};

export default StrategyRunner;
