package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.entity.GstStatus
import com.thorgil.cashbook.core.usecase.company.GetCompanyFromConfiguration
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class GetCompanyUseCase(val getCompanyFromConfiguration: GetCompanyFromConfiguration) {

    fun getCompanyDetails(): Company {

        val companyConfig: GetCompanyFromConfiguration.CompanyConfiguration = getCompanyFromConfiguration.getCompanyConfiguration()

        val dayMonthYearFormatter = DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("dd-MM-yyyy")
                .toFormatter(Locale.ENGLISH)

        val monthFormatter = DateTimeFormatterBuilder()
                .appendPattern("MM")
                .toFormatter(Locale.ENGLISH)


        val configuredEntityName: String = companyConfig.entityName
        if (configuredEntityName.isEmpty()) {
            throw IllegalArgumentException("entity name cannot be empty")
        }

        val configuredTradingName: String = companyConfig.tradingName

        val configuredCompanyNumber: String = companyConfig.companyNumber
        if (configuredCompanyNumber.isEmpty()) {
            throw IllegalArgumentException("company number cannot be empty")
        }

        val configuredNZBN: String = companyConfig.nzbn
        if (configuredNZBN.isEmpty()) {
            throw IllegalArgumentException("NZBN cannot be empty")
        }

        val configuredIncorporationDate: String = companyConfig.incorporationDate
        val incorporationDate: LocalDate = LocalDate.parse(configuredIncorporationDate, dayMonthYearFormatter)

        val configuredAnnualReturnFilingMonth: String = companyConfig.annualReturnFilingMonth
        val annualReturnFilingMonth: Month = LocalDate.parse(configuredAnnualReturnFilingMonth, monthFormatter).month

        val configuredGstStatus: String = companyConfig.gstStatus
        val gstStatus: GstStatus = GstStatus.valueOf(configuredGstStatus)

        val configuredGstEffectiveDate: String = companyConfig.gstEffectiveDate
        val gstEffectiveDate: LocalDate = LocalDate.parse(configuredGstEffectiveDate, dayMonthYearFormatter)

        val configuredIrdNumber: String = companyConfig.irdNumber
        if (configuredIrdNumber.isEmpty()) {
            throw IllegalArgumentException("IRD number cannot be empty")
        }

        return Company(configuredEntityName,
                        configuredTradingName,
                        configuredCompanyNumber,
                        configuredNZBN,
                        incorporationDate,
                        annualReturnFilingMonth,
                        gstStatus,
                        gstEffectiveDate,
                        configuredIrdNumber)
        
    }

}