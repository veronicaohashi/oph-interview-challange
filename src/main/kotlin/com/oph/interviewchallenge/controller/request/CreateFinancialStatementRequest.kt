package com.oph.interviewchallenge.controller.request

import java.math.BigDecimal
import java.time.LocalDate

data class CreateFinancialStatementRequest (
    val personId: String,
    val transactions: List<TransactionsRequest>
)

data class TransactionsRequest(
    val type: String,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate
)