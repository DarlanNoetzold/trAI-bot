import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import {
  getOpenOrders,
  getOrderHistory,
  placeOcoOrder,
  cancelOcoOrder
} from '../../services/orderService';
import OrderForm from '../../components/forms/OrderForm';
import DataTable from '../../components/tables/DataTable';

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

const OcoOrders = () => {
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [orders, setOrders] = useState([]);
  const [history, setHistory] = useState([]);
  const [formData, setFormData] = useState({
    quantity: '',
    price: '',
    stopPrice: '',
    stopLimitPrice: '',
    timeInForce: 'GTC'
  });

  const fetchOpenOrders = async () => {
    try {
      const data = await getOpenOrders(symbol);
      setOrders(data);
    } catch (error) {
      console.error('Error fetching open OCO orders', error);
    }
  };

  const fetchOrderHistory = async () => {
    try {
      const data = await getOrderHistory(symbol);
      setHistory(data);
    } catch (error) {
      console.error('Error fetching OCO order history', error);
    }
  };

  const handleSubmit = async () => {
    try {
      await placeOcoOrder({
        symbol,
        side: 'SELL',
        ...formData
      });
      fetchOpenOrders();
    } catch (error) {
      console.error('Error placing OCO order', error);
    }
  };

  const handleCancelOrder = async (order) => {
    try {
      await cancelOcoOrder(symbol, order.orderListId, order.listClientOrderId);
      fetchOpenOrders();
    } catch (error) {
      console.error('Error cancelling OCO order', error);
    }
  };

  useEffect(() => {
    fetchOpenOrders();
    fetchOrderHistory();
  }, [symbol]);

  return (
    <Container>
      <Title>Manage OCO Orders</Title>

      <FormWrapper>
        <div>
          <Label>Symbol:</Label>
          <Input
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
          />
        </div>

        <StyledOrderForm>
          <OrderForm
            formData={formData}
            setFormData={setFormData}
            onSubmit={handleSubmit}
            fields={['quantity', 'price', 'stopPrice', 'stopLimitPrice', 'timeInForce']}
          />
        </StyledOrderForm>
      </FormWrapper>

      <div>
        <SectionTitle>Open OCO Orders</SectionTitle>
        <TableWrapper>
          <DataTable
            data={orders}
            columns={['orderListId', 'symbol', 'price', 'origQty', 'status']}
            onAction={handleCancelOrder}
            actionLabel="Cancel"
          />
        </TableWrapper>
      </div>

      <div>
        <SectionTitle>OCO Order History</SectionTitle>
        <TableWrapper>
          <DataTable
            data={history}
            columns={['orderListId', 'symbol', 'price', 'origQty', 'status']}
          />
        </TableWrapper>
      </div>
    </Container>
  );
};

export default OcoOrders;
