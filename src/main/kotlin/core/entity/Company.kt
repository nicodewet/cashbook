package com.thorgil.cashbook.core.entity

import java.time.LocalDate
import java.time.Month

/**
 * System-wide configuration representing a copy of data stored at authoritative
 * New Zealand government agencies.
 *
 * These government agencies are the Companies Office (CO) at the MBIE and the IRD.
 *
 * We need a copy of the cited data to drive key cashbook functionality. Moreover
 * given that the data represented here will not change very often this is
 * deemed to be a relatively benign practice.
 *
 * See v3 of the /entities/{nzbn/ GET API provided by the MBIE for authoritative
 * documentation. Formally this API is called NZBN V3 REST API (design version 1.11).
 *
 * @param entityName the entityName in the /entities/{nzbn/ GET response (it is assumed
 *                   entityTypeCode indicates a NZCompany)
 * @param tradingName the name field of the latest tradingNames entry in the /entities/{nzbn/ GET response
 * @param companyNumber
 * @param NZBN New Zealand Business Number as required in the /entities/{nzbn/ GET request
 * @param incorporationDate
 * @param companyStatus
 * @param annualReturnFilingMonth company.annualReturnFilingMonth in the /entities/{nzbn/ GET response
 * @param gstStatus gstStatus in the /entities/{nzbn/ GET response [private unless authorised as public]
 * @param gstEffectiveDate gstEffectiveDate in the /entities/{nzbn/ GET response [private unless authorised as public]
 * @param irdNumber also serves as the GST number if gstStatus indicates registered for GST
 * @param lastUpdate the last time the company details were either created or updated thereafter
 */
class Company (val entityName: String,
               val tradingName: String?,
               val companyNumber: String,
               val NZBN: String,
               val incorporationDate: LocalDate,
               val annualReturnFilingMonth: Month,
               val gstStatus: GstStatus,
               val gstEffectiveDate: LocalDate?,
               val irdNumber: String?) {

    val companyStatus = CompanyStatus.REGISTERED

    fun gstNumber(): String? {
        return this.irdNumber
    }

    fun companiesOfficeRecordLink(): String {
        return "http://app.companiesoffice.govt.nz/co/$companyNumber"
    }
}

enum class CompanyStatus {
    REGISTERED
}

enum class GstStatus {
    REGISTERED,
    NOT_REGISTERED
}