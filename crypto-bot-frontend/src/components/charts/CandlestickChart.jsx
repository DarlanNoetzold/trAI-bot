import React, { useEffect, useRef } from 'react';
import { createChart } from 'lightweight-charts';

const CandlestickChart = ({ data }) => {
  const chartContainerRef = useRef(null);
  const chartRef = useRef(null);
  const seriesRef = useRef(null);
  const tooltipRef = useRef(null);

  useEffect(() => {
    const chart = createChart(chartContainerRef.current, {
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
      crosshair: {
        mode: 0,
      },
      timeScale: {
        borderColor: '#444',
        timeVisible: true,
        secondsVisible: true,
      },
    });

    const candleSeries = chart.addCandlestickSeries({
      upColor: '#26a69a',
      downColor: '#ef5350',
      borderVisible: false,
      wickUpColor: '#26a69a',
      wickDownColor: '#ef5350',
    });

    chartRef.current = chart;
    seriesRef.current = candleSeries;

    // Tooltip DOM
    const tooltip = document.createElement('div');
    tooltip.style = `
      position: absolute;
      background-color: rgba(17, 17, 17, 0.95);
      color: #eee;
      padding: 8px;
      font-family: Courier, monospace;
      font-size: 12px;
      border: 1px solid #444;
      border-radius: 4px;
      pointer-events: none;
      z-index: 1000;
      display: none;
    `;
    chartContainerRef.current.appendChild(tooltip);
    tooltipRef.current = tooltip;

    chart.subscribeCrosshairMove(param => {
      if (!param || !param.time || !param.seriesData) {
        tooltip.style.display = 'none';
        return;
      }

      const data = param.seriesData.get(candleSeries);
      if (!data) return;

      const { open, high, low, close } = data;
      tooltip.innerHTML = `
        <strong>Open:</strong> ${open} <br/>
        <strong>High:</strong> ${high} <br/>
        <strong>Low:</strong> ${low} <br/>
        <strong>Close:</strong> ${close}
      `;

      const { clientWidth, clientHeight } = chartContainerRef.current;
      const x = param.point.x;
      const y = param.point.y;

      tooltip.style.display = 'block';
      tooltip.style.left = `${x + 10}px`;
      tooltip.style.top = `${y - 50}px`;
    });

    return () => {
      chart.remove();
    };
  }, []);

  useEffect(() => {
    if (!seriesRef.current || !data || data.length === 0) return;

    const formattedData = data
      .map(candle => ({
        time: Math.floor(candle[0] / 1000),
        open: parseFloat(candle[1]),
        high: parseFloat(candle[2]),
        low: parseFloat(candle[3]),
        close: parseFloat(candle[4]),
      }))
      .slice(-50);

    seriesRef.current.setData(formattedData);
    chartRef.current.timeScale().fitContent();
  }, [data]);

  return (
    <div
      ref={chartContainerRef}
      className="w-full"
      style={{ width: '100%', height: 400, position: 'relative' }}
    />
  );
};

export default CandlestickChart;
