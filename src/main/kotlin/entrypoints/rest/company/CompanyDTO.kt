package com.thorgil.cashbook.entrypoints.rest.company

import com.fasterxml.jackson.annotation.JsonFormat
import com.thorgil.cashbook.core.entity.CompanyStatus
import com.thorgil.cashbook.core.entity.GstStatus
import java.time.LocalDate
import java.time.Month

/**
 * Following Google JSON Style Guide
 *
 * https://google.github.io/styleguide/jsoncstyleguide.xml
 */
data class CompanyDTO (

        val entityName: String?,
        val tradingName: String?,
        val companyNumber: String?,
        val NZBN: String?,
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        val incorporationDate: LocalDate?,
        val annualReturnFilingMonth: Month?,
        val gstStatus: GstStatus?,
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        val gstEffectiveDate: LocalDate?,
        val irdNumber: String?,
        val companyStatus: CompanyStatus?,
        val gstNumber: String?,
        val companiesOfficeRecordLink: String?

)

