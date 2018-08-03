package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company

class GetCompanyDetailsUseCase(val getCompanyDetails: GetCompanyDetails) {

    fun getCompanyDetails(): Company? {

        return getCompanyDetails.getDetails()
        
    }

}