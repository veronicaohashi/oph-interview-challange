package com.oph.interviewchallenge.infraestructure.entities

import com.oph.interviewchallenge.domain.transactions.Expenditure
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "expenditures")
@Entity
class ExpenditureEntity(
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
        fun from(expenditure: Expenditure, financialStatement: FinancialStatementEntity) = ExpenditureEntity(
            description = expenditure.description,
            amount = expenditure.amount,
            date = expenditure.date,
            financialStatement = financialStatement
        )
    }

    fun toDomain() = Expenditure(
        description = description,
        amount = amount,
        date = date
    )
}