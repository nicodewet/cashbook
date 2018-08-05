package com.thorgil.cashbook.core.usecase.company

import com.thorgil.cashbook.core.entity.Company

interface GetCompany {

    data class CompanyConfiguration(val entityName: String,
                                    val tradingName: String,
                                    val companyNumber: String,
                                    val nzbn: String,
                                    val incorporationDate: String,
                                    val annualReturnFilingMonth: String,
                                    val gstStatus: String,
                                    val gstEffectiveDate: String,
                                    val irdNumber: String)

    /**
     * Get the company configuration details.
     *
     * @return
     */
    fun getCompany(): Company
}