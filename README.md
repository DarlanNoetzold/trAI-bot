# trAI Bot - Crypto Bot

trAI is a Micro-SaaS platform for managing, executing, and monitoring automated cryptocurrency trading strategies. It offers deep integration with Binance (Spot and Futures), custom strategy creation, real-time execution tracking, notifications, and a full-featured dashboard for operational control.

---

## 🚀 Features

### User & Access Management
- **User Registration & Login:** Secure authentication with role-based access (ADMIN, TRADER, VIEWER).
- **User Profile Management:** Users can update their personal data, API keys, and notification preferences.
- **Role-based Dashboard:** Features and access are dynamically shown based on user role.

<img width="1880" height="727" alt="{FCB430F2-D867-4535-96E5-8E3046927FCD}" src="https://github.com/user-attachments/assets/630b6777-0ce3-4e27-b424-fd2fc4e19ff8" />
<br>
<img width="1896" height="935" alt="{AE63C19E-C11B-4194-9282-9CF31E1CEACA}" src="https://github.com/user-attachments/assets/81da9af1-1d95-45cf-97a8-9a703d8e4f81" />


### Spot Trading
- **Spot Account Info:** View balances and account status for Binance Spot.
- **Spot Trade History:** Access recent trades and order history.
- **Spot Order Management:** Place, monitor, and cancel Spot orders (market, limit, OCO).

<img width="1894" height="970" alt="{6613D572-9639-4E93-BBFC-D0726865BC59}" src="https://github.com/user-attachments/assets/8c84c824-2c8a-47ca-b9b4-f879ef89f52a" />

### Futures Trading
- **Futures Account Info:** View detailed account information for Binance Futures.
- **Futures Balances:** Check balances for any asset in the Futures wallet.
- **Open Positions:** Monitor all open futures positions in real time.
- **Futures Order Management:** Place and cancel futures orders (market, limit, etc).
- **Futures Price & Candles:** Access real-time price and candlestick data for futures symbols.

### Strategy Automation
- **Strategy Creation:** Build and manage custom trading strategies.
- **Bot Management:** Automate strategies with real trading rules and scheduling.
- **Execution Tracking:** Monitor strategy performance, orders, and profits.

<img width="1912" height="925" alt="{C18DB307-915A-4C99-AFFA-154F5063DCDD}" src="https://github.com/user-attachments/assets/c70ca129-da8d-41f2-a964-c1c99cb68f2f" />

<img width="1245" height="618" alt="{E98A6DB0-47B3-4F2B-BF44-AC8D22DC4C48}" src="https://github.com/user-attachments/assets/b9567934-691b-4fdb-826f-97ef9da72e49" />


### Notifications
- **WhatsApp & Telegram Integration:** Receive real-time alerts and notifications for trades, errors, and strategy events.
- **Customizable Notification Settings:** Users can set up their preferred notification channels.

![WhatsApp Image 2025-07-31 at 16 15 05_30e74b1f](https://github.com/user-attachments/assets/4dcab3dd-17a2-4c5a-978e-9cdfee8ad115)


### Monitoring & Analytics
- **Dashboard:** Centralized dashboard for account, trading, and strategy monitoring.
- **Market Overview:** Real-time market prices and charts.
- **Performance Analytics:** Track trading performance, PnL, and execution metrics.
- **Prometheus & Grafana:** Full monitoring stack for system health and metrics.

### Security & Audit
- **API Key Management:** Secure storage and management of Binance API keys (Spot and Futures, testnet and production).
- **Audit Logs:** All critical actions are logged for traceability and compliance.

### Infrastructure
- **API Gateway:** Unified entry point for all APIs.
- **Microservices Architecture:** Modular services for authentication, trading, strategy, notifications, and data science.
- **PostgreSQL Database:** Centralized data storage.
- **RabbitMQ Messaging:** Reliable event and message delivery between services.
- **Kubernetes Native:** All services are containerized and orchestrated via Kubernetes.

---

## 🌍 Architecture Overview

The platform is composed of:

- `auth-api`: User authentication and profile management
- `spot-api`: Binance Spot market operations
- `futures-api`: Binance Futures operations and management
- `strategy-api`: Strategy creation, execution, and scheduling
- `notification-api`: Messaging and notification delivery (WhatsApp, Telegram)
- `api-gateway`: Routes all HTTP requests and provides unified API access
- `ds`: Data Science API for analytics and data processing
- `trai-frontend`: React-based control panel and dashboard
- `custom-postgres`: PostgreSQL database
- `custom-rabbitmq`: Messaging with RabbitMQ
- `custom-prometheus` and `custom-grafana`: Monitoring and analytics stack

---

## 🧰 Requirements

- [Docker](https://www.docker.com/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

---

## ⚙️ Deployment Steps Using Kubernetes

### 1. Start Minikube

```bash
minikube start --cpus=4 --memory=8192
```

### 2. Connect to Minikube's Docker daemon

```bash
eval $(minikube -p minikube docker-env)
```

### 3. Build and Load Images

For each application:

```bash
cd <api-name>
docker build -t <api-name>:latest .
minikube image load <api-name>
```

Example:
```bash
cd auth-api
docker build -t auth-api:latest .
minikube image load auth-api
```

Repeat for:
- spot-api
- futures-api
- strategy-api
- notification-api
- api-gateway
- ds
- trai-frontend
- custom-postgres
- custom-rabbitmq
- custom-prometheus
- custom-grafana

### 4. Apply Kubernetes Configurations

```bash
kubectl apply -f oraculum-deployments.yaml
```

### 5. Create a tunnel for LoadBalancer exposure

To expose the `api-gateway` (and optionally the frontend):

```bash
minikube tunnel
```

In another terminal:

```bash
kubectl get svc api-gateway
```

Use the `EXTERNAL-IP` value to access:

```
http://<external-ip>:8080/api
```

### 6. Access the Frontend

Check the `trai-frontend` service IP and port (via `NodePort` or `LoadBalancer`):

```bash
kubectl get svc trai-frontend
```

Example:
```
http://<external-ip>:3000
```

### 7. (Optional) Monitoring Access

```bash
kubectl get svc custom-prometheus
kubectl get svc custom-grafana
```

Open the corresponding IPs/ports in your browser.

---

## 🌐 Important Environment Variables

For the React frontend, make sure to set `REACT_APP_API_URL` correctly at build time:

```bash
docker build --build-arg REACT_APP_API_URL=http://<external-ip>:8080/api -t trai-frontend .
```

---

⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
