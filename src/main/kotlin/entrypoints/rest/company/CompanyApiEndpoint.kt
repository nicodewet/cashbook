package com.thorgil.cashbook.entrypoints.rest.company

import com.thorgil.cashbook.core.entity.Company
import com.thorgil.cashbook.core.usecase.company.GetCompany
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/company")
class CompanyApiEndpoint(private val company: GetCompany) {

    @GetMapping("")
    fun getCompany(): CompanyDTO {

        val theCompany: Company = company.getCompany()

        return CompanyDTO(
                theCompany.entityName,
                theCompany.tradingName,
                theCompany.companyNumber,
                theCompany.NZBN,
                theCompany.incorporationDate,
                theCompany.annualReturnFilingMonth,
                theCompany.gstStatus,
                theCompany.gstEffectiveDate,
                theCompany.irdNumber,
                theCompany.companyStatus,
                theCompany.gstNumber(),
                theCompany.companiesOfficeRecordLink())
    }

}