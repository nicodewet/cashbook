package com.thorgil.cashbook.core.usecase.company.getdetails

import com.thorgil.cashbook.core.entity.Company
import java.time.LocalDateTime

interface GetCompanyDetails {

    /**
     * Get the company details as they are when this method
     * gets called.
     *
     * May return null because no company details may have been set (initialisation
     * not done).
     */
    fun getDetails(): Company?

}

class GetCompanyDetailsUseCase(val getCompanyDetails: GetCompanyDetails) {

    fun getCompanyDetails(): Company? {

        return getCompanyDetails.getDetails()
        
    }

}