package com.oph.interviewchallenge.domain

import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class FinancialStatementTest {

    @Test
    fun `should return an A rating when the ratio is less than 10`() {
        val expectedRating = FinancialStatementRating.A
        val financialStatement = FinancialStatement(
            personId = "",
            transactions = listOf(
                Income(
                    description = "Salary",
                    amount = BigDecimal("1000"),
                    date = LocalDate.parse("2024-02-24")
                ),
                Expenditure(
                    description = "Rent",
                    amount = BigDecimal("90"),
                    date = LocalDate.parse("2024-02-24")
                )
            )
        )

        Assertions.assertEquals(expectedRating, financialStatement.rating())
    }

    @Test
    fun `should return an B rating when the ratio is between 10 and 30`() {
        val expectedRating = FinancialStatementRating.B
        val financialStatement = FinancialStatement(
            personId = "",
            transactions = listOf(
                Income(
                    description = "Salary",
                    amount = BigDecimal("1000"),
                    date = LocalDate.parse("2024-02-24")
                ),
                Expenditure(
                    description = "Rent",
                    amount = BigDecimal("299"),
                    date = LocalDate.parse("2024-02-24")
                )
            )
        )

        Assertions.assertEquals(expectedRating, financialStatement.rating())
    }

    @Test
    fun `should return an C rating when the ratio is between 30 and 50`() {
        val expectedRating = FinancialStatementRating.C
        val financialStatement = FinancialStatement(
            personId = "",
            transactions = listOf(
                Income(
                    description = "Salary",
                    amount = BigDecimal("1000"),
                    date = LocalDate.parse("2024-02-24")
                ),
                Expenditure(
                    description = "Rent",
                    amount = BigDecimal("499"),
                    date = LocalDate.parse("2024-02-24")
                )
            )
        )

        Assertions.assertEquals(expectedRating, financialStatement.rating())
    }

    @Test
    fun `should return an D rating when the ratio is over 50`() {
        val expectedRating = FinancialStatementRating.D
        val financialStatement = FinancialStatement(
            personId = "",
            transactions = listOf(
                Income(
                    description = "Salary",
                    amount = BigDecimal("1000"),
                    date = LocalDate.parse("2024-02-24")
                ),
                Expenditure(
                    description = "Rent",
                    amount = BigDecimal("501"),
                    date = LocalDate.parse("2024-02-24")
                )
            )
        )

        Assertions.assertEquals(expectedRating, financialStatement.rating())
    }

    @Test
    fun `should return an D rating when the statement has no income`() {
        val expectedRating = FinancialStatementRating.D
        val financialStatement = FinancialStatement(
            personId = "",
            transactions = listOf(
                Expenditure(
                    description = "Trip",
                    amount = BigDecimal("3500"),
                    date = LocalDate.parse("2024-02-24")
                )
            )
        )

        Assertions.assertEquals(expectedRating, financialStatement.rating())
    }
}