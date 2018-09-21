package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionUseCase
import com.thorgil.cashbook.core.usecase.business.transaction.BusinessTransactionException
import com.thorgil.cashbook.core.usecase.business.transaction.FetchBusinessTransactionsUseCase
import com.thorgil.cashbook.entrypoints.rest.business.transaction.BusinessTransactionApiEndpoint.Companion.BUSINESS_TRANSACTION_END_POINT_URL
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.YearMonth
import java.time.format.DateTimeParseException
import javax.validation.Valid
import javax.validation.constraints.Pattern



@RestController
@Validated
@RequestMapping(BUSINESS_TRANSACTION_END_POINT_URL)
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddBusinessTransactionUseCase,
                                     private val fetchBusinessTransactionsUseCase: FetchBusinessTransactionsUseCase) {

    companion object {
        const val BUSINESS_TRANSACTION_END_POINT_URL: String = "/api/business/transactions"
        const val PERIOD_REQ_PARAM_NAME: String = "period"
        const val PERIOD_REQ_PARAM_VALIDATION_ERROR_MESSAGE_POSTFIX = "must match yyyy-dd"
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

    /**
     * curl -sS 'http://localhost:8080/api/business/transactions?period=2018-06'
     */
    @RequestMapping(params = [PERIOD_REQ_PARAM_NAME],
                    method = [RequestMethod.GET],
                    produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBusinessTransactions(@Pattern(regexp = "^\\d{4}-\\d{2}$", message=PERIOD_REQ_PARAM_VALIDATION_ERROR_MESSAGE_POSTFIX)
                                @RequestParam(PERIOD_REQ_PARAM_NAME) period: String):
            ResponseEntity<List<BusinessTransaction>> {

        /**
         * This approach to handing YearMonth validation is suboptimal because it breaks our
         * RestResponseEntityExceptionHandler pattern
         */
        val specifiedPeriod: YearMonth? = try { YearMonth.parse(period) } catch (e: DateTimeParseException) { null }

        return if (specifiedPeriod == null) {

            val transactions: List<BusinessTransaction> = listOf()
            ResponseEntity.badRequest().body(transactions)

        } else {

            val transactions: List<BusinessTransaction> = fetchBusinessTransactionsUseCase.fetchBusinessTransactions(specifiedPeriod)
            ResponseEntity.ok(transactions)

        }

    }
}