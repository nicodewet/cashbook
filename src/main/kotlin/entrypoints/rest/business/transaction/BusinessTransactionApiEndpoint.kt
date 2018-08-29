package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import com.thorgil.cashbook.entrypoints.rest.business.transaction.BusinessTransactionApiEndpoint.Companion.BUSINESS_TRANSACTION_END_POINT_URL
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.validation.Valid

@RestController
@RequestMapping(BUSINESS_TRANSACTION_END_POINT_URL)
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddBusinessTransactionUseCase) {

    companion object {
        const val BUSINESS_TRANSACTION_END_POINT_URL: String = "/api/business/transactions"
    }

    private val log = LoggerFactory.getLogger(BusinessTransactionApiEndpoint::class.java)

    @PostMapping
    fun putBusinessTransaction(@Valid @RequestBody businessTransactionPostBody: AddBusinessTransactionPostBody): ResponseEntity<String> {

        try {
            log.info("HTTP POST: $businessTransactionPostBody")

            val addBusinessTransactionMessage = AddBusinessTransactionMessage(
                    type = businessTransactionPostBody.type,
                    parentTransactionUUID = businessTransactionPostBody.parentTransactionUUID,
                    scheduledDate = businessTransactionPostBody.scheduledDate,
                    completedDate = businessTransactionPostBody.completedDate,
                    amountInCents = businessTransactionPostBody.amountInCents,
                    gstInCents = businessTransactionPostBody.gstInCents,
                    evidenceLink = businessTransactionPostBody.evidenceLink
            )

            addBusinessTransactionUseCase.addBusinessTransaction(addBusinessTransactionMessage)

        } catch (e: BusinessTransactionException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        return ResponseEntity.ok("BusinessTransaction added at $formatted")
    }

    // http://example.com/users/12345/bids?start=01-01-2012&end=01-31-2012
    // TODO  fun getBusinessTransactions()
}