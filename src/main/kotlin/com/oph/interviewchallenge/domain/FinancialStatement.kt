package com.oph.interviewchallenge.domain

import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import com.oph.interviewchallenge.domain.transactions.Transaction
import java.math.BigDecimal
import java.util.UUID

data class FinancialStatement(
    val id: UUID = UUID.randomUUID(),
    val personId: String,
    val transactions: List<Transaction> = emptyList(),
    val rating: FinancialStatementRating? = null
) {
    fun updateTransaction(newTransactions: List<Transaction>) =
        this.copy(transactions = this.transactions + newTransactions)

    fun rating(): FinancialStatementRating {
        if (totalIncome() == BigDecimal.ZERO) return FinancialStatementRating.D

        val ratio = totalExpenditure().divide(totalIncome()).toDouble()
        return when {
            ratio < 0.10 -> FinancialStatementRating.A
            ratio < 0.30 -> FinancialStatementRating.B
            ratio < 0.50 -> FinancialStatementRating.C
            else -> FinancialStatementRating.D
        }
    }

    private fun totalIncome() = transactions.filterIsInstance<Income>().sumOf { it.amount }

    fun totalExpenditure() = transactions.filterIsInstance<Expenditure>().sumOf { it.amount }
}