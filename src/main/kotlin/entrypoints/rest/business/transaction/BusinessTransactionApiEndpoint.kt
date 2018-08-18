package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionDTO
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/business/transaction")
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddBusinessTransactionUseCase) {

    private val log = LoggerFactory.getLogger(BusinessTransactionApiEndpoint::class.java)

    @PostMapping
    fun putBusinessTransaction(@RequestBody businessTransaction: AddBusinessTransactionDTO): ResponseEntity<String> {
        try {
            log.info("Message $businessTransaction")
            addBusinessTransactionUseCase.addBusinessTransaction(businessTransaction)
        } catch (e: BusinessTransactionException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        return ResponseEntity.ok("BusinessTransaction added at $formatted")
    }
}