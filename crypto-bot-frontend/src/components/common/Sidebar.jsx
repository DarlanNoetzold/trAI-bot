import React, { useState, useEffect } from 'react';
import { NavLink } from 'react-router-dom';
import {
  Home,
  LineChart,
  ShoppingCart,
  Layers,
  User,
  ChevronDown,
  RefreshCcw,
  LogOut
} from 'lucide-react';
import styled from 'styled-components';
import { useAuth } from '../../contexts/AuthContext';

const SidebarWrapper = styled.div`
  width: 250px;
  height: 100vh;
  background: #0f150f;
  color: #d3e6cc;
  padding: 2rem 1rem;
  font-family: 'Courier New', Courier, monospace;
  border-right: 1px solid #4d6b3c88;
  box-shadow: 2px 0 20px #00000040;
  overflow-y: auto;

  .logo {
    font-size: 1.8rem;
    font-weight: bold;
    color: #7fbb5e;
    text-align: center;
    margin-bottom: 2.5rem;
    text-shadow: 0 0 8px #4d6b3c;
  }

  .user-info {
    text-align: center;
    margin-bottom: 1.5rem;

    p {
      margin: 0 0 0.5rem;
      color: #9ccc9c;
    }

    button {
      background-color: #7fbb5e;
      color: #0f150f;
      border: none;
      padding: 0.4rem 0.8rem;
      font-weight: bold;
      border-radius: 6px;
      cursor: pointer;
      transition: background 0.3s;

      &:hover {
        background-color: #9ad187;
      }
    }
  }

  nav {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;

    a,
    .menu-toggle,
    .env-toggle {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 0.75rem;
      padding: 0.6rem 1rem;
      border-radius: 8px;
      color: #a5c8a2;
      text-decoration: none;
      transition: all 0.3s ease;
      cursor: pointer;
      background: transparent;
      border: none;

      svg {
        color: #7fbb5e;
        flex-shrink: 0;
      }

      span {
        flex: 1;
        text-align: left;
      }

      &:hover {
        background-color: #1e2a1e;
        color: #d0f0c0;
      }

      &.active {
        background-color: #2c3c2c;
        font-weight: bold;
        color: #7fbb5e;
        box-shadow: inset 2px 0 0 #7fbb5e;
      }
    }

    .submenu {
      margin-left: 1.5rem;
      display: flex;
      flex-direction: column;
      gap: 0.5rem;

      a {
        padding: 0.4rem 1rem;
        font-size: 0.9rem;

        &.active {
          font-weight: bold;
          background-color: #2f3e2f;
          color: #aef1ae;
          box-shadow: inset 2px 0 0 #7fbb5e;
        }
      }
    }
  }
`;

const Sidebar = () => {
  const [ordersOpen, setOrdersOpen] = useState(false);
  const [env, setEnv] = useState('TESTNET');
  const { user, logout } = useAuth();

  useEffect(() => {
    const stored = localStorage.getItem('binanceEnv');
    if (stored) setEnv(stored);
  }, []);

  const toggleEnv = () => {
    const newEnv = env === 'TESTNET' ? 'PRODUCTION' : 'TESTNET';
    setEnv(newEnv);
    localStorage.setItem('binanceEnv', newEnv);
    window.location.reload();
  };

  if (!user) return null;

  return (
    <SidebarWrapper>
      <div className="logo">CryptoBot</div>

      <div className="user-info">
        <p>Bem-vindo, {user.username}</p>
        <button onClick={logout}><LogOut size={16} style={{ marginRight: 4 }} /> Logout</button>
      </div>

      <nav>
        <NavLink to="/" className={({ isActive }) => (isActive ? 'active' : '')}>
          <Home size={20} />
          <span>Dashboard</span>
        </NavLink>

        <NavLink
          to="/market"
          className={({ isActive }) => (isActive ? 'active' : '')}
        >
          <LineChart size={20} />
          <span>Mercado</span>
        </NavLink>

        <button className="menu-toggle" onClick={() => setOrdersOpen(!ordersOpen)}>
          <span className="flex items-center gap-2">
            <ShoppingCart size={20} />
            <span>Ordens</span>
          </span>
          <ChevronDown
            size={16}
            style={{
              transform: ordersOpen ? 'rotate(180deg)' : 'rotate(0deg)',
              transition: 'transform 0.3s',
            }}
          />
        </button>

        {ordersOpen && (
          <div className="submenu">
            <NavLink
              to="/orders/standard"
              className={({ isActive }) => (isActive ? 'active' : '')}
            >
              Comuns
            </NavLink>
            <NavLink
              to="/orders/oco"
              className={({ isActive }) => (isActive ? 'active' : '')}
            >
              OCO
            </NavLink>
          </div>
        )}

        <NavLink
          to="/strategies"
          className={({ isActive }) => (isActive ? 'active' : '')}
        >
          <Layers size={20} />
          <span>Estratégias</span>
        </NavLink>

        <NavLink
          to="/account"
          className={({ isActive }) => (isActive ? 'active' : '')}
        >
          <User size={20} />
          <span>Conta</span>
        </NavLink>

        <button className="env-toggle" onClick={toggleEnv}>
          <RefreshCcw size={18} />
          <span>Ambiente: {env}</span>
        </button>
      </nav>
    </SidebarWrapper>
  );
};

export default Sidebar;
