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
    limit: 100,
    buyThreshold: 30,
    sellThreshold: 70,
    indicatorType: 'RSI',
    customLogicCode: ''
  });

  const [response, setResponse] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStrategy((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    try {
      const res = await api.post('/custom-strategies', strategy);
      setResponse('✅ Estratégia criada com sucesso!');
    } catch (err) {
      console.error(err);
      setResponse('❌ Erro ao criar estratégia');
    }
  };

  return (
    <Container>
      <h2 style={{ color: '#9fe69f', marginBottom: '1rem' }}>Criar Estratégia Customizada</h2>

      <Label>Nome:</Label>
      <Input name="name" value={strategy.name} onChange={handleChange} />

      <Label>Descrição:</Label>
      <Input name="description" value={strategy.description} onChange={handleChange} />

      <Label>Símbolo:</Label>
      <Input name="symbol" value={strategy.symbol} onChange={handleChange} />

      <Label>Intervalo:</Label>
      <Input name="interval" value={strategy.interval} onChange={handleChange} />

      <Label>Limite de candles:</Label>
      <Input type="number" name="limit" value={strategy.limit} onChange={handleChange} />

      <Label>Indicador:</Label>
      <Select name="indicatorType" value={strategy.indicatorType} onChange={handleChange}>
        <option value="RSI">RSI</option>
        <option value="EMA">EMA</option>
        <option value="SMA">SMA</option>
      </Select>

      <Label>Limiar de Compra:</Label>
      <Input type="number" name="buyThreshold" value={strategy.buyThreshold} onChange={handleChange} />

      <Label>Limiar de Venda:</Label>
      <Input type="number" name="sellThreshold" value={strategy.sellThreshold} onChange={handleChange} />

      <Label>Lógica Customizada (opcional):</Label>
      <TextArea name="customLogicCode" value={strategy.customLogicCode} onChange={handleChange} placeholder="def main():\n    # lógica customizada" />

      <Button onClick={handleSubmit}>Salvar Estratégia</Button>

      {response && <OutputBox>{response}</OutputBox>}
    </Container>
  );
};

export default CreateCustomStrategy;
