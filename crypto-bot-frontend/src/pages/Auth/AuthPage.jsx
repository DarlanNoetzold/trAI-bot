import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import styled from "styled-components";

const ModalWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  background: #1a1f1a;
  padding: 2rem;
  border-radius: 10px;
  max-width: 600px;
  width: 100%;
  color: #e0e0e0;
  font-family: "Courier New", monospace;

  h2 {
    text-align: center;
    margin-bottom: 1rem;
    color: #7fbb5e;
  }

  input {
    width: 100%;
    padding: 0.6rem;
    margin-bottom: 1rem;
    background: #2a2a2a;
    border: 1px solid #4d6b3c;
    border-radius: 5px;
    color: #e0e0e0;
  }

  button {
    background-color: #7fbb5e;
    border: none;
    padding: 0.6rem 1rem;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
    color: #121b12;
    transition: background 0.3s;

    &:hover {
      background-color: #9ad187;
    }
  }
`;

const ToggleButtons = styled.div`
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1rem;

  button {
    background: none;
    border: 1px solid #4d6b3c;
    padding: 0.4rem 1rem;
    border-radius: 6px;
    color: #7fbb5e;
    cursor: pointer;
    transition: background 0.3s;

    &:hover {
      background: #2a2a2a;
    }
  }
`;

export default function AuthPage({ onClose = () => {} }) {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [isLogin, setIsLogin] = useState(true);

  const [loginForm, setLoginForm] = useState({ email: "", password: "" });
  const [registerForm, setRegisterForm] = useState({
    username: "",
    email: "",
    password: "",
    birthDate: "",
    address: "",
    productionApiKey: "",
    productionSecretKey: "",
    testnetApiKey: "",
    testnetSecretKey: "",
  });

  const handleLogin = async () => {
    const res = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(loginForm),
    });
    if (res.ok) {
      const data = await res.json();
      login(data);
      onClose();
    } else {
      alert("Login failed");
    }
  };

  const handleRegister = async () => {
    const res = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(registerForm),
    });
    if (res.ok) {
      alert("Registration successful");
      setIsLogin(true);
    } else {
      alert("Registration failed");
    }
  };

  return (
    <ModalWrapper>
      <ModalContent>
        <h2>{isLogin ? "Login" : "Register"}</h2>
        <ToggleButtons>
          <button onClick={() => setIsLogin(true)}>Login</button>
          <button onClick={() => setIsLogin(false)}>Register</button>
        </ToggleButtons>

        {isLogin ? (
          <>
            <input
              placeholder="Email"
              value={loginForm.email}
              onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })}
            />
            <input
              type="password"
              placeholder="Password"
              value={loginForm.password}
              onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
            />
            <button onClick={handleLogin}>Sign In</button>
          </>
        ) : (
          <>
            <input
              placeholder="Username"
              value={registerForm.username}
              onChange={(e) => setRegisterForm({ ...registerForm, username: e.target.value })}
            />
            <input
              placeholder="Email"
              value={registerForm.email}
              onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })}
            />
            <input
              type="password"
              placeholder="Password"
              value={registerForm.password}
              onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
            />
            <input
              type="date"
              value={registerForm.birthDate}
              onChange={(e) => setRegisterForm({ ...registerForm, birthDate: e.target.value })}
            />
            <input
              placeholder="Address"
              value={registerForm.address}
              onChange={(e) => setRegisterForm({ ...registerForm, address: e.target.value })}
            />
            <input
              placeholder="Production API Key"
              value={registerForm.productionApiKey}
              onChange={(e) => setRegisterForm({ ...registerForm, productionApiKey: e.target.value })}
            />
            <input
              placeholder="Production Secret Key"
              value={registerForm.productionSecretKey}
              onChange={(e) => setRegisterForm({ ...registerForm, productionSecretKey: e.target.value })}
            />
            <input
              placeholder="Testnet API Key"
              value={registerForm.testnetApiKey}
              onChange={(e) => setRegisterForm({ ...registerForm, testnetApiKey: e.target.value })}
            />
            <input
              placeholder="Testnet Secret Key"
              value={registerForm.testnetSecretKey}
              onChange={(e) => setRegisterForm({ ...registerForm, testnetSecretKey: e.target.value })}
            />
            <button onClick={handleRegister}>Register</button>
          </>
        )}
      </ModalContent>
    </ModalWrapper>
  );
}
