# 🛒 E-Commerce Microservices Platform

A production‑style e‑commerce backend built with **Spring Cloud** microservices, **RabbitMQ** event messaging, and **polyglot persistence** (MongoDB + PostgreSQL).  
Spin up the entire stack locally via **Docker Compose**—from clone to runtime in under 60 seconds.

---

## ✨ Key Features
| Capability                    | Details                                                                                 |
|-------------------------------|-----------------------------------------------------------------------------------------|
| **Microservice Decomposition**| User, Product, Cart, Order and Notification services                                    |
| **Polyglot Persistence**      | PostgreSQL for orders/users & MongoDB for product catalog                               |
| **Async Workflows**           | Order events published to RabbitMQ; Notification service consumes asynchronously        |
| **Resilience & Observability**| Resilience4j (timeouts, circuit breakers) · Zipkin (distributed tracing)                |
| **API Gateway**               | Spring Cloud Gateway handles routing, logging and edge concerns                         |
| **Service Discovery**         | Eureka registry—no hard‑coded URLs                                                     |
| **Containerization**          | Single `docker-compose.yml` spins up the entire stack                                   |

---

## ⚙️ Tech Stack
- **Java 17**, Spring Boot 3.5.x  
- Spring Cloud (Gateway · Eureka · Sleuth)  
- RabbitMQ  
- PostgreSQL 15 · MongoDB 7  
- Resilience4j · Zipkin  
- Docker / Docker Compose  

---

## 🏗️ Architecture Diagram
Client → API Gateway → ──────>  User‑Service  ─────> PostgreSQL
                        │        
                        │        
                        │
                        ├──────>  Product‑Service ──> MongoDB  (catalog)
                        │
                        ├──────>  Cart‑Service    ──> Redis  (session cart)
                        │
                        └──────>  Order‑Service   ──> PostgreSQL (orders)
                                          │
                                          ▼
                                RabbitMQ  Exchange  «order.created»
                                          │
                                          ▼
                                  Notification‑Service  → SMTP / Twilio

---

## 🚀 Quick Start

> **Prerequisites:** Docker + Docker Compose installed

```bash
# 1 – Clone the repo
git clone https://github.com/<your‑user>/ecommerce‑application.git
cd ecommerce‑application

# 2 – Launch the full stack
docker compose up --build -d

# 3 – Verify
curl http://localhost:8761                   # Eureka dashboard
open http://localhost:9411                  # Zipkin UI
```

> **Shutdown**  
> `docker compose down -v`  # removes containers & local volumes

---

## 🔗 Service Ports

| Service           | Port |
|-------------------|------|
| Gateway           | **8080** |
| Eureka Registry   | **8761** |
| Config Server     | **8888** |
| User Service      | 8081 |
| Product Service   | 8082 |
| Cart Service      | 8083 |
| Order Service     | 8084 |
| Notification      | 8085 |
| RabbitMQ UI       | 15672 *(guest/guest)* |
| Zipkin UI         | 9411 |
| PostgreSQL        | 5432 |
| MongoDB           | 27017 |

---

## 📈 Roadmap
- ✅ Resilience4j circuit breakers  
- ✅ Zipkin tracing across async hops  
- ⬜ Kafka drop‑in broker support  
- ⬜ GitHub Actions CI/CD pipeline  
- ⬜ React / Next.js front‑end demo  

---

## 🤝 Contributing
PRs are welcome! Please open an issue first to discuss changes or enhancements.

---

## 📜 License
MIT © 2025 Kunica Tomar
