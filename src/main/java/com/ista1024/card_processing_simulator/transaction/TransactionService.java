package com.ista1024.card_processing_simulator.transaction;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
class TransactionService {

	private final TransactionRepository transactionRepository;
	private final Clock clock;

	TransactionService(TransactionRepository transactionRepository) {
		this(transactionRepository, Clock.systemUTC());
	}

	TransactionService(TransactionRepository transactionRepository, Clock clock) {
		this.transactionRepository = transactionRepository;
		this.clock = clock;
	}

	Transaction create(CreateTransactionRequest request) {
		validateCreateRequest(request);

		Transaction transaction = new Transaction(
				UUID.randomUUID().toString(),
				request.accountId().trim(),
				request.type(),
				request.amount(),
				initialStatusFor(request.type()),
				normalizeDescription(request.description()),
				null,
				Instant.now(clock)
		);

		return transactionRepository.save(transaction);
	}

	Transaction findById(String id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new TransactionNotFoundException(id));
	}

	List<Transaction> findByAccount(String accountId) {
		if (accountId == null || accountId.isBlank()) {
			throw new InvalidTransactionException("Account id is required");
		}

		return transactionRepository.findByAccountId(accountId.trim());
	}

	Transaction reverse(String id) {
		Transaction original = findById(id);
		if (original.type() != TransactionType.PURCHASE) {
			throw new InvalidTransactionException("Only purchase transactions can be reversed");
		}
		if (original.status() != TransactionStatus.APPROVED && original.status() != TransactionStatus.SETTLED) {
			throw new InvalidTransactionException("Only approved or settled purchases can be reversed");
		}

		transactionRepository.save(original.withStatus(TransactionStatus.REVERSED));

		Transaction reversal = new Transaction(
				UUID.randomUUID().toString(),
				original.accountId(),
				TransactionType.REVERSAL,
				original.amount(),
				TransactionStatus.APPROVED,
				"Reversal for " + original.id(),
				original.id(),
				Instant.now(clock)
		);

		return transactionRepository.save(reversal);
	}

	private void validateCreateRequest(CreateTransactionRequest request) {
		if (request == null) {
			throw new InvalidTransactionException("Transaction request is required");
		}
		if (request.accountId() == null || request.accountId().isBlank()) {
			throw new InvalidTransactionException("Account id is required");
		}
		if (request.type() == null) {
			throw new InvalidTransactionException("Transaction type is required");
		}
		if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidTransactionException("Amount must be greater than zero");
		}
	}

	private TransactionStatus initialStatusFor(TransactionType type) {
		return switch (type) {
			case DECLINE -> TransactionStatus.DECLINED;
			case SETTLEMENT -> TransactionStatus.SETTLED;
			case PURCHASE, REFUND, REVERSAL -> TransactionStatus.APPROVED;
		};
	}

	private String normalizeDescription(String description) {
		if (description == null || description.isBlank()) {
			return "Simulated card transaction";
		}

		return description.trim();
	}
}
