package com.thorgil.cashbook.core.usecase.company.getdetails

import com.thorgil.cashbook.core.entity.Company

interface GetCompanyDetails {

    /**
     * Get the company details as they are when this method
     * gets called.
     */
    fun getDetails(): Company?

}

class GetCompanyDetailsUseCase(val getCompanyDetails: GetCompanyDetails) {

    fun getCompanyDetails(): Company? {

        return getCompanyDetails.getDetails();
        
    }

}