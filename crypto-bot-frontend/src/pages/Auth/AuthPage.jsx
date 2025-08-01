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
    whatsappNumber: "",
    telegramChatId: "",
    whatsappApiKey: "",
    role: "", // <- adicionado aqui
  });
  

  const handleLogin = async () => {
    const res = await fetch("http://127.0.0.1:8080/api/auth/login", {
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
    const paymentRes = await fetch("http://127.0.0.1:8080/api/payment/create-checkout-session", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        amount: registerForm.role === "TRADER" ? 999 : registerForm.role === "ADMIN" ? 1999 : 0,
        planName: registerForm.role,
        successUrl: "http://localhost:3000/payment/success",
        cancelUrl: "http://localhost:3000/payment/cancel"
      }),
    });
  
    if (!paymentRes.ok) {
      alert("Erro ao iniciar pagamento");
      return;
    }
  
    const { checkoutUrl } = await paymentRes.json();
  
    localStorage.setItem("pendingUserRegister", JSON.stringify(registerForm));
  
    window.location.href = checkoutUrl;
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
            <input
              placeholder="WhatsApp Number (+55...)"
              value={registerForm.whatsappNumber}
              onChange={(e) => setRegisterForm({ ...registerForm, whatsappNumber: e.target.value })}
            />
            <input
              placeholder="Telegram Chat ID"
              value={registerForm.telegramChatId}
              onChange={(e) => setRegisterForm({ ...registerForm, telegramChatId: e.target.value })}
            />
            <input
              placeholder="WhatsApp API Key"
              value={registerForm.whatsappApiKey}
              onChange={(e) => setRegisterForm({ ...registerForm, whatsappApiKey: e.target.value })}
            />
            <div style={{ marginBottom: "1rem", textAlign: "center" }}>
              <label style={{ display: "block", marginBottom: "0.5rem", color: "#7fbb5e" }}>
                Choose Your Plan:
              </label>
              <div style={{ display: "flex", justifyContent: "center", gap: "1rem", flexWrap: "wrap" }}>
                {[
                  { label: "Basic", role: "VIEWER", price: "Free" },
                  { label: "Strategist", role: "TRADER", price: "$9.99/month" },
                  { label: "Futurist", role: "ADMIN", price: "$19.99/month" }
                ].map((plan) => (
                  <button
                    key={plan.role}
                    onClick={() => setRegisterForm({ ...registerForm, role: plan.role })}
                    style={{
                      padding: "0.6rem 1rem",
                      borderRadius: "6px",
                      background: registerForm.role === plan.role ? "#7fbb5e" : "transparent",
                      border: "1px solid #4d6b3c",
                      color: registerForm.role === plan.role ? "#121b12" : "#7fbb5e",
                      fontWeight: "bold",
                      cursor: "pointer",
                      textAlign: "left",
                      minWidth: "180px"
                    }}
                  >
                    <div>
                      {plan.label} ({plan.role})
                    </div>
                    <div style={{ fontSize: "0.8rem", color: "#a0bfa2" }}>{plan.price}</div>
                  </button>
                ))}
              </div>
            </div>


            <button onClick={handleRegister}>Register</button>
          </>
        )}
      </ModalContent>
    </ModalWrapper>
  );
}
