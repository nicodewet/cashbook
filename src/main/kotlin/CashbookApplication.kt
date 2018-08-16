package com.thorgil.cashbook

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.company.GetCompany
import com.thorgil.cashbook.core.usecase.company.entity.GetCompanyUseCase
import com.thorgil.cashbook.dataproviders.BusinessTransactionDataProvider
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CashbookApplication {

    private val log = LoggerFactory.getLogger(CashbookApplication::class.java)

    @Bean
    fun company(properties: CashbookProperties): GetCompany {

        val companyConfig: GetCompany.CompanyConfiguration =  GetCompany.CompanyConfiguration(
                properties.entityName,
                properties.tradingName,
                properties.number,
                properties.nzbn,
                properties.incorporationDate,
                properties.annualReturnFilingMonth,
                properties.gstStatus,
                properties.gstEffectiveDate,
                properties.irdNumber)

        val getCompany: GetCompany = GetCompanyUseCase(companyConfig)
        val company: Company = getCompany.getCompany()

        log.info("=============================")
        log.info("Name: ${company.entityName}")
        log.info("T/A: ${company.tradingName}")
        log.info("Company #: ${company.companyNumber}")
        log.info("NZBN: ${company.NZBN}")
        log.info("Incorporation Date: ${company.incorporationDate.toString()}")
        log.info("A/R Filing Month: ${company.annualReturnFilingMonth.toString()}")
        log.info("GST Status: ${company.gstStatus.toString()}")
        log.info("GST Effective Date: ${company.gstEffectiveDate.toString()}")
        log.info("IRD Number: ${company.irdNumber}")
        log.info("=============================")

        return getCompany

    }

    @Bean
    fun businessTransactionDataProvider(): BusinessTransactionDataProvider {
        return BusinessTransactionDataProvider()
    }

    @Bean
    fun businessTransactionUseCase(companyProvider: GetCompany, addBusinessTransactionInRepo: AddBusinessTransactionInRepository): AddBusinessTransactionUseCase {
        return AddBusinessTransactionUseCase(companyProvider, addBusinessTransactionInRepo)
    }
}

fun main(args: Array<String>) {
    runApplication<CashbookApplication>(*args)
}
