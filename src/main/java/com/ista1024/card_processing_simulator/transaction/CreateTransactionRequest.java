package com.ista1024.card_processing_simulator.transaction;

import java.math.BigDecimal;

public record CreateTransactionRequest(
		String accountId,
		TransactionType type,
		BigDecimal amount,
		String description
) {
}
