package com.oph.interviewchallenge.domain.transactions

import java.math.BigDecimal
import java.time.LocalDate

abstract class Transaction {
    abstract val description: String
    abstract val amount: BigDecimal
    abstract val date: LocalDate
}