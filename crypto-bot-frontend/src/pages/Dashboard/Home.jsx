import React, { useState } from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import {
  BarChart,
  ShoppingCart,
  LayoutDashboard,
  User,
  Lock,
  LogIn,
  LogOut,
  TrendingUp,
  Clock,
  Briefcase,
  Rocket,
} from "lucide-react";
import { Link } from "react-router-dom";

import AuthModal from "../Auth/AuthPage";
import { useAuth } from "../../contexts/AuthContext";

const Wrapper = styled.div`
  background: linear-gradient(to bottom, #0a0f0a, #121b12);
  min-height: 100vh;
  width: 100%;
  color: #e0e0e0;
  padding: 4rem 1rem;
  font-family: "Courier New", Courier, monospace;
  position: relative;
  z-index: 1;

  .heading {
    font-size: 3rem;
    text-align: center;
    font-weight: 800;
    color: #7fbb5e;
    text-shadow: 0 0 12px #4d6b3c;
  }

  .subheading {
    text-align: center;
    max-width: 700px;
    margin: 1rem auto 0;
    font-size: 1.1rem;
    color: #a8bfa2;
  }

  .plans {
    margin-top: 4rem;
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;

    @media (min-width: 768px) {
      grid-template-columns: repeat(3, 1fr);
    }

    .plan {
      background: #1a1f1a;
      border: 1px solid #4d6b3c88;
      border-radius: 1rem;
      padding: 2rem;
      text-align: center;
      box-shadow: 0 0 20px #00000050;

      h3 {
        color: #7fbb5e;
        margin-bottom: 1rem;
      }

      .price {
        font-size: 1.5rem;
        color: #9ad187;
        margin-bottom: 1rem;
      }

      ul {
        list-style: none;
        padding: 0;
        color: #b0c2b0;
        font-size: 0.9rem;

        li {
          margin: 0.5rem 0;
        }
      }

      .select-btn {
        margin-top: 1.5rem;
        background-color: #7fbb5e;
        color: #121b12;
        padding: 0.5rem 1rem;
        font-weight: bold;
        border: none;
        border-radius: 0.5rem;
        cursor: pointer;

        &:hover {
          background-color: #9ad187;
        }
      }
    }
  }

  .features {
    margin-top: 4rem;
    display: grid;
    grid-template-columns: 1fr;
    gap: 2rem;

    @media (min-width: 640px) {
      grid-template-columns: 1fr 1fr;
    }

    .feature {
      background: #1a1f1a;
      border: 1px solid #4d6b3c88;
      border-radius: 1rem;
      padding: 1.5rem;
      transition: all 0.3s ease;
      box-shadow: 0 0 20px #00000050;

      &:hover {
        transform: scale(1.02);
        box-shadow: 0 0 20px #7fbb5e33;
      }

      .icon-title {
        display: flex;
        align-items: center;
        gap: 1rem;
        color: #7fbb5e;
      }

      p {
        margin-top: 0.5rem;
        font-size: 0.9rem;
        color: #b0c2b0;
      }

      .link {
        margin-top: 1rem;
        display: inline-block;
        color: #7fbb5e;
        font-weight: bold;
        font-size: 0.9rem;
        cursor: pointer;
        transition: color 0.3s ease;

        &:hover {
          color: #9ad187;
          text-decoration: underline;
        }
      }
    }
  }

  .auth-header {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    margin-bottom: 2rem;
    font-size: 1rem;
  }

  .logout-btn {
    background-color: #7fbb5e;
    color: #121b12;
    border: none;
    padding: 0.5rem 1rem;
    font-weight: bold;
    border-radius: 0.5rem;
    cursor: pointer;
    transition: background 0.3s;

    &:hover {
      background-color: #9ad187;
    }
  }

  footer {
    margin-top: 4rem;
    text-align: center;
    font-size: 0.75rem;
    color: #555;
  }
`;

