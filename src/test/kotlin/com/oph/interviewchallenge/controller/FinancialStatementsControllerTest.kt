package com.oph.interviewchallenge.controller

import com.oph.interviewchallenge.domain.FinancialStatement
import com.oph.interviewchallenge.domain.FinancialStatementRating
import com.oph.interviewchallenge.domain.transactions.Income
import com.oph.interviewchallenge.infraestructure.entities.FinancialStatementEntity
import com.oph.interviewchallenge.infraestructure.repositories.FinancialStatementRepository
import com.oph.interviewchallenge.support.DbCleanUpExtension
import org.hamcrest.Matchers
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
            .andExpect(jsonPath("$.transactions[0].description", Matchers.equalTo("Salary")))
            .andExpect(jsonPath("$.transactions[0].amount", Matchers.equalTo(2500)))
            .andExpect(jsonPath("$.transactions[0].date", Matchers.equalTo("2024-02-24")))
            .andExpect(jsonPath("$.transactions[1].description", Matchers.equalTo("Rent")))
            .andExpect(jsonPath("$.transactions[1].amount", Matchers.equalTo(800)))
            .andExpect(jsonPath("$.transactions[1].date", Matchers.equalTo("2024-02-25")))
            .andExpect(jsonPath("$.rating", Matchers.equalTo("C")))
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
            .andExpect(jsonPath("$.transactions[0].description", Matchers.equalTo("Salary")))
            .andExpect(jsonPath("$.transactions[0].amount", Matchers.equalTo(2500.0)))
            .andExpect(jsonPath("$.transactions[0].date", Matchers.equalTo("2024-02-24")))
            .andExpect(jsonPath("$.transactions[1].description", Matchers.equalTo("Rent")))
            .andExpect(jsonPath("$.transactions[1].amount", Matchers.equalTo(1200)))
            .andExpect(jsonPath("$.transactions[1].date", Matchers.equalTo("2024-02-25")))
            .andExpect(jsonPath("$.rating", Matchers.equalTo("C")))
    }

    private fun post(path: String, body: String): ResultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
    )
}