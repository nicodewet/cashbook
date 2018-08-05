package com.thorgil.cashbook

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CashbookProperties() {

    @Value("\${company.entity-name}")
    lateinit var entityName: String

    @Value("\${company.trading-name}")
    lateinit var tradingName: String

    @Value("\${company.number}")
    lateinit var number: String

    @Value("\${company.nzbn}")
    lateinit var nzbn: String

    @Value("\${company.incorporation-date}")
    lateinit var incorporationDate: String

    @Value("\${company.annual-return-filing-month}")
    lateinit var annualReturnFilingMonth: String

    @Value("\${company.gst-status}")
    lateinit var gstStatus: String

    @Value("\${company.gst-effective-date}")
    lateinit var gstEffectiveDate: String

    @Value("\${company.ird-number}")
    lateinit var irdNumber: String

}

