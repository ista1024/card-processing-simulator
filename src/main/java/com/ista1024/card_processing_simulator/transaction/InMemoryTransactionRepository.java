package com.ista1024.card_processing_simulator.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

@Repository
class InMemoryTransactionRepository implements TransactionRepository {

	private final ConcurrentMap<String, Transaction> transactions = new ConcurrentHashMap<>();

	InMemoryTransactionRepository() {
		seedShowcaseData();
	}

	@Override
	public Transaction save(Transaction transaction) {
		transactions.put(transaction.id(), transaction);
		return transaction;
	}

	@Override
	public Optional<Transaction> findById(String id) {
		return Optional.ofNullable(transactions.get(id));
	}

	@Override
	public List<Transaction> findByAccountId(String accountId) {
		return transactions.values().stream()
				.filter(transaction -> transaction.accountId().equals(accountId))
				.sorted(Comparator.comparing(Transaction::createdAt).reversed())
				.toList();
	}

	private void seedShowcaseData() {
		save(new Transaction(
				"txn_demo_purchase_001",
				"acct_demo_1001",
				TransactionType.PURCHASE,
				new BigDecimal("125.40"),
				TransactionStatus.APPROVED,
				"Approved grocery purchase",
				null,
				Instant.parse("2026-06-10T02:00:00Z")
		));
		save(new Transaction(
				"txn_demo_decline_001",
				"acct_demo_1001",
				TransactionType.DECLINE,
				new BigDecimal("750.00"),
				TransactionStatus.DECLINED,
				"Declined high-value transaction",
				null,
				Instant.parse("2026-06-10T02:05:00Z")
		));
		save(new Transaction(
				"txn_demo_settlement_001",
				"acct_demo_2002",
				TransactionType.SETTLEMENT,
				new BigDecimal("89.99"),
				TransactionStatus.SETTLED,
				"Settled online purchase",
				null,
				Instant.parse("2026-06-10T02:10:00Z")
		));
	}
}
