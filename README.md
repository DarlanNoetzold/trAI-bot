# Oraculum Platform - Crypto Bot (Micro-SaaS)

Oraculum is a Micro-SaaS platform composed of several Spring Boot APIs and a React-based frontend. Its goal is to provide a complete environment for crypto trading strategies integrated with Binance APIs (Spot and Futures), including notifiers and a control dashboard.

## 🌍 Architecture Overview

The platform is composed of:

- `auth-api`: user authentication
- `spot-api`: Binance Spot market operations
- `futures-api`: Binance Futures operations
- `strategy-api`: strategy execution
- `notification-api`: messaging via WhatsApp and Telegram
- `api-gateway`: routes all HTTP requests
- `ds`: Data Science API (data processing)
- `trai-frontend`: React-based control panel
- `custom-postgres`: PostgreSQL database
- `custom-rabbitmq`: messaging with RabbitMQ
- `custom-prometheus` and `custom-grafana`: monitoring stack

## 🧰 Requirements

- [Docker](https://www.docker.com/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)

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

# ✅ To-Do List - Oraculum Micro-SaaS

## 🧠 Functional & Back-End
- [ ] Create `user-profile-api` to manage user preferences and Binance keys
- [ ] Store strategy execution logs in PostgreSQL
- [ ] Generate performance reports for strategies (success rate, profit, drawdown)
- [ ] Implement a `scheduler-api` to trigger strategies automatically
- [ ] Add user roles and permissions (`ADMIN`, `TRADER`, `VIEWER`)
- [ ] Implement action auditing (login, trade, strategy execution)
- [ ] Create centralized config service (`config-api` or Spring Cloud Config)
- [ ] Add rate limiting, retry and circuit breaker to `api-gateway` (Resilience4j)

## 🛡 Security
- [ ] Add JWT expiration and refresh token mechanism
- [ ] Restrict API access based on user roles
- [ ] Log all security-related events (auth failures, suspicious patterns)

## 📊 Monitoring & Observability
- [ ] Add custom Prometheus metrics for each service
- [ ] Configure Grafana dashboards per strategy and user
- [ ] Expose actuator endpoints via gateway with proper auth

## 🧪 Testing
- [ ] Write unit tests for all core APIs
- [ ] Add integration tests using TestContainers
- [ ] Create E2E tests for frontend with Cypress

## 🖥 Frontend (React)
- [ ] Display user strategy history with logs and metrics
- [ ] Create dashboard with active strategies and live updates
- [ ] Visualize trades and price data with charting libraries
- [ ] Enable dynamic configuration of strategies

## 🚀 DevOps & Deployment
- [ ] Create Helm chart for Kubernetes deployment
- [ ] Set up GitHub Actions or GitLab CI for full CI/CD
- [ ] Deploy to remote Kubernetes cluster (DigitalOcean, GKE, etc.)
- [ ] Create backup and restore routine for PostgreSQL and Redis

## 🧾 Documentation
- [ ] Add Swagger/OpenAPI docs for all Spring Boot APIs
- [ ] Create developer onboarding guide
- [ ] Document architecture and communication flow (with diagrams)

## 💳 Optional: SaaS Features
- [ ] Add subscription plans and Stripe integration
- [ ] Block premium features for non-paid users
- [ ] Send monthly usage reports to users


⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
---

