// Helpers utilitários 
// helpers.js

export const formatPrice = (value, decimals = 2) => {
    if (!value && value !== 0) return '-';
    return parseFloat(value).toFixed(decimals);
  };
  
  export const formatDate = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleString();
  };
  
  export const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));
  
  export const parseCandles = (rawData) => {
    return rawData.map(candle => ({
      time: new Date(candle[0]),
      open: parseFloat(candle[1]),
      high: parseFloat(candle[2]),
      low: parseFloat(candle[3]),
      close: parseFloat(candle[4]),
      volume: parseFloat(candle[5]),
    }));
  };
  