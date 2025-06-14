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
  box-shadow: 0 0 20px rgba(0, 255, 100, 0.15);
`;

const Title = styled.h2`
  color: #7fbb5e;
  margin-bottom: 1rem;
`;

const Button = styled.button`
  background-color: #7fbb5e;
  color: #121b12;
  padding: 0.6rem 1.2rem;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  margin-top: 1.5rem;
  cursor: pointer;

  &:hover {
    background-color: #9ad187;
  }
`;

export default function SuccessPage() {
  const navigate = useNavigate();

  return (
    <Wrapper>
      <Box>
        <Title>Payment Successful!</Title>
        <p>Your account has been successfully registered. You may now log in.</p>
        <Button onClick={() => navigate("/")}>Go to Login</Button>
      </Box>
    </Wrapper>
  );
}
