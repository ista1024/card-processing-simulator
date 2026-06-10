package com.ista1024.card_processing_simulator.transaction;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TransactionExceptionHandler {

	@ExceptionHandler(TransactionNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	TransactionErrorResponse handleNotFound(TransactionNotFoundException exception) {
		return new TransactionErrorResponse(
				"TRANSACTION_NOT_FOUND",
				exception.getMessage(),
				Instant.now()
		);
	}

	@ExceptionHandler(InvalidTransactionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	TransactionErrorResponse handleInvalidTransaction(InvalidTransactionException exception) {
		return new TransactionErrorResponse(
				"INVALID_TRANSACTION",
				exception.getMessage(),
				Instant.now()
		);
	}
}
