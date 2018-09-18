package com.thorgil.cashbook.core.entity

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * BusinessTransactions are a record of all payments (expenses) and receipts (income).
 *
 * To be clear a BusinessTransaction always has a movement of money associated with it (and so can be reconciled with
 * one of the Company's bank accounts) with the exception of BusinessTransactions that have a scheduledDate set and no
 * completedDate. The combination of the former scenario can be used to model future expected BusinessTransactions (this
 * is to help with recording liabilities such as various forms of tax to the IRD).
 *
 * In terms of modelling future transactions, when it comes to certain types of liabilities (e.g. paying over employee
 * income tax to the IRD) it is likely that one future transaction will be linked to many immediately completed
 * BusinessTransactions. This is because one payment to the IRD (in future) would be linked to many individual
 * childTransactions.
 *
 * Future BusinessTransactions could also be used to model when the business is expecting to have invoices paid.
 *
 * In terms of recording a BusinessTransactionType initially what matters is whether it is of type EXPENSE (debit) or
 * INCOME (credit). Breaking it down further can be done of course.
 *
 * Note, whether a BusinessTransaction includes GST is based on the associated company configuration (which is a model
 * of whether the company is registered for GST) at the time when the BusinessTransaction has been created.
 *
 * @param uuid unique identifier for this transaction
 * @param createdTimestamp
 * @param lastUpdateTimestamp
 * @param scheduledDate if this is set and completed is not set then this is a future (scheduled) transaction
 * @param completedDate if this is set the transaction occurred (reconciliation can occur) and no further action
 *                      is expected
 * @param type
 * @param amountInCents this is always exclusive of GST
 * @param gstInCents
 * @param parentTransaction
 * @param childTransactions transactions that are derived from this transaction i.e. they only exist because this
 *                          transaction exists
 * @param evidenceLink
 * @param businessTransactionIssue an issue picked up by the system
 * @param businessTransactionIssueDetail free form text with further detail of the issue
 */
open class BusinessTransaction(val uuid: String = UUID.randomUUID().toString(),
                          val createdTimestamp: LocalDateTime = LocalDateTime.now(),
                          val lastUpdateTimestamp: LocalDateTime = LocalDateTime.now(),
                          val scheduledDate: LocalDate?,
                          val completedDate: LocalDate?,
                          val type: BusinessTransactionType,
                          val amountInCents: Int,
                          val gstInCents: Int,
                          val parentTransaction: BusinessTransaction? = null,
                          val childTransactions: List<BusinessTransaction>? = null,
                          val evidenceLink: String? = null,
                          val businessTransactionIssue: BusinessTransactionIssue? = null,
                          val businessTransactionIssueDetail: String? = null)

/**
 * Note this enum holds a hierarchical data structure.
 *
 * See: https://dzone.com/articles/enum-tricks-hierarchical-data
 */
enum class BusinessTransactionType {
    EXPENSE {
        override fun parent() = null
    },
    INCOME {
        override fun parent() = null
    },
    BANK_FEE_PAYMENT {
        override fun parent() = EXPENSE
    },
    SALARY_PAYMENT {
        override fun parent() = EXPENSE
    },
    SUPPLIER_INVOICE_PAYMENT {
        override fun parent() = EXPENSE
    },
    IRD_SALARY_OVERHEAD_PAYMENT {
        override fun parent() = EXPENSE
    },
    IRD_GST_PAYMENT {
        override fun parent() = EXPENSE
    },
    BANK_WITHHOLDING_TAX {
        override fun parent() = EXPENSE
    },
    INVOICE_PAYMENT {
        override fun parent() = INCOME
    },
    CREDIT_INTEREST_PAYMENT {
        override fun parent() = INCOME
    },
    OWNER_CONTRIBUTION_PAYMENT {
        override fun parent() = INCOME
    };
    abstract fun parent(): BusinessTransactionType?
}

enum class BusinessTransactionIssue {
    GST {
        override fun parent() = null
    },
    INVOICE_PAYMENT_WITH_INCORRECT_GST {
        override fun parent() = GST
    };

    abstract fun parent(): BusinessTransactionIssue?
}