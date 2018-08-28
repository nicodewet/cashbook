package com.thorgil.cashbook.dataproviders

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository
import com.thorgil.cashbook.core.usecase.business.transaction.FetchBusinessTransactionsFromRepository
import java.time.YearMonth

class BusinessTransactionDataProvider: AddBusinessTransactionInRepository, FetchBusinessTransactionsFromRepository {

    val transactions: HashMap<String, BusinessTransaction> = HashMap()

    override fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction) {

        transactions[businessTransaction.uuid] = businessTransaction

    }

    override fun fetchBusinessTransactionsFromRepository(period: YearMonth): List<BusinessTransaction> {

        val foundTransactions: MutableList<BusinessTransaction> = mutableListOf()

        for ((uuid, transaction) in transactions) {
            if (transaction.completedDate != null) {
                if (transaction.completedDate.year == period.year && transaction.completedDate.month == period.month) {
                    foundTransactions.add(transaction)
                }
            } else if (transaction.scheduledDate != null) {
                if (transaction.scheduledDate.year == period.year && transaction.scheduledDate.month == period.month) {
                    foundTransactions.add(transaction)
                }
            }
        }

        return foundTransactions

    }

}