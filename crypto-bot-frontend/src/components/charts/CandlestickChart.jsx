// Gráfico de candles 
import React from 'react';
import {
  ResponsiveContainer,
  ComposedChart,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  CandlestickSeries,
} from 'recharts';

function CandlestickChart({ data }) {
  const formatData = data.map(([timestamp, open, high, low, close]) => ({
    time: new Date(timestamp).toLocaleTimeString(),
    open: parseFloat(open),
    high: parseFloat(high),
    low: parseFloat(low),
    close: parseFloat(close),
  }));

  return (
    <ResponsiveContainer width="100%" height={400}>
      <ComposedChart data={formatData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="time" />
        <YAxis domain={['dataMin', 'dataMax']} />
        <Tooltip />
        <CandlestickSeries
          dataKey="close"
          openKey="open"
          closeKey="close"
          highKey="high"
          lowKey="low"
        />
      </ComposedChart>
    </ResponsiveContainer>
  );
}

export default CandlestickChart;