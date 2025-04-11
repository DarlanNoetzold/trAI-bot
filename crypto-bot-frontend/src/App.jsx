import { BrowserRouter as Router } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Sidebar from './components/common/Sidebar';
import AppRouter from './router/AppRouter';
import styled from 'styled-components';

// Estilo global da aplicação
const AppWrapper = styled.div`
  display: flex;
  min-height: 100vh;
  background: linear-gradient(to bottom, #0a0f0a, #121b12);
  color: #e0e0e0;
  font-family: 'Courier New', Courier, monospace;
`;

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppWrapper>
          <Sidebar />
          <div style={{ flex: 1 }}>
            <AppRouter />
          </div>
        </AppWrapper>
      </AuthProvider>
    </Router>
  );
}

export default App;
