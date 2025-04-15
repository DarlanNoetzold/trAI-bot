// src/styles/GlobalStyle.js
import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  *, *::before, *::after {
    box-sizing: border-box;
  }

  html, body, #root {
    margin: 0;
    padding: 0;
    height: 100%;
    width: 100%;
    overflow: hidden;
  }

  body {
    font-family: 'Courier New', Courier, monospace;
    background: linear-gradient(to bottom, #0a0f0a, #121b12);
    color: #e0e0e0;
  }
`;

export default GlobalStyle;
