package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import java.time.YearMonth

interface FetchBusinessTransactions {

    /**
     * Fetch all BusinessTransactions during a specific year and/or month.
     *
     * @param period
     * @return a list of BusinessTransactions, may be an empty list
     */
    fun fetchBusinessTransactions(period: YearMonth): List<BusinessTransaction>

}

class FetchBusinessTransactionsUseCase(private val fetchBusinessTransactions: FetchBusinessTransactionsFromRepository): FetchBusinessTransactions {

    override fun fetchBusinessTransactions(period: YearMonth): List<BusinessTransaction> {
         return fetchBusinessTransactions.fetchBusinessTransactionsFromRepository(period)
    }

}