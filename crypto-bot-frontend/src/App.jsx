import { BrowserRouter as Router } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Sidebar from './components/common/Sidebar';
import AppRouter from './router/AppRouter';
import styled from 'styled-components';

const AppWrapper = styled.div`
  display: flex;
  flex-direction: row;
  height: 100dvh;
  width: 100vw;
  overflow: hidden;
  background: linear-gradient(to bottom, #0a0f0a, #121b12);
  color: #e0e0e0;
  font-family: 'Courier New', Courier, monospace;

  @media (max-width: 768px) {
    flex-direction: column;
  }
`;

const ContentWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1rem;
  overflow-y: auto;
  min-width: 0;
  max-width: 100%;
`;

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppWrapper>
          <Sidebar />
          <ContentWrapper>
            <AppRouter />
          </ContentWrapper>
        </AppWrapper>
      </AuthProvider>
    </Router>
  );
}

export default App;
