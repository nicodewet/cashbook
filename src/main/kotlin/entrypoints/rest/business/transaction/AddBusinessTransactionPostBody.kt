package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransactionType
import java.time.LocalDate
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero

/**
 * @param scheduledDate should be serialized in format String of "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
 * @param completedDate should be serialized in format String of "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
 */
data class AddBusinessTransactionPostBody(val type: BusinessTransactionType,
                                          val parentTransactionUUID: String? = null,
                                          @get: FutureOrPresent (message = "Scheduled date must be a future or present date")
                                          val scheduledDate: LocalDate? = null,
                                          @get: PastOrPresent (message = "Completed date must be a past or present date")
                                          val completedDate: LocalDate?,
                                          @get: PositiveOrZero
                                          val amountInCents: Int,
                                          @get: PositiveOrZero
                                          val gstInCents: Int = 0,
                                          val evidenceLink: String? = null)