# Food Ordering System – Microservices Architecture (Restaurant Service Included)

This repository contains a **production-grade Food Ordering System** implemented using **Java, Spring Boot, Domain-Driven Design (DDD), SAGA Pattern, Kafka, and Clean Hexagonal Architecture**.
This branch (`restaurant-service`) specifically contains the **Restaurant Service implementation** and integrates it with other core services like Order and Payment.

---

## 🚀 Project Overview

The Food Ordering System is designed as a **distributed microservices ecosystem** supporting:

* ✔️ Domain-Driven Design (DDD)
* ✔️ Event-driven communication using Kafka
* ✔️ Outbox Pattern for reliable messaging
* ✔️ SAGA pattern for distributed transaction consistency
* ✔️ Modular Maven multi-module architecture
* ✔️ Clean, scalable, enterprise-ready service design

---

## 🗂️ Project Structure (Multi-Module Overview)

Your project is organized into clear domain and service-specific modules:

```
food-ordering-system/
│
├── common/
│   └── shared classes, utilities, constants
│
├── common-domain/
│   └── generic domain-driven abstractions (ValueObjects, Entities, DomainEvents)
│
├── customer-service/
│   └── customer-domain
│   └── customer-data-access
│   └── customer-application
│   └── customer-messaging
│   └── customer-container
│
├── infrastructure/
│   ├── kafka/
│   │   ├── kafka-config-data
│   │   ├── kafka-model
│   │   ├── kafka-consumer
│   │   ├── kafka-producer
│   │   └── shared Kafka infrastructure logic
│
├── order-service/
│   ├── order-domain
│   ├── order-application
│   ├── order-data-access
│   ├── order-messaging
│   └── order-container
│
├── payment-service/
│   ├── payment-domain
│   ├── payment-data-access
│   ├── payment-messaging
│   └── payment-container
│
└── restaurant-service/
    ├── restaurant-domain
    ├── restaurant-data-access
    ├── restaurant-messaging
    └── restaurant-container
```

Each service contains:

* **domain** → Entities, Value Objects, Domain Logic
* **data-access** → JPA repositories and persistence layer
* **application** → Application services and DTO mapping
* **messaging** → Kafka producers/consumers
* **container** → Spring Boot starter module (main application)

---

## 🏗️ Restaurant Service Module (This Branch)

The **Restaurant Service** manages all restaurant-side validation and approval in the order workflow.

### ✔️ Responsibilities

* Validate restaurant operational status
* Validate ordered products & pricing
* Approve or reject orders
* Publish events for the Order service
* Manage restaurant-product relationships

### ✔️ Core Domain Models

* **Restaurant**
* **Product**
* **OrderApproval**
* **RestaurantId, ProductId, Money, OrderApprovalStatus**

---

## 📦 Tech Stack

* **Java 17+**
* **Spring Boot**
* **Kafka (Event Messaging)**
* **Domain-Driven Design**
* **Hexagonal Architecture**
* **Maven Multi-Module Project**
* **PostgreSQL**
* **Lombok**
* **Docker (Optional)**

---

## 🔧 Architecture Principles

The project follows:

* **Domain-centric design** (domain module owns business logic)
* **Separated bounded contexts** (Order, Payment, Restaurant, Customer services)
* **Outbox table per service** for transactional messaging
* **Kafka as an event backbone**
* **Clean module boundaries & dependency rules**

---

## ▶️ Running the Project

1. Clone the repo:

   ```bash
   git clone https://github.com/sandeep-sparrow/food-ordering-system.git
   ```

2. Switch to the Restaurant Service branch:

   ```bash
   git checkout implement-restaurant-service
   ```

3. Build all modules:

   ```bash
   mvn clean install -DskipTests
   ```

4. Run a specific service:

   ```bash
   mvn spring-boot:run -pl restaurant-service/restaurant-container
   ```

5. Or run all services individually via IDE.

---

## 🧪 Testing

Includes:

* Domain tests
* Order approval logic tests
* Product & restaurant validation tests
* Kafka message contract tests

Run tests:

```bash
mvn test
```

---

## 📚 Future Improvements

* Add Restaurant Management API (create/update products)
* Add open/closed hours calendar
* Add richer messaging patterns (retry, DLQ)
* Add Testcontainers-based integration tests

---

## 📬 Contact

**Author:** Sandeep Prajapati
For collaboration or discussions, connect via GitHub or LinkedIn.

---

Happy Coding! 🚀
