import React from "react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import styled from "styled-components";
import {
  BarChart,
  ShoppingCart,
  LayoutDashboard,
  User,
} from "lucide-react";

const Wrapper = styled.div`
  background: linear-gradient(to bottom, #0a0f0a, #121b12);
  min-height: 100vh;
  color: #e0e0e0;
  padding: 4rem 1rem;
  font-family: "Courier New", Courier, monospace;

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

      a {
        margin-top: 1rem;
        display: inline-block;
        color: #7fbb5e;
        font-weight: bold;
        font-size: 0.9rem;
        text-decoration: none;
        transition: color 0.3s ease;

        &:hover {
          color: #9ad187;
          text-decoration: underline;
        }
      }
    }
  }

  footer {
    margin-top: 4rem;
    text-align: center;
    font-size: 0.75rem;
    color: #555;
  }
`;

const features = [
  {
    icon: <BarChart size={32} />,
    title: "Mercado em Tempo Real",
    description: "Visualize preços, candles e ordens com gráficos dinâmicos.",
    link: "/market",
  },
  {
    icon: <ShoppingCart size={32} />,
    title: "Ordens",
    description:
      "Envie ordens simples e OCO rapidamente com controle completo.",
    link: "/orders",
  },
  {
    icon: <LayoutDashboard size={32} />,
    title: "Gerenciar Bots",
    description:
      "Execute e monitore bots automáticos com estratégias personalizadas.",
    link: "/strategies",
  },
  {
    icon: <User size={32} />,
    title: "Dashboard",
    description:
      "Acompanhe seu portfólio, saldo e estratégias em tempo real.",
    link: "/account",
  },
];

export default function HomePage() {
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
        Controle completo das suas estratégias automatizadas com insights,
        execução de ordens e gráficos em tempo real.
      </motion.p>

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
            <Link to={feature.link}>Acessar →</Link>
          </motion.div>
        ))}
      </div>

      <footer>
        Powered by Binance Testnet · Developed by Darlan Noetzold
      </footer>
    </Wrapper>
  );
}
