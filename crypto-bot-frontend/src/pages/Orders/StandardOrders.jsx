import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import OrderForm from '../../components/forms/OrderForm';
import DataTable from '../../components/tables/DataTable';
import {
  getOpenOrders,
  getOrderHistory,
  cancelOrder
} from '../../services/orderService';

const Container = styled.div`
  background-color: #0e150e;
  color: #d0e6c5;
  font-family: 'Courier New', monospace;
  padding: 2rem;
`;

const Title = styled.h2`
  font-size: 1.6rem;
  color: #b6efb6;
  margin-bottom: 2rem;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 0.5rem;
  color: #b9e6b9;
`;

const Input = styled.input`
  background-color: #1c2b1c;
  border: 1px solid #3f5f3f;
  color: #c7ffc7;
  padding: 0.6rem;
  border-radius: 4px;
  width: 200px;
  margin-bottom: 2rem;
`;

const SectionTitle = styled.h3`
  font-size: 1.2rem;
  margin: 2rem 0 1rem;
  color: #b6efb6;
`;

const FormWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 600px;
`;

const StyledOrderForm = styled.div`
  background-color: #1b2d1b;
  padding: 1.5rem;
  border-radius: 8px;
  border: 1px solid #385838;
  margin-bottom: 2rem;

  input, select, button {
    background-color: #203520;
    color: #c7ffc7;
    border: 1px solid #4d6b4d;
    padding: 0.5rem 0.75rem;
    border-radius: 6px;
    font-family: 'Courier New', monospace;
    margin-bottom: 0.75rem;
  }

  input::placeholder {
    color: #9bbf9b;
  }

  button {
    background-color: #2e4b2e;
    cursor: pointer;
    transition: background-color 0.2s;
  }

  button:hover {
    background-color: #3c5c3c;
  }

  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 1rem;
`;

const TableWrapper = styled.div`
  background-color: #1a261a;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #3f5f3f;
`;

function StandardOrders() {
  const [openOrders, setOpenOrders] = useState([]);
  const [orderHistory, setOrderHistory] = useState([]);
  const [symbol, setSymbol] = useState('BTCUSDT');

  const fetchOrders = async () => {
    try {
      const open = await getOpenOrders(symbol);
      const history = await getOrderHistory(symbol);
      setOpenOrders(open);
      setOrderHistory(history);
    } catch (err) {
      console.error('Erro ao carregar ordens:', err);
    }
  };

  const handleCancelOrder = async (orderId) => {
    try {
      await cancelOrder(symbol, orderId);
      fetchOrders();
    } catch (err) {
      console.error('Erro ao cancelar ordem:', err);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [symbol]);

  return (
    <Container>
      <Title>Ordens Comuns</Title>

      <FormWrapper>
        <div>
          <Label>Símbolo:</Label>
          <Input
            type="text"
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
          />
        </div>

        <StyledOrderForm>
          <OrderForm symbol={symbol} onOrderPlaced={fetchOrders} />
        </StyledOrderForm>
      </FormWrapper>

      <div>
        <SectionTitle>Ordens Abertas</SectionTitle>
        <TableWrapper>
          <DataTable
            data={openOrders}
            columns={['orderId', 'symbol', 'price', 'origQty', 'status', 'side']}
            onAction={handleCancelOrder}
            actionLabel="Cancelar"
          />
        </TableWrapper>
      </div>

      <div>
        <SectionTitle>Histórico de Ordens</SectionTitle>
        <TableWrapper>
          <DataTable
            data={orderHistory}
            columns={['orderId', 'symbol', 'price', 'origQty', 'status', 'side']}
          />
        </TableWrapper>
      </div>
    </Container>
  );
}

export default StandardOrders;
