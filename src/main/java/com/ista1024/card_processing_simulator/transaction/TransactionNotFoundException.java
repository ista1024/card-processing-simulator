package com.ista1024.card_processing_simulator.transaction;

class TransactionNotFoundException extends RuntimeException {

	TransactionNotFoundException(String id) {
		super("Transaction not found: " + id);
	}
}
