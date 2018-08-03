package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company
import java.time.YearMonth

/**
 * Apply business rules to SetCompanyDetails operation(s)
 */
class UpdateCompanyDetailsUseCase(private val updateCompanyDetails: UpdateCompanyDetails) {

    fun updateCompanyDetails(company: Company): Company {

        if (company.entityName.isEmpty()) {
            throw IllegalArgumentException("Company name cannot be empty")
        }

        if (company.NZBN.isEmpty()) {
            throw IllegalArgumentException("NZBN cannot be empty")
        }

        if (company.incorporationDate.isAfter(YearMonth.now())) {
            throw IllegalArgumentException("Company incorporation date cannot be in the future")
        }

        val updatedCompany: Company = updateCompanyDetails.updateDetails(company)

        if (updatedCompany.lastUpdate == null) {
            throw IllegalStateException("lastUpdate cannot be null after an updateDetails")
        }

        return updatedCompany

    }

}