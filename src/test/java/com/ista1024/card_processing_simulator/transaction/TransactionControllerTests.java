package com.ista1024.card_processing_simulator.transaction;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void returnsSeededShowcaseTransactionsForDemoAccount() throws Exception {
		mockMvc.perform(get("/api/accounts/acct_demo_1001/transactions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
				.andExpect(jsonPath("$[0].accountId").value("acct_demo_1001"))
				.andExpect(jsonPath("$[*].id", hasItem("txn_demo_decline_001")))
				.andExpect(jsonPath("$[*].status", hasItem("DECLINED")));
	}

	@Test
	void createsPurchaseTransaction() throws Exception {
		mockMvc.perform(post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "accountId": "acct_demo_3003",
								  "type": "PURCHASE",
								  "amount": 42.50,
								  "description": "Showcase cafe purchase"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").value("acct_demo_3003"))
				.andExpect(jsonPath("$.type").value("PURCHASE"))
				.andExpect(jsonPath("$.amount").value(42.50))
				.andExpect(jsonPath("$.status").value("APPROVED"))
				.andExpect(jsonPath("$.description").value("Showcase cafe purchase"));
	}

	@Test
	void reversesEligiblePurchaseTransaction() throws Exception {
		mockMvc.perform(post("/api/transactions/txn_demo_purchase_001/reverse"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").value("acct_demo_1001"))
				.andExpect(jsonPath("$.type").value("REVERSAL"))
				.andExpect(jsonPath("$.status").value("APPROVED"))
				.andExpect(jsonPath("$.relatedTransactionId").value("txn_demo_purchase_001"));

		mockMvc.perform(get("/api/transactions/txn_demo_purchase_001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("REVERSED"));
	}

	@Test
	void rejectsInvalidTransactionAmount() throws Exception {
		mockMvc.perform(post("/api/transactions")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "accountId": "acct_demo_3003",
								  "type": "PURCHASE",
								  "amount": 0,
								  "description": "Invalid purchase"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("INVALID_TRANSACTION"))
				.andExpect(jsonPath("$.message").value("Amount must be greater than zero"));
	}

	@Test
	void returnsNotFoundForUnknownTransaction() throws Exception {
		mockMvc.perform(get("/api/transactions/missing"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("TRANSACTION_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Transaction not found: missing"));
	}
}
