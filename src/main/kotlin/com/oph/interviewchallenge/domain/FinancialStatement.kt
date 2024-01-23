package com.oph.interviewchallenge.domain

import com.oph.interviewchallenge.domain.transactions.Transaction
import java.util.UUID

data class FinancialStatement(
    val id: UUID = UUID.randomUUID(),
    val personId: String,
    val transaction: List<Transaction> = emptyList(),
    val rating: FinancialStatementRating? = null
)