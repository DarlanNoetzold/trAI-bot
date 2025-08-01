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
  LogOut,
  Clock
} from 'lucide-react';
import styled from 'styled-components';
import { useAuth } from '../../contexts/AuthContext';


const SidebarWrapper = styled.div`
  width: ${(props) => (props.collapsed ? '70px' : '250px')};
  height: 100vh;
  background: #0f150f;
  color: #d3e6cc;
  padding: 2rem 1rem;
  font-family: 'Courier New', Courier, monospace;
  border-right: 1px solid #4d6b3c88;
  box-shadow: 2px 0 20px #00000040;
  overflow-y: auto;
  transition: width 0.3s;

  @media (max-width: 768px) {
    position: absolute;
    z-index: 1000;
    height: 100%;
    left: ${(props) => (props.show ? '0' : '-250px')};
    transition: left 0.3s;
  }

  .logo {
    font-size: ${(props) => (props.collapsed ? '1rem' : '1.8rem')};
    font-weight: bold;
    color: #7fbb5e;
    text-align: center;
    margin-bottom: 2.5rem;
    text-shadow: 0 0 8px #4d6b3c;
    overflow: hidden;
    white-space: nowrap;
  }

  .user-info {
    text-align: center;
    margin-bottom: 1.5rem;

    p {
      margin: 0 0 0.5rem;
      color: #9ccc9c;
      font-size: ${(props) => (props.collapsed ? '0' : '1rem')};
      transition: font-size 0.3s;
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
      display: ${(props) => (props.collapsed ? 'none' : 'flex')};
      align-items: center;
      gap: 0.3rem;
      justify-content: center;

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
        display: ${(props) => (props.collapsed ? 'none' : 'block')};
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
  const [collapsed, setCollapsed] = useState(false);
  const [showMobile, setShowMobile] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('binanceEnv');
    if (stored) setEnv(stored);

    const handleResize = () => {
      if (window.innerWidth <= 768) {
        setCollapsed(true);
      } else {
        setCollapsed(false);
        setShowMobile(false);
      }
    };

    window.addEventListener('resize', handleResize);
    handleResize();
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const toggleEnv = () => {
    const newEnv = env === 'TESTNET' ? 'PRODUCTION' : 'TESTNET';
    setEnv(newEnv);
    localStorage.setItem('binanceEnv', newEnv);
    window.location.reload();
  };

  if (!user) return null;
  const isViewer = user?.role === "VIEWER";
  const isTrader = user?.role === "TRADER" || user?.role === "ADMIN";
  const isAdmin = user?.role === "ADMIN";

  return (
    <SidebarWrapper collapsed={collapsed} show={showMobile}>
  <div className="logo">{collapsed ? 'CB' : 'CryptoBot'}</div>

  <div className="user-info">
    <p>{user.username}</p>
    <button onClick={logout}><LogOut size={16} /> Logout</button>
  </div>

  <nav>
  <NavLink to="/" className={({ isActive }) => (isActive ? 'active' : '')}>
    <Home size={20} />
    <span>Dashboard</span>
  </NavLink>

  <NavLink to="/market" className={({ isActive }) => (isActive ? 'active' : '')}>
    <LineChart size={20} />
    <span>Market</span>
  </NavLink>

  {isTrader && (
    <>
      <button className="menu-toggle" onClick={() => setOrdersOpen(!ordersOpen)}>
        <span className="flex items-center gap-2">
          <ShoppingCart size={20} />
          <span>Orders</span>
        </span>
        <ChevronDown
          size={16}
          style={{
            transform: ordersOpen ? 'rotate(180deg)' : 'rotate(0deg)',
            transition: 'transform 0.3s'
          }}
        />
      </button>

      {ordersOpen && (
        <div className="submenu">
          <NavLink to="/orders/standard" className={({ isActive }) => (isActive ? 'active' : '')}>
            Standard
          </NavLink>
          <NavLink to="/orders/oco" className={({ isActive }) => (isActive ? 'active' : '')}>
            OCO
          </NavLink>
        </div>
      )}

      <NavLink to="/strategies" end className={({ isActive }) => (isActive ? 'active' : '')}>
        <Layers size={20} />
        <span>Strategies</span>
      </NavLink>

      <NavLink to="/strategies/create" className={({ isActive }) => (isActive ? 'active' : '')}>
        <Layers size={20} />
        <span>Create Strategy</span>
      </NavLink>

      <NavLink to="/scheduler" className={({ isActive }) => (isActive ? 'active' : '')}>
        <Clock size={20} />
        <span>Scheduler</span>
      </NavLink>
    </>
  )}

  {isAdmin && (
    <NavLink to="/futures" className={({ isActive }) => (isActive ? 'active' : '')}>
      <ShoppingCart size={20} />
      <span>Futures</span>
    </NavLink>
  )}

  <NavLink to="/account" className={({ isActive }) => (isActive ? 'active' : '')}>
    <User size={20} />
    <span>Account</span>
  </NavLink>

  <button className="env-toggle" onClick={toggleEnv}>
    <RefreshCcw size={18} />
    <span>Environment: {env}</span>
  </button>
</nav>

</SidebarWrapper>

  );
};

export default Sidebar;