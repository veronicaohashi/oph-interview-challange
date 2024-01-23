package com.oph.interviewchallenge.domain

import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import com.oph.interviewchallenge.domain.transactions.Transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

data class FinancialStatement(
    val id: UUID = UUID.randomUUID(),
    val personId: String,
    val transactions: List<Transaction> = emptyList(),
    val rating: FinancialStatementRating? = null
) {
    private var logger: Logger = LoggerFactory.getLogger(javaClass)

    fun updateTransaction(newTransactions: List<Transaction>) =
        this.copy(transactions = this.transactions + newTransactions)

    fun rating(): FinancialStatementRating {
        if (totalIncome() == BigDecimal.ZERO) return FinancialStatementRating.D
        logger.info("TotalIncome: ${totalIncome()}")
        logger.info("TotalExpenditure: ${totalExpenditure()}")

        val ratio = totalExpenditure().divide(totalIncome(), 2, RoundingMode.HALF_UP).toDouble()
        logger.info("Ratio: $ratio")

        return when {
            ratio < 0.10 -> FinancialStatementRating.A
            ratio < 0.30 -> FinancialStatementRating.B
            ratio < 0.50 -> FinancialStatementRating.C
            else -> FinancialStatementRating.D
        }
    }

    fun disposableIncome() = totalIncome() - totalExpenditure()

    fun totalIncome() = transactions.filterIsInstance<Income>().sumOf { it.amount }

    fun totalExpenditure() = transactions.filterIsInstance<Expenditure>().sumOf { it.amount }
}