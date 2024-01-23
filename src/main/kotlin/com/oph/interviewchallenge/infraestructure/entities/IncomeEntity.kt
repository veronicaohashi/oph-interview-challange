package com.oph.interviewchallenge.infraestructure.entities

import com.oph.interviewchallenge.domain.transactions.Income
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "incomes")
@Entity
class IncomeEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "description", nullable = false, updatable = false)
    val description: String,

    @Column(name = "amount", nullable = false, updatable = false)
    val amount: BigDecimal,

    @Column(name = "date", nullable = false, updatable = false)
    val date: LocalDate,

    @ManyToOne
    val financialStatement: FinancialStatementEntity? = null,
) {
    companion object {
        fun from(income: Income, financialStatement: FinancialStatementEntity) = IncomeEntity(
            description = income.description,
            amount = income.amount,
            date = income.date,
            financialStatement = financialStatement
        )
    }

    fun toDomain() = Income(
        description = description,
        amount = amount,
        date = date
    )
}