package com.oph.interviewchallenge.infraestructure.repositories

import com.oph.interviewchallenge.infraestructure.entities.FinancialStatementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FinancialStatementRepository : JpaRepository<FinancialStatementEntity, UUID> {
}