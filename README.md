# trAI Bot - Crypto Bot

trAI is a Micro-SaaS platform designed to manage, execute, and monitor automated cryptocurrency trading strategies. It offers integration with Binance (Spot and Futures), supports custom strategy creation, real-time execution tracking, and includes a notification system and control dashboard for full operational oversight.

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

---

⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)


