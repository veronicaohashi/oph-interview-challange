package com.oph.interviewchallenge.infraestructure.entities

import com.oph.interviewchallenge.domain.FinancialStatement
import com.oph.interviewchallenge.domain.FinancialStatementRating
import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Table(name = "financial_statements")
@Entity
class FinancialStatementEntity(
    @Id
    val id: UUID,

    @Column(name = "person_id", nullable = false, updatable = false)
    val personId: String,

    @OneToMany(
        mappedBy = "financialStatement",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    var incomes: List<IncomeEntity> = emptyList(),

    @OneToMany(
        mappedBy = "financialStatement",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    var expenditures: List<ExpenditureEntity> = emptyList(),

    @Column(name = "rating", nullable = false, updatable = true)
    @Enumerated(EnumType.STRING)
    var rating: FinancialStatementRating,
) {
    companion object {
        fun from(financialStatement: FinancialStatement) = FinancialStatementEntity(
            id = financialStatement.id,
            personId = financialStatement.personId,
            rating = financialStatement.rating()
        ).also { entity ->
            val (incomes, expenditures) = financialStatement.transactions.partition { it is Income }
            entity.incomes = incomes.map { IncomeEntity.from(it as Income, entity) }
            entity.expenditures = expenditures.map { ExpenditureEntity.from(it as Expenditure, entity) }
        }
    }

    fun toDomain() = FinancialStatement(
        id = id,
        personId = personId,
        transactions = incomes.map { it.toDomain() } + expenditures.map { it.toDomain() },
        rating = rating
    )
}