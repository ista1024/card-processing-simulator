package com.ista1024.card_processing_simulator.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
		String id,
		String accountId,
		TransactionType type,
		BigDecimal amount,
		TransactionStatus status,
		String description,
		String relatedTransactionId,
		Instant createdAt
) {
	Transaction withStatus(TransactionStatus nextStatus) {
		return new Transaction(
				id,
				accountId,
				type,
				amount,
				nextStatus,
				description,
				relatedTransactionId,
				createdAt
		);
	}
}
