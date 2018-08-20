package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.*
import com.thorgil.cashbook.core.usecase.company.GetCompany
import java.time.LocalDate

interface AddParentBusinessTransaction {

    /**
     * Add a cashbook transaction to the system.
     *
     * Linking to a parent transaction is not yet supported.
     *
     * @param businessTransaction cannot itself have a parent
     * @throws BusinessTransactionException indicates a business rule validation error
     * @return when adding a child transaction the parent transaction should be returned in the response
     */
    fun addBusinessTransaction(businessTransaction: AddBusinessTransactionMessage): BusinessTransaction

}

class AddBusinessTransactionUseCase(private val companyProvider: GetCompany,
                                    private val addBusinessTransactionInRepo: AddBusinessTransactionInRepository): AddParentBusinessTransaction {

    override fun addBusinessTransaction(businessTransaction: AddBusinessTransactionMessage): BusinessTransaction {

        val company: Company = companyProvider.getCompany()

        if (businessTransaction.parentTransactionUUID != null) {
            throw BusinessTransactionException("Parent business transaction cannot itself have a parent")
        }

        if (businessTransaction.completedDate != null && businessTransaction.scheduledDate != null) {
            throw BusinessTransactionException("A completed BusinessTransaction cannot also be scheduled")
        }

        if (businessTransaction.scheduledDate == null &&
                businessTransaction.completedDate != null &&
                LocalDate.now().isBefore(businessTransaction.completedDate)) {
            throw BusinessTransactionException("A completed BusinessTransaction must have a date equal to or before today")
        }

        if (businessTransaction.completedDate == null &&
                businessTransaction.scheduledDate != null &&
                LocalDate.now().isAfter(businessTransaction.scheduledDate)) {
            throw BusinessTransactionException("A scheduled BusinessTransaction must be scheduled for a future date")
        }

        if (businessTransaction.amountInCents <= 0) {
            throw BusinessTransactionException("BusinessTransaction amount must be greater than 0 when supplied")
        }

        if (businessTransaction.gstInCents < 0) {
            throw BusinessTransactionException("BusinessTransaction GST amount must be greater or equal to than 0 when supplied")
        }

        var businessTransactionIssue: BusinessTransactionIssue? = null
        var businessTransactionIssueDetail: String? = null

        if (company.gstStatus == GstStatus.REGISTERED &&
                businessTransaction.type == BusinessTransactionType.INVOICE_PAYMENT &&
                businessTransaction.amountInCents != 0) {

            val calculatedGstInCents = (businessTransaction.amountInCents * Company.GST_PERCENTAGE).toInt()

            if (calculatedGstInCents != businessTransaction.gstInCents) {
                businessTransactionIssue = BusinessTransactionIssue.INVOICE_PAYMENT_WITH_INCORRECT_GST
                businessTransactionIssueDetail = "GST of " + calculatedGstInCents + " expected"
            }

        }

        val theBusinessTransaction
                = BusinessTransaction(type = businessTransaction.type,
                                    scheduledDate = businessTransaction.scheduledDate,
                                    completedDate = businessTransaction.completedDate,
                                    amountInCents = businessTransaction.amountInCents,
                                    gstInCents = businessTransaction.gstInCents,
                                    evidenceLink = businessTransaction.evidenceLink,
                                    businessTransactionIssue = businessTransactionIssue,
                                    businessTransactionIssueDetail = businessTransactionIssueDetail)

        addBusinessTransactionInRepo.addBusinessTransactionInRepository(theBusinessTransaction)

        return theBusinessTransaction

    }

}