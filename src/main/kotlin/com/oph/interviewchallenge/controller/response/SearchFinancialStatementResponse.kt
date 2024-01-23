package com.oph.interviewchallenge.controller.response

import com.oph.interviewchallenge.domain.FinancialStatement
import com.oph.interviewchallenge.domain.FinancialStatementRating
import com.oph.interviewchallenge.domain.transactions.Transaction
import java.math.BigDecimal
import java.time.LocalDate

class SearchFinancialStatementResponse(
    val personId: String,
    val transactions: List<TransactionResponse>? = null,
    val rating: FinancialStatementRating? = null,
    val balance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalExpenditure: BigDecimal
) {
    companion object {
        fun from(financialStatement: FinancialStatement) = SearchFinancialStatementResponse(
            personId = financialStatement.personId,
            transactions = financialStatement.transactions.map { TransactionResponse.from(it) },
            rating = financialStatement.rating,
            balance = financialStatement.disposableIncome(),
            totalIncome = financialStatement.totalIncome(),
            totalExpenditure = financialStatement.totalExpenditure()
        )
    }
}

data class TransactionResponse(
    val type: String,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate
) {
    companion object {
        fun from(transaction: Transaction) = TransactionResponse(
            type = transaction.javaClass.simpleName.lowercase(),
            description = transaction.description,
            amount = transaction.amount,
            date = transaction.date
        )
    }
}