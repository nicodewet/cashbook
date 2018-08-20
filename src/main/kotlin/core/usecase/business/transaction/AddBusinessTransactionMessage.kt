package com.thorgil.cashbook.core.usecase.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransactionType
import java.time.LocalDate

/**
 * @param type at the very least a transaction must be classified as an expense or income but should ideally be finer
 *             grained
 * @param parentTransactionUUID ff
 * @param scheduledDate can be set only if completedDate is null
 * @param completedDate must be set if scheduledDate is null
 * @param amountInCents
 * @param gstInCents
 * @param evidenceLink
 */
data class AddBusinessTransactionMessage(
        val type: BusinessTransactionType,
        val parentTransactionUUID: String? = null,
        val scheduledDate: LocalDate? = null,
        val completedDate: LocalDate?,
        val amountInCents: Int,
        val gstInCents: Int = 0,
        val evidenceLink: String? = null
)