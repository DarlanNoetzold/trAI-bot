import React from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";

const Wrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #1a1f1a;
`;

const Box = styled.div`
  background: #2a2a2a;
  padding: 2rem;
  border-radius: 10px;
  color: #e0e0e0;
  font-family: "Courier New", monospace;
  text-align: center;
  max-width: 500px;
  box-shadow: 0 0 20px rgba(255, 100, 100, 0.15);
`;

const Title = styled.h2`
  color: #e57373;
  margin-bottom: 1rem;
`;

const Button = styled.button`
  background-color: #e57373;
  color: #121b12;
  padding: 0.6rem 1.2rem;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  margin-top: 1.5rem;
  cursor: pointer;

  &:hover {
    background-color: #f18c8c;
  }
`;

export default function CancelPage() {
  const navigate = useNavigate();

  return (
    <Wrapper>
      <Box>
        <Title>Payment Cancelled</Title>
        <p>The payment process was cancelled or failed. Please try again.</p>
        <Button onClick={() => navigate("/")}>Back to Registration</Button>
      </Box>
    </Wrapper>
  );
}
