import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getAccountInfo, getAccountTrades, getUserInfo, updateUserInfo } from '../../services/accountService';
import Loader from '../../components/common/Loader';

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

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  background-color: #1a261a;
  color: #d8f2d0;
  margin-bottom: 2rem;
`;

const Th = styled.th`
  border: 1px solid #4a754a;
  padding: 0.75rem;
  background-color: #233423;
`;

const Td = styled.td`
  border: 1px solid #3f5f3f;
  padding: 0.6rem;
  text-align: left;
`;

const SectionTitle = styled.h3`
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: #b6efb6;
`;

const Button = styled.button`
  background: #233423;
  color: #b6efb6;
  border: none;
  border-radius: 4px;
  padding: 0.5rem 1.2rem;
  margin-right: 1rem;
  cursor: pointer;
  font-family: inherit;
  font-size: 1rem;
  margin-bottom: 1rem;
`;

function EditForm({ userData, setUserData, setIsEditing }) {
  const [form, setForm] = useState({ ...userData });
  const [saving, setSaving] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await updateUserInfo(form);
      setUserData(form);
      setIsEditing(false);
    } catch (err) {
      alert('Erro ao atualizar dados do usuário.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Label>Username:</Label>
      <Input name="username" value={form.username || ''} onChange={handleChange} />

      <Label>Email:</Label>
      <Input name="email" value={form.email || ''} onChange={handleChange} />

      <Label>Birth Date:</Label>
      <Input name="birthDate" type="date" value={form.birthDate || ''} onChange={handleChange} />

      <Label>Address:</Label>
      <Input name="address" value={form.address || ''} onChange={handleChange} />

      <Label>Testnet API Key:</Label>
      <Input name="testnetApiKey" value={form.testnetApiKey || ''} onChange={handleChange} />

      <Label>Testnet Secret Key:</Label>
      <Input name="testnetSecretKey" value={form.testnetSecretKey || ''} onChange={handleChange} />

      <Label>Production API Key:</Label>
      <Input name="productionApiKey" value={form.productionApiKey || ''} onChange={handleChange} />

      <Label>Production Secret Key:</Label>
      <Input name="productionSecretKey" value={form.productionSecretKey || ''} onChange={handleChange} />

      <Label>Whatsapp Number:</Label>
      <Input name="whatsappNumber" value={form.whatsappNumber || ''} onChange={handleChange} />

      <Label>Telegram Chat ID:</Label>
      <Input name="telegramChatId" value={form.telegramChatId || ''} onChange={handleChange} />

      <Label>Whatsapp API Key:</Label>
      <Input name="whatsappApiKey" value={form.whatsappApiKey || ''} onChange={handleChange} />

      <div>
        <Button type="submit" disabled={saving}>{saving ? 'Saving...' : 'Save'}</Button>
        <Button type="button" onClick={() => setIsEditing(false)}>Cancel</Button>
      </div>
    </form>
  );
}

function AccountInfo() {
  const [info, setInfo] = useState(null);
  const [trades, setTrades] = useState([]);
  const [symbol, setSymbol] = useState('BTCUSDT');
  const [loading, setLoading] = useState(true);

  // Novos estados para edição de usuário
  const [userData, setUserData] = useState(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    async function fetchAll() {
      setLoading(true);
      try {
        const [infoRes, tradesRes, userRes] = await Promise.all([
          getAccountInfo(),
          getAccountTrades(symbol, 50),
          getUserInfo(),
        ]);
        setInfo(infoRes);
        setTrades(tradesRes);
        setUserData(userRes);
      } catch (error) {
        console.error('Error fetching account information:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchAll();
  }, [symbol]);

  if (loading) return <Loader />;

  return (
    <Container>
      <Title>Account Information</Title>

      <div>
        <Label>Symbol:</Label>
        <Input
          type="text"
          value={symbol}
          onChange={(e) => setSymbol(e.target.value)}
        />
      </div>

      {userData && (
        <div>
          <SectionTitle>User Information</SectionTitle>
          {isEditing ? (
            <EditForm userData={userData} setUserData={setUserData} setIsEditing={setIsEditing} />
          ) : (
            <div>
              <p><strong>Username:</strong> {userData.username}</p>
              <p><strong>Email:</strong> {userData.email}</p>
              <p><strong>Birth Date:</strong> {userData.birthDate}</p>
              <p><strong>Address:</strong> {userData.address}</p>
              <p><strong>Testnet API Key:</strong> {userData.testnetApiKey}</p>
              <p><strong>Testnet Secret Key:</strong> {userData.testnetSecretKey}</p>
              <p><strong>Production API Key:</strong> {userData.productionApiKey}</p>
              <p><strong>Production Secret Key:</strong> {userData.productionSecretKey}</p>
              <p><strong>Whatsapp Number:</strong> {userData.whatsappNumber}</p>
              <p><strong>Telegram Chat ID:</strong> {userData.telegramChatId}</p>
              <p><strong>Whatsapp API Key:</strong> {userData.whatsappApiKey}</p>
              <Button onClick={() => setIsEditing(true)}>Edit</Button>
            </div>
          )}
        </div>
      )}

      {info && (
        <div>
          <SectionTitle>Balances:</SectionTitle>
          <Table>
            <thead>
              <tr>
                <Th>Asset</Th>
                <Th>Available</Th>
                <Th>In Order</Th>
              </tr>
            </thead>
            <tbody>
              {info.balances
                .filter(
                  (b) => parseFloat(b.free) > 0 || parseFloat(b.locked) > 0
                )
                .map((balance) => (
                  <tr key={balance.asset}>
                    <Td>{balance.asset}</Td>
                    <Td>{balance.free}</Td>
                    <Td>{balance.locked}</Td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </div>
      )}

      {trades && (
        <div>
          <SectionTitle>Recent Trades ({symbol}):</SectionTitle>
          <Table>
            <thead>
              <tr>
                <Th>ID</Th>
                <Th>Price</Th>
                <Th>Quantity</Th>
                <Th>Timestamp</Th>
              </tr>
            </thead>
            <tbody>
              {trades.map((trade) => (
                <tr key={trade.id}>
                  <Td>{trade.id}</Td>
                  <Td>{trade.price}</Td>
                  <Td>{trade.qty}</Td>
                  <Td>{new Date(trade.time).toLocaleString()}</Td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </Container>
  );
}

export default AccountInfo;