import React, { useEffect, useRef } from 'react';
import { createChart } from 'lightweight-charts';

const CandlestickChart = ({ data }) => {
  const chartContainerRef = useRef(null);
  const chartRef = useRef(null);
  const seriesRef = useRef(null);

  useEffect(() => {
    // Criação do gráfico com tema dark
    chartRef.current = createChart(chartContainerRef.current, {
      layout: {
        background: { color: '#111' },
        textColor: '#DDD',
      },
      grid: {
        vertLines: { color: '#222' },
        horzLines: { color: '#222' },
      },
      width: chartContainerRef.current.clientWidth,
      height: 400,
      timeScale: {
        borderColor: '#444',
        timeVisible: true,
        secondsVisible: true,
      },
    });

    seriesRef.current = chartRef.current.addCandlestickSeries({
      upColor: '#26a69a',
      downColor: '#ef5350',
      borderVisible: false,
      wickUpColor: '#26a69a',
      wickDownColor: '#ef5350',
    });

    return () => {
      chartRef.current.remove();
    };
  }, []);

  useEffect(() => {
    if (!seriesRef.current || !data || data.length === 0) return;

    // Formatação dos dados (timestamp correto e slicing dos últimos 50)
    const formattedData = data
      .map(candle => ({
        time: Math.floor(candle[0] / 1000),
        open: parseFloat(candle[1]),
        high: parseFloat(candle[2]),
        low: parseFloat(candle[3]),
        close: parseFloat(candle[4]),
      }))
      .slice(-50); // últimos 50 candles

    seriesRef.current.setData(formattedData);
    chartRef.current.timeScale().fitContent();
  }, [data]);

  return (
    <div
      ref={chartContainerRef}
      className="w-full"
      style={{ width: '100%', height: 400 }}
    />
  );
};

export default CandlestickChart;
