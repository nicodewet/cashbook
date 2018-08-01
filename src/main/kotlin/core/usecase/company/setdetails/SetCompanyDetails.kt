package com.thorgil.cashbook.core.usecase.company.setdetails

import com.thorgil.cashbook.core.entity.Company
import java.time.YearMonth

interface SetCompanyDetails {

    /**
     * Set the company details.
     *
     * This method can be called multiple times to change the company configuration
     * over time.
     */
    fun setDetails(company: Company): Company

}

class SetCompanyDetailsUseCase(private val setCompanyDetails: SetCompanyDetails) {

    fun setCompanyDetails(company: Company): Company {

        if (company.companyName.isEmpty()) {
            throw IllegalArgumentException("Company name cannot be empty")
        }

        if (company.companyNumber.isEmpty()) {
            throw IllegalArgumentException("Company number cannot be empty")
        }

        if (company.newZealandBusinessNumber.isEmpty()) {
            throw IllegalArgumentException("NZBN cannot be empty")
        }

        if (company.incorporationDate.isAfter(YearMonth.now())) {
            throw IllegalArgumentException("Company incorporation date cannot be in the future")
        }

        // Note that because gstNumber doubles as a company's IRD number, the converse of the below business
        // rule does not hold (gstRegistrationDate can be null while the company has a gstNumber)
        if (company.gstRegistrationDate != null && company.gstNumber == null) {
            throw IllegalArgumentException("GST number must be provided when GST registration date is provided")
        }

        return setCompanyDetails.setDetails(company)

    }

}