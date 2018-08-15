package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction

interface AddBusinessTransactionInRepository {

    /**
     * @throws BusinessTransactionPersistenceException when an unrecoverable persistence error
     *         occurs
     */
    @Throws(BusinessTransactionPersistenceException::class)
    fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction)

}