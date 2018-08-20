package com.thorgil.cashbook.entrypoints.rest.business.transaction

import com.thorgil.cashbook.core.entity.BusinessTransactionType
import java.time.LocalDate
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.PastOrPresent

/**
 * @param scheduledDate must be in ISO 8601 local date format e.g. 2018-07-31
 * @param completedDate must be in ISO 8601 local date format e.g. 2018-07-31
 */
data class AddBusinessTransactionPostBody(val type: BusinessTransactionType,
                                          val parentTransactionUUID: String? = null,
                                          @get: FutureOrPresent
                                          val scheduledDate: LocalDate? = null,
                                          @get: PastOrPresent
                                          val completedDate: LocalDate?,
                                          val amountInCents: Int,
                                          val gstInCents: Int = 0,
                                          val evidenceLink: String? = null)