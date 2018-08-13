package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction

interface AddBusinessTransactionInRepository {

    /**
     * @return true if the business transaction was successfully added to the repo, otherwise
     *         false
     * @throws BusinessTransactionPersistenceException when an unrecoverable persistence error
     *         occurs
     */
    @Throws(BusinessTransactionPersistenceException::class)
    fun addBusinessTransactionInRepository(businessTransaction: BusinessTransaction): Boolean

}