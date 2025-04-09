// Formulário de ordens 
import React, { useState } from 'react';

const OrderForm = ({ onSubmit, type = 'STANDARD' }) => {
  const [formData, setFormData] = useState({
    symbol: '',
    side: 'BUY',
    quantity: '',
    price: '',
    stopPrice: '',
    stopLimitPrice: '',
    timeInForce: 'GTC',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <input type="text" name="symbol" placeholder="Symbol" value={formData.symbol} onChange={handleChange} className="input" required />
      <select name="side" value={formData.side} onChange={handleChange} className="input">
        <option value="BUY">BUY</option>
        <option value="SELL">SELL</option>
      </select>
      <input type="number" name="quantity" placeholder="Quantity" value={formData.quantity} onChange={handleChange} className="input" required />
      <input type="number" name="price" placeholder="Price" value={formData.price} onChange={handleChange} className="input" />
      {type === 'OCO' && (
        <>
          <input type="number" name="stopPrice" placeholder="Stop Price" value={formData.stopPrice} onChange={handleChange} className="input" required />
          <input type="number" name="stopLimitPrice" placeholder="Stop Limit Price" value={formData.stopLimitPrice} onChange={handleChange} className="input" required />
        </>
      )}
      <select name="timeInForce" value={formData.timeInForce} onChange={handleChange} className="input">
        <option value="GTC">GTC</option>
        <option value="IOC">IOC</option>
      </select>
      <button type="submit" className="btn btn-primary">Submit Order</button>
    </form>
  );
};

export default OrderForm;
