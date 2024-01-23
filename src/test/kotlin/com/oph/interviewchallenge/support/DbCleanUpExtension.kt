package com.oph.interviewchallenge.support

import com.oph.interviewchallenge.infraestructure.entities.FinancialStatementEntity
import com.oph.interviewchallenge.infraestructure.repositories.FinancialStatementRepository
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.function.Consumer

class DbCleanUpExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext?) {
        val applicationContext = SpringExtension.getApplicationContext(context!!)
        cleanUp(
            listOf(
                applicationContext.getBean(FinancialStatementRepository::class.java)
            )
        )
    }

    private fun cleanUp(repositories: Collection<CrudRepository<*, *>>) {
        repositories.forEach(Consumer { obj: CrudRepository<*, *> -> obj.deleteAll() })
    }

}