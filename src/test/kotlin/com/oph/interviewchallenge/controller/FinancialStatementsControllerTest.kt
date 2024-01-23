package com.oph.interviewchallenge.controller

import com.oph.interviewchallenge.domain.FinancialStatement
import com.oph.interviewchallenge.domain.FinancialStatementRating
import com.oph.interviewchallenge.domain.transactions.Expenditure
import com.oph.interviewchallenge.domain.transactions.Income
import com.oph.interviewchallenge.infraestructure.entities.FinancialStatementEntity
import com.oph.interviewchallenge.infraestructure.repositories.FinancialStatementRepository
import com.oph.interviewchallenge.support.DbCleanUpExtension
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(DbCleanUpExtension::class)
class FinancialStatementsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var financialStatementRepository: FinancialStatementRepository

    @Test
    fun `should create a new financial statement for the given person`() {
        val request = """
            {
                "personId": "123",
                "transactions": [{
                    "type": "income",
                    "description": "Salary",
                    "amount": 2500,
                    "date": "2024-02-24"
                },
                {
                    "type": "expenditure",
                    "description": "Rent",
                    "amount": 800,
                    "date": "2024-02-25"
                }]
            }
        """.trimIndent()

        val result = post(
            path = "/financial-statements",
            body = request
        )

        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.transactions", hasSize<String>(2)))
            .andExpect(jsonPath("$.transactions[0].description", equalTo("Salary")))
            .andExpect(jsonPath("$.transactions[0].amount", equalTo(2500)))
            .andExpect(jsonPath("$.transactions[0].date", equalTo("2024-02-24")))
            .andExpect(jsonPath("$.transactions[1].description", equalTo("Rent")))
            .andExpect(jsonPath("$.transactions[1].amount", equalTo(800)))
            .andExpect(jsonPath("$.transactions[1].date", equalTo("2024-02-25")))
            .andExpect(jsonPath("$.rating", equalTo("C")))
    }

    @Test
    fun `should update the new financial statement for the given person`() {
        financialStatementRepository.save(
            FinancialStatementEntity.from(
                FinancialStatement(
                    personId = "123",
                    transactions = mutableListOf(
                        Income(
                            description = "Salary",
                            amount = BigDecimal("2500"),
                            date = LocalDate.parse("2024-02-24")
                        )
                    ),
                    rating = FinancialStatementRating.A
                )
            )
        )

        val request = """
            {
                "personId": "123",
                "transactions": [{
                    "type": "expenditure",
                    "description": "Rent",
                    "amount": 1200,
                    "date": "2024-02-25"
                }]
            }
        """.trimIndent()

        val result = post(
            path = "/financial-statements",
            body = request
        )

        result.andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.transactions", hasSize<String>(2)))
            .andExpect(jsonPath("$.transactions[0].description", equalTo("Salary")))
            .andExpect(jsonPath("$.transactions[0].amount", equalTo(2500.0)))
            .andExpect(jsonPath("$.transactions[0].date", equalTo("2024-02-24")))
            .andExpect(jsonPath("$.transactions[1].description", equalTo("Rent")))
            .andExpect(jsonPath("$.transactions[1].amount", equalTo(1200)))
            .andExpect(jsonPath("$.transactions[1].date", equalTo("2024-02-25")))
            .andExpect(jsonPath("$.rating", equalTo("C")))
    }

    @Test
    fun `Should find the financial statement for given person`() {
        financialStatementRepository.save(
            FinancialStatementEntity.from(
                FinancialStatement(
                    personId = "123",
                    transactions = mutableListOf(
                        Income(
                            description = "Salary",
                            amount = BigDecimal("2500"),
                            date = LocalDate.parse("2024-02-24")
                        ),
                        Expenditure(
                            description = "Rent",
                            amount = BigDecimal("1000"),
                            date = LocalDate.parse("2024-02-25")
                        ),
                        Expenditure(
                            description = "Water",
                            amount = BigDecimal("300"),
                            date = LocalDate.parse("2024-02-26")
                        )
                    ),
                    rating = FinancialStatementRating.C
                )
            )
        )

        val result = get(
            path = "/financial-statements",
            queryString = "personId=123"
        )

        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.personId", equalTo("123")))
            .andExpect(jsonPath("$.transactions", hasSize<String>(3)))
            .andExpect(jsonPath("$.transactions[0].description", equalTo("Salary")))
            .andExpect(jsonPath("$.transactions[0].amount", equalTo(2500.0)))
            .andExpect(jsonPath("$.transactions[0].date", equalTo("2024-02-24")))
            .andExpect(jsonPath("$.transactions[0].type", equalTo("income")))
            .andExpect(jsonPath("$.transactions[1].description", equalTo("Rent")))
            .andExpect(jsonPath("$.transactions[1].amount", equalTo(1000.0)))
            .andExpect(jsonPath("$.transactions[1].date", equalTo("2024-02-25")))
            .andExpect(jsonPath("$.transactions[1].type", equalTo("expenditure")))
            .andExpect(jsonPath("$.transactions[2].description", equalTo("Water")))
            .andExpect(jsonPath("$.transactions[2].amount", equalTo(300.0)))
            .andExpect(jsonPath("$.transactions[2].date", equalTo("2024-02-26")))
            .andExpect(jsonPath("$.transactions[2].type", equalTo("expenditure")))
            .andExpect(jsonPath("$.rating", equalTo("D")))
            .andExpect(jsonPath("$.balance", equalTo(1200.0)))
            .andExpect(jsonPath("$.totalIncome", equalTo(2500.0)))
            .andExpect(jsonPath("$.totalExpenditure", equalTo(1300.0)))
    }

    private fun post(path: String, body: String): ResultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
    )

    private fun get(path: String, queryString: String?): ResultActions = mockMvc.perform(
        MockMvcRequestBuilders.get("$path?$queryString")
            .contentType(MediaType.APPLICATION_JSON))
}