import React, { useState } from 'react';
import styled from 'styled-components';
import api from '../../services/api';

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

const Input = styled.input`
  background: #1e2d1e;
  border: 1px solid #4d6b3c;
  color: #d3ffcb;
  padding: 0.5rem;
  border-radius: 4px;
  width: 100%;
  margin-bottom: 1rem;
`;

const Select = styled.select`
  background: #1e2d1e;
  border: 1px solid #4d6b3c;
  color: #d3ffcb;
  padding: 0.5rem;
  border-radius: 4px;
  width: 100%;
  margin-bottom: 1rem;
`;

const TextArea = styled.textarea`
  background: #111b11;
  border: 1px solid #355232;
  color: #d7fcd7;
  width: 100%;
  padding: 1rem;
  height: 150px;
  border-radius: 6px;
  font-size: 0.95rem;
  margin-bottom: 1rem;
`;

const Button = styled.button`
  background: #4d6b3c;
  border: none;
  color: #f2fff2;
  padding: 0.5rem 1.2rem;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.2s;
  margin-right: 1rem;

  &:hover {
    background: #628d50;
  }
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

const CreateCustomStrategy = () => {
  const [strategy, setStrategy] = useState({
    name: '',
    description: '',
    symbol: 'BTCUSDT',
    interval: '1m',
    position: 'LONG',
    indicatorType: 'RSI',
    period: 14,
    entryCondition: '<',
    entryValue: '30',
    exitCondition: '>',
    exitValue: '70',
    strategyCode: ''
  });

  const [response, setResponse] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStrategy((prev) => ({
      ...prev,
      [name]: name === 'period' ? parseInt(value) : value,
    }));
  };

  const handleSubmit = async () => {
    try {
      const payload = {
        name: strategy.name,
        symbol: strategy.symbol,
        interval: strategy.interval,
        position: strategy.position,
        strategyCode: strategy.strategyCode,
        indicators: [
          {
            type: strategy.indicatorType,
            period: strategy.period,
            entryCondition: strategy.entryCondition,
            entryValue: strategy.entryValue,
            exitCondition: strategy.exitCondition,
            exitValue: strategy.exitValue
          }
        ]
      };

      const res = await api.post('/custom-strategies', payload);
      setResponse('✅ Strategy created successfully!');
    } catch (err) {
      console.error(err);
      setResponse('❌ Error while creating strategy');
    }
  };

  return (
    <Container>
      <h2 style={{ color: '#9fe69f', marginBottom: '1rem' }}>Create Custom Strategy</h2>

      <Label>Name:</Label>
      <Input name="name" value={strategy.name} onChange={handleChange} />

      <Label>Symbol:</Label>
      <Input name="symbol" value={strategy.symbol} onChange={handleChange} />

      <Label>Interval:</Label>
      <Input name="interval" value={strategy.interval} onChange={handleChange} />

      <Label>Position:</Label>
      <Select name="position" value={strategy.position} onChange={handleChange}>
        <option value="LONG">LONG</option>
        <option value="SHORT">SHORT</option>
      </Select>

      <Label>Indicator:</Label>
      <Select name="indicatorType" value={strategy.indicatorType} onChange={handleChange}>
        <option value="RSI">RSI</option>
        <option value="EMA">EMA</option>
        <option value="SMA">SMA</option>
      </Select>

      <Label>Period:</Label>
      <Input type="number" name="period" value={strategy.period} onChange={handleChange} />

      <Label>Entry Condition:</Label>
      <Select name="entryCondition" value={strategy.entryCondition} onChange={handleChange}>
        <option value=">">{'>'}</option>
        <option value="<">{'<'}</option>
        <option value="==">{'=='}</option>
      </Select>

      <Label>Entry Value:</Label>
      <Input name="entryValue" value={strategy.entryValue} onChange={handleChange} />

      <Label>Exit Condition:</Label>
      <Select name="exitCondition" value={strategy.exitCondition} onChange={handleChange}>
        <option value=">">{'>'}</option>
        <option value="<">{'<'}</option>
        <option value="==">{'=='}</option>
      </Select>

      <Label>Exit Value:</Label>
      <Input name="exitValue" value={strategy.exitValue} onChange={handleChange} />

      <Label>Custom Logic (optional):</Label>
      <TextArea
        name="strategyCode"
        value={strategy.strategyCode}
        onChange={handleChange}
        placeholder="def main():\n    # custom strategy logic"
      />

      <Button onClick={handleSubmit}>Save Strategy</Button>

      {response && <OutputBox>{response}</OutputBox>}
    </Container>
  );
};

export default CreateCustomStrategy;
