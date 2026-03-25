# Order Messaging Demo

A Spring Boot 3 (Java 21) demo application demonstrating Message Queue (RabbitMQ) usage in an Event-Driven Architecture.

## 🎯 Overview

The system simulates a simple Order Processing workflow:
- A REST API receives order creation requests
- The system publishes an event to RabbitMQ
- Multiple consumers process the event asynchronously:
  - Email notification (with simulated failure for retry demo)
  - Inventory update

## ⚙️ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring AMQP (RabbitMQ)
- Lombok
- Jackson (for JSON serialization)

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- Docker (for RabbitMQ)

### 1. Start RabbitMQ

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

RabbitMQ Management UI: http://localhost:15672 (guest/guest)

### 2. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

Or run from your IDE by executing `MessagingApplication.java`

### 3. Test the API

Create an order:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"userEmail": "test@example.com", "productId": "PROD-001", "quantity": 2}'
```

Using PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/orders" -Method Post -ContentType "application/json" -Body '{"userEmail": "test@example.com", "productId": "PROD-001", "quantity": 2}'
```

## 📦 Project Structure

```
com.demo.messaging
├── MessagingApplication.java       # Main application entry point
├── config/
│   └── RabbitMQConfig.java         # RabbitMQ configuration
├── controller/
│   └── OrderController.java        # REST API controller
├── producer/
│   └── OrderEventProducer.java     # Message producer
├── consumer/
│   ├── EmailConsumer.java          # Email notification consumer
│   └── InventoryConsumer.java      # Inventory update consumer
├── model/
│   └── OrderEvent.java             # Event model (Java record)
└── service/
    ├── EmailService.java           # Email service simulation
    └── InventoryService.java       # Inventory service simulation
```

## 📬 Messaging Design

- **Queue**: `order.queue`
- **Exchange**: `order.exchange` (Direct)
- **Routing Key**: `order.created`

## 🔄 Features Demonstrated

1. **Async Processing**: REST API returns immediately, processing happens in background
2. **Competing Consumers**: Multiple consumers listen to the same queue
3. **Retry Behavior**: EmailConsumer has 30% failure rate to demonstrate retry mechanism
4. **JSON Serialization**: Messages are serialized/deserialized using Jackson

## 📊 Sample Logs

When you create an order, you should see logs similar to:

```
📥 Received order request: orderId=xxx, userEmail=test@example.com, productId=PROD-001, quantity=2
📤 Publishing order event: orderId=xxx, userEmail=test@example.com, productId=PROD-001, quantity=2
✅ Order event published successfully: orderId=xxx
✅ Order accepted: orderId=xxx
📬 [EmailConsumer] Received order event: orderId=xxx
📧 Sending email to user: test@example.com for order: xxx
📬 [InventoryConsumer] Received order event: orderId=xxx
📦 Updating inventory for product: PROD-001, quantity: 2, orderId: xxx
✅ Inventory updated successfully for product: PROD-001
✅ Email sent successfully to: test@example.com (took 2500ms)
```

## ⚠️ Failure Simulation

The EmailConsumer has a 30% chance of throwing an exception to simulate failures. When this happens:
- The message will be retried up to 3 times
- You'll see error logs indicating the failure
- Spring AMQP handles the retry automatically

## 📝 Configuration

Key configuration in `application.yml`:

- RabbitMQ connection: `localhost:5672`
- Retry: enabled with 3 max attempts
- Server port: 8080
