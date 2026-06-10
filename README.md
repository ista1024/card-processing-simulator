# Card Processing Simulator

Java and Spring Boot portfolio project that demonstrates REST API development, card transaction processing concepts, validation, error handling, transaction status flow, and a clean path toward PostgreSQL persistence and Docker-based deployment.

The project is designed to show practical backend skills for financial systems work: card payments, reversals, declined transactions, account transaction history, and support-friendly API behavior.

## Current Status

The backend currently exposes a Spring Boot REST API with in-memory showcase data. PostgreSQL is intentionally planned as the next persistence step so the API contract can be demonstrated before database setup is introduced.

```text
React UI / API Client
        |
        v
Spring Boot REST API
        |
        v
In-memory showcase data
        |
        v
Future PostgreSQL repository
```

## Tech Stack

| Area | Choice |
|---|---|
| Backend | Java 21, Spring Boot |
| API style | REST |
| Current data store | In-memory dummy data |
| Planned data store | PostgreSQL |
| Planned frontend | React, TypeScript |
| Planned deployment | Docker / Docker Compose |

## API Endpoints

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/api/transactions` | Create a simulated card transaction |
| `GET` | `/api/transactions/{id}` | Retrieve one transaction |
| `GET` | `/api/accounts/{accountId}/transactions` | Retrieve transaction history for an account |
| `POST` | `/api/transactions/{id}/reverse` | Reverse an eligible purchase transaction |

## Showcase Data

The application starts with demo transactions that can be used immediately:

| Transaction Id | Account Id | Type | Status |
|---|---|---|---|
| `txn_demo_purchase_001` | `acct_demo_1001` | `PURCHASE` | `APPROVED` |
| `txn_demo_decline_001` | `acct_demo_1001` | `DECLINE` | `DECLINED` |
| `txn_demo_settlement_001` | `acct_demo_2002` | `SETTLEMENT` | `SETTLED` |

## Run Locally

Prerequisites:

- Java 21 installed
- `JAVA_HOME` set to the Java 21 installation path

Start the application:

```powershell
.\gradlew.bat bootRun
```

Run tests:

```powershell
.\gradlew.bat test
```

The API will run on the default Spring Boot port:

```text
http://localhost:8080
```

## Example Requests

Create a purchase:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri http://localhost:8080/api/transactions `
  -ContentType "application/json" `
  -Body '{
    "accountId": "acct_demo_3003",
    "type": "PURCHASE",
    "amount": 42.50,
    "description": "Showcase cafe purchase"
  }'
```

Get demo account history:

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri http://localhost:8080/api/accounts/acct_demo_1001/transactions
```

Reverse the demo purchase:

```powershell
Invoke-RestMethod `
  -Method Post `
  -Uri http://localhost:8080/api/transactions/txn_demo_purchase_001/reverse
```

## Transaction Types

```text
PURCHASE
REFUND
REVERSAL
DECLINE
SETTLEMENT
```

## Transaction Statuses

```text
APPROVED
DECLINED
SETTLED
REVERSED
```

## PostgreSQL Direction

The current repository boundary is intentionally simple:

```text
TransactionController
        |
        v
TransactionService
        |
        v
TransactionRepository
        |
        +-- InMemoryTransactionRepository
        |
        +-- Future PostgreSQL implementation
```

Next persistence steps:

1. Add PostgreSQL driver and Spring Data JDBC or JPA.
2. Create a `transactions` table.
3. Move showcase data into migrations or startup seed data.
4. Add Docker Compose for the API and PostgreSQL.
5. Add repository integration tests.

## Portfolio Purpose

This project was built as a Java and Spring Boot portfolio project to demonstrate REST API development, card transaction processing concepts, validation and error handling, database-ready architecture, Docker deployment readiness, and future React frontend integration.

