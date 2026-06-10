package com.ista1024.card_processing_simulator.transaction;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class TransactionController {

	private final TransactionService transactionService;

	TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/transactions")
	@ResponseStatus(HttpStatus.CREATED)
	Transaction create(@RequestBody CreateTransactionRequest request) {
		return transactionService.create(request);
	}

	@GetMapping("/transactions/{id}")
	Transaction findById(@PathVariable String id) {
		return transactionService.findById(id);
	}

	@GetMapping("/accounts/{accountId}/transactions")
	List<Transaction> findByAccount(@PathVariable String accountId) {
		return transactionService.findByAccount(accountId);
	}

	@PostMapping("/transactions/{id}/reverse")
	@ResponseStatus(HttpStatus.CREATED)
	Transaction reverse(@PathVariable String id) {
		return transactionService.reverse(id);
	}
}
