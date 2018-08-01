package com.thorgil.cashbook.core.entity

import java.time.Month
import java.time.YearMonth

data class Company(val companyName: String,
                   val tradingName: String?,
                   val companyNumber: String,
                   val newZealandBusinessNumber: String,
                   val incorporationDate: YearMonth,
                   val companyStatus: String,
                   val annualReturnFilingMonth: Month,
                   val gstRegistrationDate: YearMonth?,
                   val gstNumber: String?)