export default function HomePage() {
  const { user, logout } = useAuth();
  const [showModal, setShowModal] = useState(false);

  const getFeaturesByRole = (role) => {
    const common = [
      {
        icon: <BarChart size={32} />,
        title: "Market Overview",
        description: "Explore real-time market prices and charts.",
        link: "/market",
      },
      {
        icon: <User size={32} />,
        title: "Dashboard",
        description: "Monitor performance, orders and profits.",
        link: "/account",
      }
    ];
  
    const trader = [
      {
        icon: <ShoppingCart size={32} />,
        title: "Standard Orders",
        description: "Execute buy/sell market or limit orders.",
        link: "/orders/standard",
      },
      {
        icon: <ShoppingCart size={32} />,
        title: "OCO Orders",
        description: "Place OCO orders with stop-loss and take-profit.",
        link: "/orders/oco",
      },
      {
        icon: <LayoutDashboard size={32} />,
        title: "Bot Management",
        description: "Automate your strategies using real trading rules.",
        link: "/strategies",
      },
      {
        icon: <Clock size={32} />,
        title: "Scheduler",
        description: "Schedule your strategy executions.",
        link: "/scheduler",
      }
    ];
  
    const admin = [
      {
        icon: <Rocket size={32} />,
        title: "Binance Futures",
        description: "Access the futures trading features.",
        link: "/futures",
      }
    ];
  
    if (role === "ADMIN") return [...common, ...trader, ...admin];
    if (role === "TRADER") return [...common, ...trader];
    return common; // VIEWER or fallback
  };
  
  const features = !user
    ? [
        {
          icon: <LogIn size={32} />,
          title: "Login",
          description: "Log in to access your account and strategies dashboard.",
          action: () => setShowModal(true),
        },
        {
          icon: <Lock size={32} />,
          title: "Register",
          description: "Create your account and integrate your Binance API keys.",
          action: () => setShowModal(true),
        }
      ]
    : getFeaturesByRole(user.role);
  

  return (
    <Wrapper>
      <motion.h1
        initial={{ opacity: 0, y: -40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="heading"
      >
        CRYPTO BOT DASHBOARD
      </motion.h1>

      <motion.p
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6, duration: 0.8 }}
        className="subheading"
      >
        Automate your trading strategies with real-time execution, intelligent bots, and powerful analytics.
      </motion.p>

      {!user && (
        <div className="plans">
          <div className="plan">
            <h3>Basic Plan</h3>
            <div className="price">Free</div>
            <ul>
              <li><BarChart size={16} style={{ marginRight: 6 }} /> Access to real-time market data</li>
              <li><User size={16} style={{ marginRight: 6 }} /> Dashboard overview</li>
            </ul>
            <button className="select-btn" onClick={() => setShowModal(true)}>Get Started</button>
          </div>
          <div className="plan">
            <h3>Strategist Plan</h3>
            <div className="price">$9.99 / month</div>
            <ul>
              <li><LayoutDashboard size={16} style={{ marginRight: 6 }} /> Access to bot manager</li>
              <li><Clock size={16} style={{ marginRight: 6 }} /> Strategy scheduler</li>
              <li>All Basic Plan features</li>
            </ul>
            <button className="select-btn" onClick={() => setShowModal(true)}>Choose Plan</button>
          </div>
          <div className="plan">
            <h3>Futurist Plan</h3>
            <div className="price">$19.99 / month</div>
            <ul>
              <li><Rocket size={16} style={{ marginRight: 6 }} /> Access to Binance Futures</li>
              <li>All Strategist Plan features</li>
            </ul>
            <button className="select-btn" onClick={() => setShowModal(true)}>Go Premium</button>
          </div>
        </div>
      )}

      {user && (
        <div className="auth-header">
          <span>Welcome, {user.username}</span>
          <button onClick={logout} className="logout-btn">
            <LogOut size={18} style={{ marginRight: 4 }} /> Logout
          </button>
        </div>
      )}

      <div className="features">
        {features.map((feature, idx) => (
          <motion.div
            key={idx}
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: idx * 0.15 }}
            viewport={{ once: true }}
            className="feature"
          >
            <div className="icon-title">
              {feature.icon}
              <h3>{feature.title}</h3>
            </div>
            <p>{feature.description}</p>
            {feature.link ? (
              <Link className="link" to={feature.link}>Access →</Link>
            ) : (
              <span className="link" onClick={feature.action}>Access →</span>
            )}
          </motion.div>
        ))}
      </div>

      <footer>
        Powered by Binance Testnet · Developed by Darlan Noetzold
      </footer>

      {showModal && <AuthModal onClose={() => setShowModal(false)} />}
    </Wrapper>
  );
}
