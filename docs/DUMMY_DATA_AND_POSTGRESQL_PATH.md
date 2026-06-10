# Dummy Data And PostgreSQL Path

## Current Showcase Mode

The application currently uses an in-memory transaction repository for fast portfolio demonstration. This keeps the first runnable API simple while still exposing realistic card-processing endpoints.

Seeded showcase records:

| Id | Account | Type | Status | Purpose |
|---|---|---|---|---|
| `txn_demo_purchase_001` | `acct_demo_1001` | `PURCHASE` | `APPROVED` | Reversible approved purchase |
| `txn_demo_decline_001` | `acct_demo_1001` | `DECLINE` | `DECLINED` | Declined transaction example |
| `txn_demo_settlement_001` | `acct_demo_2002` | `SETTLEMENT` | `SETTLED` | Settled transaction example |

## Persistence Boundary

The controller and service depend on the `TransactionRepository` interface. The current implementation is `InMemoryTransactionRepository`; a future PostgreSQL implementation should replace that bean without changing the REST API contract.

```text
+-----------------------+
| TransactionController |
+-----------+-----------+
            |
            v
+-----------------------+
| TransactionService    |
+-----------+-----------+
            |
            v
+----------------------------+
| TransactionRepository      |
+------+---------------------+
       |
       +--> InMemoryTransactionRepository
       |
       +--> Future PostgreSQL Repository
```

## Future PostgreSQL Migration

Recommended future steps:

1. Add Spring Data JDBC or Spring Data JPA after the dummy API is stable.
2. Add PostgreSQL driver dependency.
3. Create a `transactions` table with id, account id, type, amount, status, description, related transaction id, and created timestamp.
4. Move seeded records into migration or startup demo data.
5. Add Docker Compose with app and PostgreSQL services.
6. Add repository integration tests against Testcontainers or a dedicated test database.

The dummy repository is intentionally temporary. It exists so the project can demonstrate API behavior before introducing database setup complexity.

