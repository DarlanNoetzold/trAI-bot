@echo off
SET ROOT_DIR=crypto-bot-frontend

echo Criando pasta do projeto: %ROOT_DIR%
mkdir %ROOT_DIR%
cd %ROOT_DIR%

echo Criando pasta public com dados mock
mkdir public
mkdir public\mock-data
echo [] > public\mock-data\sample-candles.json
echo [] > public\mock-data\sample-trades.json

echo Criando src e App.jsx
mkdir src
cd src
echo // App principal > App.jsx

echo Criando estrutura de components
mkdir components
cd components
mkdir charts tables forms modals common

cd charts
echo // Gráfico de candles > CandlestickChart.jsx
cd ..

cd tables
echo // Tabela genérica > DataTable.jsx
cd ..

cd forms
echo // Formulário de ordens > OrderForm.jsx
cd ..

cd modals
echo // Modal genérico > Modal.jsx
cd ..

cd common
echo // Botão genérico > Button.jsx
echo // Loader genérico > Loader.jsx
cd ..
cd ..

echo Criando estrutura de páginas
mkdir pages
cd pages
mkdir Market Orders Strategies Account Dashboard

cd Market
echo // Tela de mercado > MarketOverview.jsx
cd ..

cd Orders
echo // Ordens padrão > StandardOrders.jsx
echo // Ordens OCO > OcoOrders.jsx
cd ..

cd Strategies
echo // Gerenciador de estratégias > StrategyManager.jsx
echo // Execução de bot > StrategyRunner.jsx
cd ..

cd Account
echo // Info da conta > AccountInfo.jsx
cd ..

cd Dashboard
echo // Dashboard geral > Home.jsx
cd ..
cd ..

echo Criando pasta de services e arquivos de API
mkdir services
cd services
echo // API de mercado > marketService.js
echo // API de ordens > orderService.js
echo // API de estratégias > strategyService.js
echo // API de conta > accountService.js
echo // Axios config > api.js
cd ..

echo Criando router
mkdir router
cd router
echo // Rotas da aplicação > AppRouter.jsx
cd ..

echo Criando utils
mkdir utils
cd utils
echo // Helpers utilitários > helpers.js
cd ..

echo Projeto criado com sucesso!
cd ..
pause
