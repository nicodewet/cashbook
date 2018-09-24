package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import java.time.YearMonth

interface FetchBusinessTransactionsFromRepository {

    fun fetchBusinessTransactionsFromRepository(period: YearMonth): List<BusinessTransaction>

    fun fetchBusinessTransactionFromRepository(uuid: String): BusinessTransaction?
}