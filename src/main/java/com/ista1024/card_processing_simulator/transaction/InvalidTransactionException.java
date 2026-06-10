package com.ista1024.card_processing_simulator.transaction;

class InvalidTransactionException extends RuntimeException {

	InvalidTransactionException(String message) {
		super(message);
	}
}
