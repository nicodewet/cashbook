package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionDTO
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/business/transaction")
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddBusinessTransactionUseCase) {

    @PostMapping
    fun putBusinessTransaction(@RequestBody businessTransaction: AddBusinessTransactionDTO) {
        addBusinessTransactionUseCase.addBusinessTransaction(businessTransaction)
    }
}