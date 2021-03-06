package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransaction
import com.thorgil.cashbook.core.usecase.business.transaction.AddBusinessTransactionMessage
import com.thorgil.cashbook.core.usecase.business.transaction.AddParentBusinessTransactionUseCase
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
class BusinessTransactionApiEndpoint(private val addBusinessTransactionUseCase: AddParentBusinessTransactionUseCase,
                                     private val fetchBusinessTransactionsUseCase: FetchBusinessTransactionsUseCase) {

    companion object {
        const val BUSINESS_TRANSACTION_END_POINT_URL: String = "/api/business/transactions/**"
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

            val addedBusinessTransaction =  addBusinessTransactionUseCase.addParentBusinessTransaction(addBusinessTransactionMessage)

            return ResponseEntity.ok(AddBusinessTransactionResponse(addedBusinessTransaction.uuid,null))

        } catch (e: BusinessTransactionException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(AddBusinessTransactionResponse(null,e.message))
        }

    }

    fun toBusinessTransactionDTO(transaction: BusinessTransaction): BusinessTransactionDTO {
        val childTransactionUuids = mutableListOf<String>()
        if (transaction.childTransactions != null && transaction.childTransactions.isNotEmpty()) {
            for (childTransaction in transaction.childTransactions) {
                childTransactionUuids.add(childTransaction.uuid)
            }
        }

        val transactionDTO: BusinessTransactionDTO = BusinessTransactionDTO(
                uuid = transaction.uuid,
                createdTimestamp = transaction.createdTimestamp,
                lastUpdateTimestamp = transaction.lastUpdateTimestamp,
                scheduledDate = transaction.scheduledDate,
                completedDate = transaction.completedDate,
                type = transaction.type.toString(),
                amountInCents = transaction.amountInCents,
                gstInCents = transaction.gstInCents,
                parentTransactionUuid = transaction.parentTransaction?.uuid,
                childTransactionUuids = childTransactionUuids,
                evidenceLink = transaction.evidenceLink,
                businessTransactionIssue = transaction.businessTransactionIssue?.toString(),
                businessTransactionIssueDetail = transaction.businessTransactionIssueDetail
        )
        return transactionDTO
    }

    @RequestMapping(value = ["**/{uuid}"],
                    method = [RequestMethod.GET])
    fun getBusinessTransaction(@PathVariable("uuid") uuid: String): ResponseEntity<BusinessTransactionDTO> {
        val transaction: BusinessTransaction? = fetchBusinessTransactionsUseCase.fetchBusinessTransaction(uuid)

        return if (transaction != null) {
            val transactionDTO = toBusinessTransactionDTO(transaction)
            ResponseEntity.ok(transactionDTO)
        } else {
            ResponseEntity.notFound().build()
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
            ResponseEntity<List<BusinessTransactionDTO>> {

        /**
         * This approach to handling YearMonth validation is suboptimal because it breaks our
         * RestResponseEntityExceptionHandler and ValidationErrorDTO response pattern
         *
         * https://stackoverflow.com/questions/46692104/validate-request-parameter-date-in-spring-rest-controller
         */
        val specifiedPeriod: YearMonth? = try { YearMonth.parse(period) } catch (e: DateTimeParseException) { null }

        return if (specifiedPeriod == null) {

            val transactions: List<BusinessTransactionDTO> = listOf()
            ResponseEntity.badRequest().body(transactions)

        } else {

            val transactions: List<BusinessTransaction> = fetchBusinessTransactionsUseCase.fetchBusinessTransactions(specifiedPeriod)
            val transactionsResponse = mutableListOf<BusinessTransactionDTO>()

            for (transaction in transactions) {

                val transactionDTO = toBusinessTransactionDTO(transaction)
                transactionsResponse.add(transactionDTO)
            }

            ResponseEntity.ok(transactionsResponse)

        }

    }
}