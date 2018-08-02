package com.thorgil.cashbook.core.usecase.company.setdetails

import com.thorgil.cashbook.core.entity.Company
import java.time.YearMonth

interface SetCompanyDetails {

    /**
     * Set the company details.
     *
     * It is assumed the company details have been separately obtained from the NZBN V3 REST API (design version 1.11).
     * The relevant method being a GET request to /entities/{nzbn/
     *
     * This method can be called multiple times to change the company configuration over time.
     */
    fun setDetails(company: Company): Company

}

/**
 * Apply business rules to SetCompanyDetails operation(s)
 */
class SetCompanyDetailsUseCase(private val setCompanyDetails: SetCompanyDetails) {

    fun setCompanyDetails(company: Company): Company {

        if (company.entityName.isEmpty()) {
            throw IllegalArgumentException("Company name cannot be empty")
        }

        if (company.NZBN.isEmpty()) {
            throw IllegalArgumentException("NZBN cannot be empty")
        }

        if (company.incorporationDate.isAfter(YearMonth.now())) {
            throw IllegalArgumentException("Company incorporation date cannot be in the future")
        }

        return setCompanyDetails.setDetails(company)

    }

}