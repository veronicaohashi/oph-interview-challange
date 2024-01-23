package com.oph.interviewchallenge.domain.transactions

import java.math.BigDecimal
import java.time.LocalDate

data class Expenditure(
    override val description: String,
    override val amount: BigDecimal,
    override val date: LocalDate
) : Transaction()