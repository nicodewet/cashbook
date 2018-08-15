package com.thorgil.cashbook.dataproviders

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionInRepository

class BusinessTransactionDataProvider: AddBusinessTransactionInRepository {

    val transactions: HashMap<String, BusinessTransaction> = HashMap()

    override fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction) {

        transactions[businessTransaction.uuid] = businessTransaction

    }

}