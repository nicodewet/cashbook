package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company

interface UpdateCompanyDetails {

    /**
     * Update the company details.
     *
     * It is assumed the company details have been separately obtained from the NZBN V3 REST API (design version 1.11).
     * The relevant method being a GET request to /entities/{nzbn/
     *
     * This method can be called multiple times to change the company configuration over time.
     *
     * @param company
     * @return
     */
    fun updateDetails(company: Company): Company
}