package com.ista1024.card_processing_simulator.transaction;

import java.util.List;
import java.util.Optional;

interface TransactionRepository {

	Transaction save(Transaction transaction);

	Optional<Transaction> findById(String id);

	List<Transaction> findByAccountId(String accountId);
}
