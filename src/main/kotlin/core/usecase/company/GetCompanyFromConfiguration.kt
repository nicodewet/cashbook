package com.thorgil.cashbook.core.usecase.company

interface GetCompanyFromConfiguration {

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
    fun getCompanyConfiguration(): CompanyConfiguration
}