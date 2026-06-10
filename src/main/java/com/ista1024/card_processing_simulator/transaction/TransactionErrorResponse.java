package com.ista1024.card_processing_simulator.transaction;

import java.time.Instant;

public record TransactionErrorResponse(
		String code,
		String message,
		Instant timestamp
) {
}
