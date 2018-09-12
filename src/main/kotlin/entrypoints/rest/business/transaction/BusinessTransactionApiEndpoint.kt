package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import com.thorgil.cashbook.core.usecase.business.transaction.FetchBusinessTransactions
import com.thorgil.cashbook.entrypoints.rest.business.transaction.BusinessTransactionApiEndpoint.Companion.BUSINESS_TRANSACTION_END_POINT_URL
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.YearMonth
import javax.validation.Valid
import javax.validation.constraints.Pattern

@RestController
@Validated
@RequestMapping(BUSINESS_TRANSACTION_END_POINT_URL)
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddBusinessTransactionUseCase,
                                     private val fetchBusinessTransactions: FetchBusinessTransactions) {

    companion object {
        const val BUSINESS_TRANSACTION_END_POINT_URL: String = "/api/business/transactions"
    }

    private val log = LoggerFactory.getLogger(BusinessTransactionApiEndpoint::class.java)

    @PostMapping
    fun putBusinessTransaction(@Valid @RequestBody businessTransactionPostBody: AddBusinessTransactionPostBody): ResponseEntity<AddBusinessTransactionResponse> {

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

            val addedBusinessTransaction =  addBusinessTransactionUseCase.addBusinessTransaction(addBusinessTransactionMessage)

            return ResponseEntity.ok(AddBusinessTransactionResponse(addedBusinessTransaction.uuid,null))

        } catch (e: BusinessTransactionException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AddBusinessTransactionResponse(null,e.message))
        }

    }

    // curl -sS 'http://localhost:8080/api/business/transactions?period=2018-06'
    @RequestMapping(params = ["period"],
                    method = [RequestMethod.GET],
                    produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBusinessTransactions(@Pattern(regexp = "^\\d{4}-\\d{2}$") @RequestParam("period") period: String):
            ResponseEntity<List<BusinessTransaction>> {

        val specifiedPeriod: YearMonth = YearMonth.parse(period)
        val transactions: List<BusinessTransaction> = fetchBusinessTransactions.fetchBusinessTransactions(specifiedPeriod)

        return ResponseEntity.ok(transactions)
    }
}