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
                                          @get: FutureOrPresent (message = "{scheduled_date.future_or_present}")
                                          val scheduledDate: LocalDate? = null,
                                          @get: PastOrPresent (message = "{completed_date.past_or_present}")
                                          val completedDate: LocalDate?,
                                          @get: PositiveOrZero
                                          val amountInCents: Int,
                                          @get: PositiveOrZero
                                          val gstInCents: Int = 0,
                                          val evidenceLink: String? = null)