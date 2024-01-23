package com.oph.interviewchallenge.controller

import com.oph.interviewchallenge.controller.request.CreateFinancialStatementRequest
import com.oph.interviewchallenge.controller.response.SearchFinancialStatementResponse
import com.oph.interviewchallenge.domain.FinancialStatement
import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import com.oph.interviewchallenge.infraestructure.entities.FinancialStatementEntity
import com.oph.interviewchallenge.infraestructure.repositories.FinancialStatementRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/financial-statements")
@CrossOrigin
class FinancialStatementsController(
    private val financialStatementRepository: FinancialStatementRepository
) {
    private var logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun create(@RequestBody request: CreateFinancialStatementRequest): ResponseEntity<FinancialStatement> {
        val financialStatement = financialStatementRepository
            .findByPersonId(request.personId)
            ?.toDomain().also { logger.info("Existing financial statement: $it") }
            ?: FinancialStatement(personId = request.personId).also { logger.info("New financial statement: $it") }

        val transactions = request.transactions.map {
            when(it.type) {
                "income" -> Income(
                    description = it.description,
                    amount = it.amount,
                    date = it.date
                )
                "expenditure" -> Expenditure(
                    description = it.description,
                    amount = it.amount,
                    date = it.date
                )
                else -> throw RuntimeException("Unknown transaction type")
            }
        }.also { logger.info("New transactions:$it")}

        val financialStatementWithNewTransactions = financialStatement.updateTransaction(transactions)

        val updatedFinancialStatement = financialStatementRepository
            .save(FinancialStatementEntity.from(financialStatementWithNewTransactions))
            .also { logger.info("Saved financial statement: $it") }

        return ResponseEntity(updatedFinancialStatement.toDomain(), HttpStatus.CREATED)
    }

    @GetMapping
    fun search(@RequestParam(value = "personId", required = true) personId: String): ResponseEntity<SearchFinancialStatementResponse> {
        val financialStatement = financialStatementRepository.findByPersonId(personId)?.toDomain()
            ?: throw RuntimeException("Financial statement not found for the given personId: $personId")

        return ResponseEntity(SearchFinancialStatementResponse.from(financialStatement), HttpStatus.OK)
    }


}