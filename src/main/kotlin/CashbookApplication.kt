package com.thorgil.cashbook

import com.thorgil.cashbook.core.usecase.company.GetCompany
import com.thorgil.cashbook.core.usecase.company.entity.GetCompanyUseCase
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

        log.info("=============================")
        log.info("$companyConfig")
        log.info("=============================")

        return GetCompanyUseCase(companyConfig)

    }
}

fun main(args: Array<String>) {
    runApplication<CashbookApplication>(*args)
}
