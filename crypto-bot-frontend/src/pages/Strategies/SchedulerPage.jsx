// src/pages/strategies/SchedulerPage.jsx

import React, { useState } from "react";
import styled from "styled-components";
import { useAuth } from "../../contexts/AuthContext";
import axios from "axios";

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
  width: 100%;
  max-width: 400px;
  margin-bottom: 1.5rem;
`;

const Button = styled.button`
  background-color: #2e4b2e;
  color: #c7ffc7;
  border: 1px solid #4d6b4d;
  padding: 0.6rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-family: 'Courier New', monospace;

  &:hover {
    background-color: #3c5c3c;
  }
`;

const Message = styled.p`
  margin-top: 1rem;
  font-size: 1rem;
  color: ${(props) => (props.success ? '#8de68d' : '#ff7b7b')};
`;

function SchedulerPage() {
  const [strategyName, setStrategyName] = useState("");
  const [cronExpression, setCronExpression] = useState("");
  const [message, setMessage] = useState(null);
  const { token, user } = useAuth();

  const handleSchedule = async () => {
    try {
      await axios.post("/api/scheduler", {
        strategyName,
        cronExpression,
        jwtToken: token,
        userId: user.userId,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMessage({ text: "Strategy scheduled successfully!", success: true });
    } catch (err) {
      setMessage({ text: "Failed to schedule strategy.", success: false });
    }
  };

  return (
    <Container>
      <Title>Schedule Strategy Execution</Title>

      <Label htmlFor="strategyName">Strategy Name</Label>
      <Input
        id="strategyName"
        type="text"
        value={strategyName}
        onChange={(e) => setStrategyName(e.target.value)}
        placeholder="e.g., dca, ai, rsi-crossover"
      />

      <Label htmlFor="cronExpression">Cron Expression</Label>
      <Input
        id="cronExpression"
        type="text"
        value={cronExpression}
        onChange={(e) => setCronExpression(e.target.value)}
        placeholder="e.g., 0 0 9 * * *"
      />

      <Button onClick={handleSchedule}>Schedule</Button>

      {message && <Message success={message.success}>{message.text}</Message>}
    </Container>
  );
}

export default SchedulerPage;
